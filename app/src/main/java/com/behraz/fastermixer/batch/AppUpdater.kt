package com.behraz.fastermixer.batch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AppUpdater private constructor(
    private val context: Context,
    private val link: String,
    downloadLocation: String,
    private val newVersionCode: Long,
    private val interactions: Interactions
) {

    companion object {
        private lateinit var instance: AppUpdater
        @Synchronized
        fun getInstance(
            context: Activity,
            link: String,
            downloadLocation: String,
            newVersionCode: Long,
            interactions: Interactions
        ): AppUpdater {
            if (!::instance.isInitialized) {
                instance = AppUpdater(
                    context.applicationContext,
                    link,
                    downloadLocation,
                    newVersionCode,
                    interactions
                )
            }
            return instance
        }
    }


    private val downloadLocationFile = File(downloadLocation)

    fun startIfNeeded() {
        if (newVersionCode > BuildConfig.VERSION_CODE) {
            CoroutineScope(IO).launch {
                val apkFileVersionCode = checkIsDownloadedAndVersionCode()
                if (apkFileVersionCode == -1L) { //not downloaded
                    CoroutineScope(Main).launch { interactions.onDownloadStarted() }
                    println("debug:download and install, No Download Available")
                    download()
                    install()
                } else {
                    if (newVersionCode > apkFileVersionCode) {
                        println("debug:delete download and install -> newVersionCode:$newVersionCode , lastDownloadVersionCode:$apkFileVersionCode")
                        CoroutineScope(Main).launch { interactions.onDownloadStarted() }
                        downloadLocationFile.delete()
                        download()
                        install()
                    } else {
                        println("debug:just install  -> newVersionCode:$newVersionCode , lastDownloadVersionCode:$apkFileVersionCode")
                        install()
                    }
                }
            }
        } else {
            println("debug: no install needed -> newVersionCode:$newVersionCode , currentVersionCode:${BuildConfig.VERSION_CODE}")
        }
    }

    private fun install() {
        if (downloadLocationFile.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            val type = "application/vnd.android.package-archive"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val downloadedApk =
                    FileProvider.getUriForFile(
                        context,
                        context.applicationContext.packageName + ".provider",
                        downloadLocationFile
                    )
                //intent.setData(downloadedApk)
                intent.setDataAndType(downloadedApk, type)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                intent.setDataAndType(Uri.fromFile(downloadLocationFile), type)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } else {
            interactions.onDownloadCancelled("فایل مورد نظر یافت نشد")
        }
    }

    private fun checkIsDownloadedAndVersionCode(): Long {
        if (downloadLocationFile.exists()) {
            val pm: PackageManager = context.packageManager
            pm.getPackageArchiveInfo(downloadLocationFile.absolutePath, 0)!!.let { info ->
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    println("debug:download:VersionCode : " + info.longVersionCode + ", VersionName : " + info.versionName)
                    info.longVersionCode
                } else {
                    println("debug:download:VersionCode : " + info.versionCode + ", VersionName : " + info.versionName)
                    info.versionCode.toLong()
                }
            }
        } else {
            return -1
        }
    }

    private suspend fun download() {
        withContext(IO) {
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download

            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            val wl = (context.getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                javaClass.name
            )
            wl.acquire(10 * 60 * 1000L /*10 minutes*/)


            var input: InputStream? = null
            var output: FileOutputStream? = null
            var connection: HttpURLConnection? = null
            try {
                val url =
                    URL(link)
                connection = url.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    CoroutineScope(Main).launch {
                        interactions.onDownloadCancelled(connection.responseMessage)
                    }
                    println("debug: Download: Server returns response code ${connection.responseCode} :${connection.responseMessage}")
                } else {
                    val fileLength =
                        connection.contentLength //if server don't report the length it will return -1
                    input = connection.inputStream
                    output = FileOutputStream(downloadLocationFile)

                    val data = ByteArray(4096)
                    var total = 0L

                    while (true) {
                        val count = input.read(data)
                        if (count == -1)
                            break
                        total += count
                        if (fileLength > 0) {
                            val percentage = (total * 100 / fileLength).toInt()
                            println("debug: Download: $percentage")
                            CoroutineScope(Main).launch {
                                interactions.onProgressUpdate(percentage)
                            }
                        }
                        output.write(data, 0, count)
                    }
                    CoroutineScope(Main).launch {
                        interactions.onProgressUpdate(100)
                    }
                }
            } catch (ex: IOException) {
                println("debug: ${ex.message}")
                CoroutineScope(Main).launch {
                    interactions.onDownloadCancelled(ex.message ?: "")
                }
            } finally {
                input?.close()
                output?.close()
                connection?.disconnect()
                wl.release()
            }
        }
    }

    interface Interactions {
        fun onDownloadStarted()
        fun onProgressUpdate(progress: Int)
        fun onDownloadCancelled(message: String)
    }

}
package com.behraz.fastermixer.batch.utils.general

import android.app.Activity
import android.net.wifi.WifiManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.net.NetworkInterface
import java.util.*


fun Activity.getMAC(): String? {
    val wifiMan = applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
    val wifiInf = wifiMan.connectionInfo
    return if (wifiInf.macAddress == "02:00:00:00:00:00") {
        getAdressMacByInterface() ?: getAddressMacByFile(wifiMan)
    } else {
        wifiInf.macAddress
    }
}


//getting mac by interface
private fun getAdressMacByInterface(): String? {
    try {
        val all: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (nif in all) {
            if (nif.name.equals("wlan0", true)) {
                val macBytes: ByteArray = nif.getHardwareAddress() ?: return ""
                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:", b))
                }
                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        }
    } catch (e: Exception) {
        Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ")
    }
    return null
}

//getting mac by file
private fun getAddressMacByFile(wifiMan: WifiManager): String? {
    try {
        val ret: String
        val wifiState = wifiMan.wifiState
        wifiMan.isWifiEnabled = true
        val fl = File("/sys/class/net/wlan0/address")
        val fin = FileInputStream(fl)
        val builder = java.lang.StringBuilder()
        var ch: Int
        while (fin.read().also { ch = it } != -1) {
            builder.append(ch.toChar())
        }
        ret = builder.toString()
        fin.close()
        val enabled = WifiManager.WIFI_STATE_ENABLED == wifiState
        wifiMan.isWifiEnabled = enabled
        return ret
    } catch (ex: java.lang.Exception) {
        return null
    }
}
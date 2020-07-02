package com.behraz.fastermixer.batch.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.LayoutRecordDialogBinding
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.millisToTimeString
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.RecordingFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.concurrent.fixedRateTimer


class RecordingDialogFragment : DialogFragment() {

    companion object {
        private const val LOG_TAG = "debug:AudioRecord"
    }

    private lateinit var viewModel: RecordingFragmentViewModel
    private lateinit var mBinding: LayoutRecordDialogBinding

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private val fileName: String
        get() =
            "${activity!!.externalCacheDir!!.absolutePath}/${System.currentTimeMillis()}.3gp"


    private lateinit var timer: Timer

    //private var interactions: Interactions? = null
    private var tick = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        roundDialog()
        viewModel = ViewModelProvider(this).get(RecordingFragmentViewModel::class.java)
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_record_dialog, container, false)
        initViews()
        startRecording()
        observeViewModel()
        return mBinding.root
    }

    private fun observeViewModel() {
        viewModel.sendVoiceResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            showProgress(false)
            if (it != null) {
                if (it.isSucceed) {
                    toast("پیام ارسال شد")
                    dismiss()
                } else {
                    toast(it.message)
                }
            } else {
                toast(Constants.SERVER_ERROR)
            }
        })
    }

    private fun initViews() {
        isCancelable = false

        mBinding.btnRecord.setOnClickListener {
            stopRecording()
        }

        mBinding.btnRetry.setOnClickListener {
            startRecording()
        }

        mBinding.btnSend.setOnClickListener {
            showProgress(true)
            //todo uncomment this line viewModel.sendRecordFile(File(fileName))
            Handler().postDelayed({ //TODO ui test purpose
                showProgress(false)
                dismiss()
            }, 2000)

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //  interactions = activity as Interactions
    }


    private fun roundDialog() {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set transparent background and no title
        dialog?.let { dialog ->
            dialog.window?.let { window ->
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.requestFeature(Window.FEATURE_NO_TITLE)
            }
        }
    }


    private fun showProgress(shouldShow: Boolean) {
        if (shouldShow) {
            mBinding.progressBar.visibility = View.VISIBLE
            mBinding.rippleAnimView.visibility = View.VISIBLE
            mBinding.frameBtns.visibility = View.GONE
            mBinding.tvRecordMessage.text = "لطفا کمی صبر کنید.."
            mBinding.tvTick.visibility = View.VISIBLE
        } else {
            mBinding.progressBar.visibility = View.GONE
            mBinding.rippleAnimView.visibility = View.GONE
            mBinding.frameBtns.visibility = View.VISIBLE
            mBinding.tvRecordMessage.text = "ضبط پایان یافت"
            mBinding.tvTick.visibility = View.VISIBLE
        }
    }


    //---------------
    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
                start()

                timer = fixedRateTimer(period = 1000L) {
                    tick++
                    if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                        mBinding.tvTick.post {
                            mBinding.tvTick.text = millisToTimeString(tick * 1000L).substring(10)
                        }
                    }
                }

                mBinding.btnRecord.visibility = View.VISIBLE
                mBinding.rippleAnimView.startRippleAnimation()
                mBinding.rippleAnimView.visibility = View.VISIBLE
                mBinding.frameBtns.visibility = View.GONE

                mBinding.tvRecordMessage.text = "در حال ضبط کردن.."
                mBinding.tvTick.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.deep_red
                    )
                )
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

        }
    }


    private fun stopRecording() {
        recorder?.apply {
            mBinding.tvRecordMessage.text = "لطفا کمی صبر کنید.."
            mBinding.rippleAnimView.stopRippleAnimation()
            mBinding.btnRecord.visibility = View.GONE
            mBinding.progressBar.visibility = View.VISIBLE
            CoroutineScope(IO).launch {
                stop()
                release()
                CoroutineScope(Main).launch {
                    mBinding.progressBar.visibility = View.GONE
                    mBinding.rippleAnimView.visibility = View.GONE
                    mBinding.frameBtns.visibility = View.VISIBLE
                    mBinding.tvRecordMessage.text = "ضبط پایان یافت"
                    mBinding.tvTick.setTextColor(Color.WHITE)
                }
                recorder = null
            }
        }
        timer.cancel()
        timer.purge()
        tick = 0
    }
//---------------


}
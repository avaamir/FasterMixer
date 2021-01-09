package com.behraz.fastermixer.batch.ui.fragments.admin.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentDrawRoadBinding
import com.behraz.fastermixer.batch.ui.animations.crossfade
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.DrawRoadFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.ReportViewModel

class DrawRoadFragment : Fragment(), SeekBar.OnSeekBarChangeListener {
    private lateinit var mBinding: FragmentDrawRoadBinding
    private lateinit var reportViewModel: ReportViewModel
    private lateinit var viewModel: DrawRoadFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        viewModel = ViewModelProvider(this).get(DrawRoadFragmentViewModel::class.java)
        viewModel.request = reportViewModel.request

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_draw_road, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()
        viewModel.getDrawRoadReport()
    }


    private fun initViews() = mBinding.run {
        ivResumePause.setOnClickListener {
            if (
                viewModel.currentPointIndex.value == ((viewModel.drawRoadReport.value?.entity?.size
                    ?: 0) - 1)
            ) {
                viewModel.currentPointIndex.value = 0
            }
            viewModel.isPaused = !viewModel.isPaused
        }
        ivNext.setOnClickListener {
            //TODO ??
        }
        ivPrev.setOnClickListener {
            //TODO ??
        }
        seekBar.setOnSeekBarChangeListener(this@DrawRoadFragment)
    }

    private fun subscribeObservers() = viewModel.run {
        isPausedLiveData.observe(viewLifecycleOwner) { isPaused ->
            mBinding.ivResumePause.setImageResource(
                if (isPaused) R.drawable.ic_resume else R.drawable.ic_pause
            )
        }

        drawRoadReport.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isSucceed) {
                    val lastIndex = it.entity!!.size - 1
                    mBinding.seekBar.max = lastIndex
                    crossfade(mBinding.gpPlayer, mBinding.progressBar)
                    //TODO update dataTime
                    //mBinding.tvLength.text = it.entity[lastIndex].dateTime
                    //mBinding.tvDuration.text = it.entity[0].dateTime
                } else {
                    toast(it.message)
                }
            } else {
                mBinding.progressBar.visibility = View.INVISIBLE
                snack(Constants.SERVER_ERROR, onAction = {
                    tryGetDrawRoadAgain()
                    mBinding.progressBar.visibility = View.VISIBLE
                })
            }
        }

        currentPointIndex.observe(viewLifecycleOwner) {
            if (mBinding.seekBar.progress != it) {
                mBinding.seekBar.progress = it
                //TODO update dataTime
                //val points = viewModel.drawRoadReport.value!!.entity!!
                //mBinding.tvDuration.text = points[it].dateTime
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            println("debux: seekBar changed fromUser")
            viewModel.currentPointIndex.value = progress
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}


}
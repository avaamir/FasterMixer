package com.behraz.fastermixer.batch.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentChooseReportDateBinding

class ChooseReportDateFragment : Fragment() {
    private lateinit var mBinding: FragmentChooseReportDateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_choose_report_date,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val data = arrayOf("فروردین", "اردیبهشت", "Tokyo", "Paris")
        mBinding.dayPicker.minValue = 0
        mBinding.dayPicker.maxValue = data.size - 1
        mBinding.dayPicker.displayedValues = data
    }


}
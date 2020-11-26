package com.behraz.fastermixer.batch.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentChooseReportDateBinding
import com.behraz.fastermixer.batch.utils.general.howManyYearsAfter
import com.behraz.fastermixer.batch.utils.general.now
import java.util.*

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
        //year after 20/3/2020 miladi = 1/1/1399 shamsi
        var yearsAfter = now().howManyYearsAfter(2020, Calendar.MARCH, 1)
        if (yearsAfter < 0) yearsAfter = 0

        val years = List(yearsAfter + 1) { "${it + 1399}" }.reversed().toTypedArray()
        val months = resources.getStringArray(R.array.shami_months)
        val days = List(31) { "${it + 1}" }.toTypedArray()


        mBinding.monthPicker.initPicker(months)
        mBinding.dayPicker.initPicker(days)
        mBinding.yearPicker.initPicker(years)


        mBinding.endMonthPicker.initPicker(months)
        mBinding.endDayPicker.initPicker(days)
        mBinding.endYearPicker.initPicker(years)
    }


}
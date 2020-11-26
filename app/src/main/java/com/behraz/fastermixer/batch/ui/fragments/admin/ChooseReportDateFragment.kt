package com.behraz.fastermixer.batch.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentChooseReportDateBinding
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.howManyYearsAfter
import com.behraz.fastermixer.batch.utils.general.now
import com.behraz.fastermixer.batch.utils.general.toast
import java.util.*

class ChooseReportDateFragment : Fragment(), NumberPicker.OnValueChangeListener {
    private lateinit var reportType: String
    private lateinit var mBinding: FragmentChooseReportDateBinding

    private val startDate = Array(3) { "" } //0:day , 1:month , 2:year
    private val endDate = Array(3) { "" }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::reportType.isInitialized)
            reportType = requireArguments().get(Constants.INTENT_REPORT_TYPE) as String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        mBinding.btnContinue.setOnClickListener {
            val notAllowedItems = listOf("روز", "ماه", "سال")
            for (i in 0..2) {
                println("Debux: ${startDate[i]}, ${endDate[i]}")
                if (startDate[i].isEmpty() || endDate[i].isEmpty()) {
                    toast("لطفا بازه گزارش گیری را مشخص کنید")
                    return@setOnClickListener
                } else {
                    if (startDate[i] in notAllowedItems || endDate[i] in notAllowedItems) {
                        toast("لطفا بازه گزارش گیری را مشخص کنید")
                        return@setOnClickListener
                    }
                }
            }
            findNavController().navigate(
                when (reportType) {
                    Constants.REPORT_TYPE_FULL -> R.id.action_chooseReportDateFragment_to_chooseReportEquipmentFragment
                    Constants.REPORT_TYPE_SUMMERY -> TODO("not implemented")
                    Constants.REPORT_TYPE_DRAW_ROAD -> TODO("not implemented")
                    else -> throw Exception("report Type is not valid: $reportType")
                },
                bundleOf(
                    Constants.INTENT_REPORT_START_DATE to startDate,
                    Constants.INTENT_REPORT_END_DATE to endDate
                )
            )
        }


        //year after 20/3/2020 miladi = 1/1/1399 shamsi
        var yearsAfter = now().howManyYearsAfter(2020, Calendar.MARCH, 1)
        if (yearsAfter < 0) yearsAfter = 0

        val years =
            (listOf("سال") + List(yearsAfter + 1) { "${it + 1399}" }.reversed()).toTypedArray()
        val months = (listOf("ماه") + resources.getStringArray(R.array.shami_months)).toTypedArray()
        val days = (listOf("روز") + List(31) { "${it + 1}" }).toTypedArray()


        mBinding.monthPicker.initPicker(months)
        mBinding.dayPicker.initPicker(days)
        mBinding.yearPicker.initPicker(years)


        mBinding.endMonthPicker.initPicker(months)
        mBinding.endDayPicker.initPicker(days)
        mBinding.endYearPicker.initPicker(years)

        mBinding.monthPicker.setOnValueChangedListener(this)
        mBinding.dayPicker.setOnValueChangedListener(this)
        mBinding.yearPicker.setOnValueChangedListener(this)
        mBinding.endYearPicker.setOnValueChangedListener(this)
        mBinding.endDayPicker.setOnValueChangedListener(this)
        mBinding.endMonthPicker.setOnValueChangedListener(this)
    }

    override fun onValueChange(picker: NumberPicker, prev: Int, current: Int) {
        val value = picker.displayedValues[current]
        when (picker.id) {
            mBinding.dayPicker.id -> {
                startDate[0] = value
            }
            mBinding.endDayPicker.id -> {
                endDate[0] = value
            }

            mBinding.monthPicker.id -> {
                startDate[1] = value
            }
            mBinding.endMonthPicker.id -> {
                endDate[1] = value
            }

            mBinding.yearPicker.id -> {
                startDate[2] = value
            }
            mBinding.endYearPicker.id -> {
                endDate[2] = value
            }
        }
    }


}
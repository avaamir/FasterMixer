package com.behraz.fastermixer.batch.ui.fragments.admin.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentChooseReportDateBinding
import com.behraz.fastermixer.batch.ui.fragments.navigate
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.ReportViewModel
import java.util.*

class ChooseReportDateFragment : Fragment(), NumberPicker.OnValueChangeListener {
    private lateinit var mBinding: FragmentChooseReportDateBinding
    private lateinit var viewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
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

    private fun initViews() = mBinding.run {

        btnContinue.setOnClickListener {
            if (viewModel.request.hasEmptyDateField) {
                toast("لطفا بازه گزارش گیری را مشخص کنید")
                return@setOnClickListener
            }

            val isEndDateBiggerThanStartDate = viewModel.request.run {
                fun compare(end: String?, start: String?): Boolean? {
                    val mStart = start!!.toInt()
                    val mEnd = end!!.toInt()
                    return when {
                        mEnd > mStart -> true
                        mEnd == mStart -> null
                        else -> false
                    }
                }
                compare(endYear, startYear) ?:
                compare(endMonth, startMonth) ?:
                compare(endDate, startDate) ?:
                compare(endHour, startHour) ?:
                compare(endMinute, startMinute) ?:
                compare(endSecond, startSecond) ?: false
            }
            if (!isEndDateBiggerThanStartDate) {
                toast("پایان بازه باید از شروع بازه بزرگتر باشد")
                return@setOnClickListener
            }

            navigate(R.id.action_chooseReportDateFragment_to_chooseReportEquipmentFragment)
        }


        //year after 20/3/2020 miladi = 1/1/1399 shamsi
        var yearsAfter = now().howManyYearsAfter(2020, Calendar.MARCH, 1)
        if (yearsAfter < 0) yearsAfter = 0

        val years =
            (listOf("سال") + List(yearsAfter + 1) { "${it + 1399}" }.reversed()).toTypedArray()
        val months = (listOf("ماه") + resources.getStringArray(R.array.shami_months)).toTypedArray()
        val days =
            (listOf("روز") + List(31) { (if (it < 10) "0" else "") + (it + 1) }).toTypedArray()

        val hours = (listOf("ساعت") + List(24) { (if (it < 10) "0" else "") + it }).toTypedArray()
        val minutes =
            (listOf("دقیقه") + List(60) { (if (it < 10) "0" else "") + it }).toTypedArray()
        val seconds =
            (listOf("ثانیه") + List(60) { (if (it < 10) "0" else "") + it }).toTypedArray()



        monthPicker.initPicker(months)
        dayPicker.initPicker(days)
        yearPicker.initPicker(years)
        startHourPicker.initPicker(hours)
        startMinutePicker.initPicker(minutes)
        startSecondPicker.initPicker(seconds)

        endMonthPicker.initPicker(months)
        endDayPicker.initPicker(days)
        endYearPicker.initPicker(years)
        endHourPicker.initPicker(hours)
        endMinutePicker.initPicker(minutes)
        endSecondPicker.initPicker(seconds)


        val request = viewModel.request
        request.startMonth?.let {
            monthPicker.setValue(numberToSolarMonthName(it.toInt()))
        }
        dayPicker.setValue(request.startDate)
        yearPicker.setValue(request.startYear)
        startHourPicker.setValue(request.startHour)
        startMinutePicker.setValue(request.startMinute)
        startSecondPicker.setValue(request.startSecond)

        request.endMonth?.let {
            endMonthPicker.setValue(numberToSolarMonthName(it.toInt()))
        }
        endDayPicker.setValue(request.endDate)
        endYearPicker.setValue(request.endYear)
        endHourPicker.setValue(request.endHour)
        endMinutePicker.setValue(request.endMinute)
        endSecondPicker.setValue(request.endSecond)


        monthPicker.setOnValueChangedListener(this@ChooseReportDateFragment)
        dayPicker.setOnValueChangedListener(this@ChooseReportDateFragment)
        yearPicker.setOnValueChangedListener(this@ChooseReportDateFragment)
        startHourPicker.setOnValueChangedListener(this@ChooseReportDateFragment)
        startMinutePicker.setOnValueChangedListener(this@ChooseReportDateFragment)
        startSecondPicker.setOnValueChangedListener(this@ChooseReportDateFragment)

        endYearPicker.setOnValueChangedListener(this@ChooseReportDateFragment)
        endDayPicker.setOnValueChangedListener(this@ChooseReportDateFragment)
        endMonthPicker.setOnValueChangedListener(this@ChooseReportDateFragment)
        endHourPicker.setOnValueChangedListener(this@ChooseReportDateFragment)
        endMinutePicker.setOnValueChangedListener(this@ChooseReportDateFragment)
        endSecondPicker.setOnValueChangedListener(this@ChooseReportDateFragment)
    }


    override fun onValueChange(picker: NumberPicker, prev: Int, current: Int) {

        val notAllowedItems = listOf("روز", "ماه", "سال", "ساعت", "دقیقه", "ثانیه")
        val value = picker.displayedValues[current].let {
            if (it in notAllowedItems)
                null
            else
                it
        }


        mBinding.run {
            val request = viewModel.request
            when (picker.id) {
                dayPicker.id -> request.startDate = value
                monthPicker.id -> request.startMonth =
                    if (value != null) solarMonthNameToNumber(value) else null
                yearPicker.id -> request.startYear = value
                startHourPicker.id -> request.startHour = value
                startMinutePicker.id -> request.startMinute = value
                startSecondPicker.id -> request.startSecond = value
                endDayPicker.id -> request.endDate = value
                endMonthPicker.id -> request.endMonth =
                    if (value != null) solarMonthNameToNumber(value) else null
                endYearPicker.id -> request.endYear = value
                endHourPicker.id -> request.endHour = value
                endMinutePicker.id -> request.endMinute = value
                endSecondPicker.id -> request.endSecond = value
            }
        }
    }


}
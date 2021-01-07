package com.behraz.fastermixer.batch.ui.fragments.admin.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentChooseReportDateBinding
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.ui.fragments.navigate
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.howManyYearsAfter
import com.behraz.fastermixer.batch.utils.general.now
import com.behraz.fastermixer.batch.utils.general.toast
import java.util.*

class ChooseReportDateFragment : Fragment(), NumberPicker.OnValueChangeListener {
    private lateinit var reportType: String
    private lateinit var mBinding: FragmentChooseReportDateBinding

    private val request = GetReportRequest()

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
            if (request.hasEmptyDateField) {
                toast("لطفا بازه گزارش گیری را مشخص کنید")
                return@setOnClickListener
            }


            navigate(
                when (reportType) {
                    Constants.REPORT_TYPE_DRAW_ROAD, Constants.REPORT_TYPE_FULL, Constants.REPORT_TYPE_SUMMERY -> R.id.action_chooseReportDateFragment_to_chooseReportEquipmentFragment
                    else -> throw Exception("report Type is not valid: $reportType")
                },
                bundleOf(
                    Constants.INTENT_REPORT_TYPE to reportType,
                    Constants.INTENT_REPORT_GET_REPORT_REQ to request,
                )
            )
        }


        //year after 20/3/2020 miladi = 1/1/1399 shamsi
        var yearsAfter = now().howManyYearsAfter(2020, Calendar.MARCH, 1)
        if (yearsAfter < 0) yearsAfter = 0

        val years = (listOf("سال") + List(yearsAfter + 1) { "${it + 1399}" }.reversed()).toTypedArray()
        val months = (listOf("ماه") + resources.getStringArray(R.array.shami_months)).toTypedArray()
        val days = (listOf("روز") + List(31) { "${it + 1}" }).toTypedArray()

        val hours = (listOf("ساعت") + List(25) { "${it + 1}" }).toTypedArray()
        val minutes = (listOf("دقیقه") + List(61) { "${it + 1}" }).toTypedArray()
        val seconds = (listOf("ثانیه") + List(61) { "${it + 1}" }).toTypedArray()


        mBinding.monthPicker.initPicker(months)
        mBinding.dayPicker.initPicker(days)
        mBinding.yearPicker.initPicker(years)
        mBinding.startHourPicker.initPicker(hours)
        mBinding.startMinutePicker.initPicker(minutes)
        mBinding.startSecondPicker.initPicker(seconds)


        mBinding.endMonthPicker.initPicker(months)
        mBinding.endDayPicker.initPicker(days)
        mBinding.endYearPicker.initPicker(years)
        mBinding.endHourPicker.initPicker(hours)
        mBinding.endMinutePicker.initPicker(minutes)
        mBinding.endSecondPicker.initPicker(seconds)

        mBinding.monthPicker.setOnValueChangedListener(this)
        mBinding.dayPicker.setOnValueChangedListener(this)
        mBinding.yearPicker.setOnValueChangedListener(this)
        mBinding.startHourPicker.setOnValueChangedListener(this)
        mBinding.startMinutePicker.setOnValueChangedListener(this)
        mBinding.startSecondPicker.setOnValueChangedListener(this)

        mBinding.endYearPicker.setOnValueChangedListener(this)
        mBinding.endDayPicker.setOnValueChangedListener(this)
        mBinding.endMonthPicker.setOnValueChangedListener(this)
        mBinding.endHourPicker.setOnValueChangedListener(this)
        mBinding.endMinutePicker.setOnValueChangedListener(this)
        mBinding.endSecondPicker.setOnValueChangedListener(this)
    }

    override fun onValueChange(picker: NumberPicker, prev: Int, current: Int) {

        val notAllowedItems = listOf("روز", "ماه", "سال", "ساعت", "دقیقه", "ثانیه")
        val value = picker.displayedValues[current].let {
            if (it in notAllowedItems)
                null
            else
                it
        }

        when (picker.id) {
            mBinding.dayPicker.id -> {
                request.startDate = value
            }
            mBinding.monthPicker.id -> {
                request.startMonth = value
            }
            mBinding.yearPicker.id -> {
                request.startYear = value
            }
            mBinding.startHourPicker.id -> {
                request.startHour = value
            }
            mBinding.startMinutePicker.id -> {
                request.startMinute = value
            }
            mBinding.startSecondPicker.id -> {
                request.startSecond = value
            }
            mBinding.endDayPicker.id -> {
                request.endDate = value
            }
            mBinding.endMonthPicker.id -> {
                request.endMonth = value
            }
            mBinding.endYearPicker.id -> {
                request.endYear = value
            }
            mBinding.endHourPicker.id -> {
                request.startHour = value
            }
            mBinding.endMinutePicker.id -> {
                request.startMinute = value
            }
            mBinding.endSecondPicker.id -> {
                request.startSecond = value
            }
        }
    }


}
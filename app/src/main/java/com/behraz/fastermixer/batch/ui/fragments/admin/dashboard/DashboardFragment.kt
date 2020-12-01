package com.behraz.fastermixer.batch.ui.fragments.admin.dashboard

import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.FasterMixerApplication
import com.behraz.fastermixer.batch.databinding.LayoutDashboardFragmentBinding
import com.behraz.fastermixer.batch.models.enums.EquipmentState
import com.behraz.fastermixer.batch.ui.fragments.navigate
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.createSpannableString
import com.behraz.fastermixer.batch.utils.general.exhaustive
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF

class DashboardFragment : Fragment(), OnChartValueSelectedListener {


    private var isFirstEqLoad = true
    private var isFirstReqLoad = true

    private val colors = listOf(
        Color.parseColor("#0090ff"),
        Color.parseColor("#fbc02d"),
        Color.parseColor("#FF0022"),
        Color.parseColor("#933c94")
    )

    private lateinit var adminActivityViewModel: AdminActivityViewModel

    private lateinit
    var mBinding: LayoutDashboardFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminActivityViewModel =
            ViewModelProvider(requireActivity()).get(AdminActivityViewModel::class.java)
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_dashboard_fragment, container, false)
        initViews()
        subscribeObservers()
        return mBinding.root
    }

    private fun initViews() {
        initPieChart(mBinding.requestChart, "درخواست ها", hashMapOf())
        initPieChart(mBinding.vehiclesChart, "وضعیت ماشین آلات", hashMapOf())

        mBinding.requestChart.legend.isEnabled = false
        mBinding.requestChart.invalidate()
        mBinding.vehiclesChart.legend.isEnabled = false
        mBinding.vehiclesChart.invalidate()

        mBinding.frameShowRequestDetails.setOnClickListener {
            navigate(R.id.action_dashboardFragment_to_requestsFragment)
        }

        mBinding.frameShowEquipmentsDetails.setOnClickListener {
            adminActivityViewModel.eventOnShowEquipmentsDetails.value = Event(Unit)
        }
    }


    private fun subscribeObservers() {
        adminActivityViewModel.plans.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.isSucceed) {
                    if (mBinding.frameRequests.visibility != View.VISIBLE) {
                        mBinding.frameRequests.visibility = View.VISIBLE
                        TransitionManager.beginDelayedTransition(
                            mBinding.frameRequests,
                            AutoTransition()
                        )
                    }
                    var sum = 0.0
                    var delivered = 0.0

                    it.entity?.let { plans ->
                        mBinding.tvRequestCount.text = plans.size.toString()
                        plans.forEach { plan ->
                            sum += plan.plannedAmount
                            delivered += plan.sentAmount
                        }
                        mBinding.tvTotalRequestVolume.text = sum.toString()
                    }

                    val remain = sum - delivered

                    setPieData(
                        mBinding.requestChart, hashMapOf(
                            Pair("تحویلی", delivered.toFloat()),
                            Pair("مانده", remain.toFloat())
                        ),
                        shouldAnimate = isFirstReqLoad
                    )
                    isFirstReqLoad = false
                } else {
                    mBinding.frameRequests.visibility = View.GONE
                    TransitionManager.beginDelayedTransition(
                        mBinding.frameRequests,
                        AutoTransition()
                    )
                }
            } else {
                //TODO
            }
        })

        adminActivityViewModel.equipments.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.isSucceed) {
                    it.entity?.let { equipments ->
                        var fixing = 0
                        var off = 0
                        var on = 0
                        var other = 0

                        equipments.forEach { equipment ->
                            when (equipment.state) {
                                EquipmentState.Fixing -> fixing++
                                EquipmentState.Off -> off++
                                EquipmentState.Using -> on++
                                EquipmentState.Other -> other++
                            }.exhaustive()
                        }

                        mBinding.tvOn.text = on.toString()
                        mBinding.tvOff.text = off.toString()
                        mBinding.tvRepair.text = fixing.toString()

                        val vehicleChartMap = hashMapOf(
                            Pair("روشن", on.toFloat()),
                            Pair("خاموش", off.toFloat()),
                            Pair("تعمیری", fixing.toFloat())
                        )

                        if (other != 0) {
                            vehicleChartMap["دیگر"] = other.toFloat()
                        }

                        setPieData(
                            mBinding.vehiclesChart, vehicleChartMap,
                            shouldAnimate = isFirstEqLoad
                        )
                        isFirstEqLoad = false
                    }

                } else {
                    //TODO
                }
            } else {
                //TODO
            }
        })
    }


    private fun initPieChart(
        chart: PieChart,
        centerTitle: String,
        chartData: HashMap<String, Float>
    ) {
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.setCenterTextTypeface((requireActivity().application as FasterMixerApplication).iransans)
        chart.centerText = createSpannableString(
            centerTitle,
            (requireActivity().application as FasterMixerApplication).iransans,
            Color.BLACK
        )

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)

        chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this)

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTypeface((requireActivity().application as FasterMixerApplication).iransansMedium)
        chart.setEntryLabelTextSize(12f)

        chart.setDrawRoundedSlices(false)
        setPieData(chart, chartData)
    }

    private fun setPieData(
        chart: PieChart,
        chartData: HashMap<String, Float>,
        helpLabel: String? = null,
        shouldAnimate: Boolean = false
    ) { //name , value
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        chartData.forEach { dat ->
            entries.add(
                PieEntry(dat.value, dat.key)
            )
        }


        val dataSet = PieDataSet(entries, helpLabel)
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        dataSet.valueTypeface = (requireActivity().application as FasterMixerApplication).iransans

        // add a lot of colors
        /*val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())*/
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueTypeface((requireActivity().application as FasterMixerApplication).iransans)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface((requireActivity().application as FasterMixerApplication).iransans)
        chart.data = data

        // undo all highlights
        chart.highlightValues(null)

        chart.invalidate()

        if (shouldAnimate)
            chart.animateXY(1400, 1400)

    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.i(
            "VAL SELECTED",
            "Value: " + e.y + ", index: " + h.x
                    + ", DataSet index: " + h.dataSetIndex
        )
    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }


}
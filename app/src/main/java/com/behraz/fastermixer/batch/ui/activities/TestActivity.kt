package com.behraz.fastermixer.batch.ui.activities


import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.FasterMixerApplication
import com.behraz.fastermixer.batch.databinding.ActivityTestBinding
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.ui.adapters.MixerAdapter
import com.behraz.fastermixer.batch.utils.general.createSpannableString
import com.behraz.fastermixer.batch.utils.general.subscribeSignalStrengthChangeListener
import com.behraz.fastermixer.batch.utils.general.toast
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*


class TestActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, MixerAdapter.BatchAdapterInteraction,
    OnChartValueSelectedListener {

    protected val parties = arrayOf(
        "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
        "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
        "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
        "Party Y", "Party Z"
    )

    private lateinit var mBinding: ActivityTestBinding

    private var counter = 0

    private var googleApiClient: GoogleApiClient? = null


    var batchLocation: CircleFence? = null
    private val mixerAdapter = MixerAdapter(false, this)

    private val batteryLevel: Float
        get() {
            val batteryIntent =
                registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

            // Error checking that probably isn't needed but I added just in case.
            return if (level == -1 || scale == -1) {
                50.0f
            } else level.toFloat() / scale.toFloat() * 100.0f
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test)


        val x = subscribeSignalStrengthChangeListener(true) {
            println("debug:signal: $it")
        }



        // initProgressChart()
        initViews()

    }

    private fun initViews() {
        initPieChart(
            "وضعیت ماشین آلات", hashMapOf(
                Pair("روشن", 30f),
                Pair("خاموش", 20f),
                Pair("خراب", 50f),
            )
        )
    }

    private fun initPieChart(centerTitle: String, chartData: HashMap<String, Float>) {
        val chart = mBinding.chartView
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.setCenterTextTypeface((application as FasterMixerApplication).iransans)
        chart.centerText = createSpannableString(
            centerTitle,
            (application as FasterMixerApplication).iransans,
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
        chart.setEntryLabelTypeface((application as FasterMixerApplication).iransansMedium)
        chart.setEntryLabelTextSize(12f)

        chart.setDrawRoundedSlices(false) //age bekhaym round beshe true mikonim
        setPieData(chartData)
    }

    private fun setPieData(chartData: HashMap<String, Float>) { //name , value
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        chartData.forEach { dat ->
            entries.add(
                PieEntry(dat.value, dat.key)
            )
        }


        val dataSet = PieDataSet(entries, "وضعیت ماشین آلات")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        dataSet.valueTypeface = (application as FasterMixerApplication).iransans

        // add a lot of colors
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface((application as FasterMixerApplication).iransans)
        mBinding.chartView.data = data

        // undo all highlights
        mBinding.chartView.highlightValues(null)
        mBinding.chartView.invalidate()
    }

    @Override
    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.i(
            "VAL SELECTED",
            "Value: " + e.y + ", index: " + h.x
                    + ", DataSet index: " + h.dataSetIndex
        )
    }

    @Override
    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }


    //enable GPS req by google Play--------------------------------------------

    private fun enableGPSReq() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build()
        }
        googleApiClient?.let {
            it.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 30 * 1000
            locationRequest.fastestInterval = 5 * 1000
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

            // **************************
            builder.setAlwaysShow(true) // this is the key ingredient
            // **************************

            val result: PendingResult<LocationSettingsResult> = LocationServices.SettingsApi
                .checkLocationSettings(it, builder.build())
            result.setResultCallback {
                val status: Status = it.status
                val state: LocationSettingsStates = it
                    .locationSettingsStates
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                        println("debug: Location settings are satisfied ")
                        // All location settings are satisfied. The client can
                        // initialize location
                        // requests here.
                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // Location settings are not satisfied. But could be
                        // fixed by showing the user
                        // a dialog.
                        try {
                            println("debug: Location settings are not satisfied, fixed by showing the user a dialog")
                            // Show the dialog by calling
                            // startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(this@TestActivity, 1000)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        println("debug: Location settings are not satisfied, no way to fix ")
                        // Location settings are not satisfied. However, we have
                        // no way to fix the
                        // settings so we won't show the dialog.
                    }
                }
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        println("debug: onConnected -> $p0")
    }

    override fun onConnectionSuspended(p0: Int) {
        println("debug: onConnectionSuspended -> $p0")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        println("debug: onConnectionFailed -> $p0")
    }

    override fun onCallClicked(mixer: Mixer) {
        toast("Not yet implemented")
    }

    override fun onEndLoadingClicked(mixer: Mixer) {
        toast("Not yet implemented")
    }

}
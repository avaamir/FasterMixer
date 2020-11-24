package com.behraz.fastermixer.batch.ui.dialogs

import android.app.Activity
import com.behraz.fastermixer.batch.R
import kotlinx.android.synthetic.main.dialog_admin_equipment.*
import kotlinx.android.synthetic.main.dialog_message.*


class AdminEquipmentDialog(
    activity: Activity,
    themeResId: Int,
    private val interactions: Interactions
) :
    MyBaseFullScreenDialog(
        activity, themeResId,
        R.layout.dialog_admin_equipment
    ) {


    override fun initViews() {
        frameShowOnMap.setOnClickListener {
            interactions.onShowOnMapClicked()
            dismiss()
        }
        frameRouteToCar.setOnClickListener {
            interactions.onRoutingToEquipmentClicked()
            dismiss()
        }
        frameDrawRoad.setOnClickListener {
            interactions.onDrawRoadReportClicked()
            dismiss()
        }
        frameWorkFullReport.setOnClickListener {
            interactions.onFullReportClicked()
            dismiss()
        }
        frameWorkSummeryReport.setOnClickListener {
            interactions.onSummeryReportClicked()
            dismiss()
        }
    }

    interface Interactions {
        fun onShowOnMapClicked()
        fun onRoutingToEquipmentClicked()
        fun onDrawRoadReportClicked()
        fun onSummeryReportClicked()
        fun onFullReportClicked()
    }
}
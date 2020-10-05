package com.behraz.fastermixer.batch.ui.osm

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Interpolator
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import com.behraz.fastermixer.batch.ui.fragments.MarkerAnimationUtil
import com.behraz.fastermixer.batch.utils.general.getBitmapFromVectorDrawable
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

open class ImageMarker(drawableId: Int, mapView: MapView, width: Int = 42, height: Int = 42) :
    Marker(mapView) {

    val markerAnimation = MarkerAnimationUtil()

    init {
        val markerBitmap =
            getBitmapFromVectorDrawable(
                mapView.context,
                drawableId,
                width = 42,
                height = 42
            )
        val dr: Drawable = BitmapDrawable(
            mapView.context.resources,
            Bitmap.createScaledBitmap(
                markerBitmap,
                (width * mapView.context.resources.displayMetrics.density).toInt(),
                (height * mapView.context.resources.displayMetrics.density).toInt(),
                true
            )
        )
        this.icon = dr
        this.setAnchor(ANCHOR_CENTER, ANCHOR_BOTTOM)
    }

    init {
        infoWindow = DriverInfoWindow(mapView)
    }

    override fun getInfoWindow(): DriverInfoWindow {
        return super.getInfoWindow() as DriverInfoWindow
    }

    fun setPelakText(
        firstPelakText: String,
        secondText: String,
        thirdText: String,
        forthText: String
    ) {
        infoWindow.setPelakText(firstPelakText, secondText, thirdText, forthText)
    }
}
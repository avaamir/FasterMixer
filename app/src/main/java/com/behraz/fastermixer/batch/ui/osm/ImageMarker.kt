package com.behraz.fastermixer.batch.ui.osm

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.behraz.fastermixer.batch.utils.general.getBitmapFromVectorDrawable
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

open class ImageMarker(drawableId: Int, mapView: MapView, width: Int = 42, height: Int = 42) :
    Marker(mapView) {
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
        this.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    }
}
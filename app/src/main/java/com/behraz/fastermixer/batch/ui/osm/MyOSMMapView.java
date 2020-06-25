package com.behraz.fastermixer.batch.ui.osm;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class MyOSMMapView extends MapView {
    private OnMapClickListener onMapClickListener;
    private Long touchTime;


    public MyOSMMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs);
    }

    public MyOSMMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs, boolean hardwareAccelerated) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs, hardwareAccelerated);
    }

    public MyOSMMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyOSMMapView(Context context) {
        super(context);
    }

    public MyOSMMapView(Context context, MapTileProviderBase aTileProvider) {
        super(context, aTileProvider);
    }

    public MyOSMMapView(Context context, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler) {
        super(context, aTileProvider, tileRequestCompleteHandler);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int actionType = ev.getAction();
        if (actionType == MotionEvent.ACTION_UP) {
            Projection projection = getProjection();
            IGeoPoint loc = projection.fromPixels((int) ev.getX(), (int) ev.getY());
            if (System.currentTimeMillis() - touchTime < 125) {
                if (onMapClickListener != null) {
                    onMapClickListener.onMapTapped(new GeoPoint(loc));
                }
            }
        } else if (actionType == MotionEvent.ACTION_DOWN) {
            touchTime = System.currentTimeMillis();
        }
        return super.dispatchTouchEvent(ev);
    }


    public void setOnMapClickListener(OnMapClickListener onMapClickListener) {
        this.onMapClickListener = onMapClickListener;
    }

    public interface OnMapClickListener {
        void onMapTapped(GeoPoint geoPoint);
    }
}

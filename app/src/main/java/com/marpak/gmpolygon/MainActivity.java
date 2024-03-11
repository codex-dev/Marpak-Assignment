package com.marpak.gmpolygon;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

// Implement OnMapReadyCallback.
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int POLYGON_FILL_COLOR = 0x800000ff;
    private static final int LINE_COLOR = 0xff0000ff;
    private static final int DIVIDER_COLOR = 0xffffff00;
    private static final int LINE_WIDTH_PX = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout file as the content view.
        setContentView(R.layout.activity_main);

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Polygon polygon = googleMap.addPolygon(new PolygonOptions().clickable(true)
                .add(
                        new LatLng(-31.673, 128.892),
                        new LatLng(-31.952, 115.857),
                        new LatLng(-17.785, 122.258),
                        new LatLng(-12.4258, 130.7932)));
        polygon.setTag("SeLecTioN");
        polygon.setStrokeWidth(LINE_WIDTH_PX);
        polygon.setStrokeColor(LINE_COLOR);
        polygon.setFillColor(POLYGON_FILL_COLOR);

        // cross line
        Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(new LatLng(-31.952, 115.857),
                        new LatLng(-12.4258, 130.7932)));
        polyline.setWidth(LINE_WIDTH_PX);
        polyline.setColor(LINE_COLOR);
    }
}
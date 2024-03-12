package com.marpak.gmpolygon;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.marpak.gmpolygon.databinding.ActivityMainBinding;
import com.marpak.gmpolygon.enums.ButtonAction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private GoogleMap.OnMapClickListener mapClickListener;
    private final List<LatLng> polylinePoints = new ArrayList<>();
    private Polyline polyline;
    private Polygon polygon;

    private ActivityMainBinding binding;
    private ButtonPalette buttonPalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout file as the content view.
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        buttonPalette = new ButtonPalette(binding.buttonLayout);
    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        mapClickListener = this::addPoint;

        binding.btnDrawPolygon.setOnClickListener(view -> {
            if (buttonPalette.getCurrentAction() == ButtonAction.CLEAR_DRAWINGS) {
                buttonPalette.setAction(ButtonAction.START_DRAWING_SHAPE);

                // start collecting click points
                polylinePoints.clear();
                googleMap.setOnMapClickListener(mapClickListener);

            } else if (buttonPalette.getCurrentAction() == ButtonAction.START_DRAWING_SHAPE) {
                // stop collecting click points and draw polygon
                if(polylinePoints.size() >=3) {
                    drawPolygon();

                    buttonPalette.setAction(ButtonAction.STOP_DRAWING_SHAPE);

                    polylinePoints.clear();
                    googleMap.setOnMapClickListener(null);
                }
            }
        });

        binding.btnDrawCrossLine.setOnClickListener(view -> {
            if (buttonPalette.getCurrentAction() == ButtonAction.STOP_DRAWING_SHAPE) {
                buttonPalette.setAction(ButtonAction.START_DRAWING_CROSS_LINE);

                // start collecting click points
                polylinePoints.clear();
                googleMap.setOnMapClickListener(mapClickListener);

            } else if (buttonPalette.getCurrentAction() == ButtonAction.START_DRAWING_CROSS_LINE) {
                // stop collecting click points and draw cross line
                if(polylinePoints.size() >=2) {
                    buttonPalette.setAction(ButtonAction.STOP_DRAWING_CROSS_LINE);

                    polylinePoints.clear();
                    googleMap.setOnMapClickListener(null);
                }
            }
        });

        binding.btnDivideCrossLine.setOnClickListener(view -> {
            if (buttonPalette.getCurrentAction() == ButtonAction.STOP_DRAWING_CROSS_LINE) {
                // get user input via an prompt
                InputPromptDialog.showInputDialog(MainActivity.this,
                        getResources().getString(R.string.prompt_dialog_title),
                        getResources().getString(R.string.prompt_dialog_description),
                        userInput -> {
                    // divide the cross line
                    buttonPalette.setAction(ButtonAction.DIVIDE_CROSS_LINE);
                }, () -> {

                });
            }
        });

        binding.btnClearDrawings.setOnClickListener(view -> {
            polygon.remove();
            polyline.remove();

            buttonPalette.setAction(ButtonAction.CLEAR_DRAWINGS);
        });
    }

    private void addPoint(LatLng latLng) {
        polylinePoints.add(latLng);
        if (polyline != null) {
            // remove existing polyline from map and add new polyline with updated points
            polyline.remove();
        }
        polyline = googleMap.addPolyline(new PolylineOptions().addAll(polylinePoints).color(getResources().getColor(R.color.polyline_color)));
    }

    private void drawPolygon() {
        if (polygon != null) {
            // remove existing polygon from map and add new polygon with updated points
            polygon.remove();
        }
        polygon = googleMap.addPolygon(new PolygonOptions().addAll(polylinePoints).strokeColor(getResources().getColor(R.color.polyline_color)).fillColor(getResources().getColor(R.color.polygon_fill_color)));
        polygon.setTag("User Selection");
    }
}
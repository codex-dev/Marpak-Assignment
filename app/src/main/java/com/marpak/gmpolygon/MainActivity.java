package com.marpak.gmpolygon;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.marpak.gmpolygon.databinding.ActivityMainBinding;
import com.marpak.gmpolygon.enums.ButtonAction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap googleMap;
    private GoogleMap.OnMapClickListener mapClickListener;
    private final List<LatLng> polygonPoints = new ArrayList<>();
    private final List<LatLng> crossLinePoints = new ArrayList<>();
    private Polyline polyline;
    private Polygon polygon;
    private PolygonOptions polygonOptions;

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
        googleMap.setOnMarkerDragListener(this);
        mapClickListener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (buttonPalette.getCurrentAction() == ButtonAction.START_DRAWING_SHAPE) {
                    addPoint(latLng, polygonPoints);
                } else if (buttonPalette.getCurrentAction() == ButtonAction.START_DRAWING_CROSS_LINE) {
                    addPoint(latLng, crossLinePoints);
                }
            }
        };

        binding.btnDrawPolygon.setOnClickListener(view -> {
            if (buttonPalette.getCurrentAction() == ButtonAction.CLEAR_DRAWINGS) {
                buttonPalette.setAction(ButtonAction.START_DRAWING_SHAPE);

                // start collecting click points
                googleMap.setOnMapClickListener(mapClickListener);

            } else if (buttonPalette.getCurrentAction() == ButtonAction.START_DRAWING_SHAPE) {
                // stop collecting click points and draw polygon
                if (polygonPoints.size() >= 3) {
                    drawPolygon();

                    buttonPalette.setAction(ButtonAction.STOP_DRAWING_SHAPE);

                    googleMap.setOnMapClickListener(null);
                }
            }
        });

        binding.btnDrawCrossLine.setOnClickListener(view -> {
            if (buttonPalette.getCurrentAction() == ButtonAction.STOP_DRAWING_SHAPE) {
                buttonPalette.setAction(ButtonAction.START_DRAWING_CROSS_LINE);

                // start collecting click points
                googleMap.setOnMapClickListener(mapClickListener);

            } else if (buttonPalette.getCurrentAction() == ButtonAction.START_DRAWING_CROSS_LINE) {
                // stop collecting click points and draw cross line
                if (polygonPoints.size() >= 2) {
                    buttonPalette.setAction(ButtonAction.STOP_DRAWING_CROSS_LINE);

                    googleMap.setOnMapClickListener(null);
                }
            }
        });

        binding.btnDivideCrossLine.setOnClickListener(view -> {
            // get user input via an prompt
            InputPromptDialog.showInputDialog(MainActivity.this,
                    getResources().getString(R.string.prompt_dialog_title),
                    getResources().getString(R.string.prompt_dialog_description),
                    userInput -> {
                        // divide the cross line
                        buttonPalette.setAction(ButtonAction.DIVIDE_CROSS_LINE);
                    }, () -> {

                    });
        });

        binding.btnResizePolygon.setOnClickListener(view -> {
            if (buttonPalette.getCurrentAction() == ButtonAction.DIVIDE_CROSS_LINE) {
                // TODO show markers on the corners of the polygon, let user drag those and resize it
                for (int index = 0; index < polygonPoints.size(); index++) {
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(polygonPoints.get(index))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .draggable(true));
                    if (marker != null) marker.setTitle(String.valueOf(index));
                }

                buttonPalette.setAction(ButtonAction.START_RESIZING_SHAPE);
            } else if (buttonPalette.getCurrentAction() == ButtonAction.START_RESIZING_SHAPE) {
                // hide markers on the map
                googleMap.clear();
                googleMap.addPolygon(polygonOptions);
                buttonPalette.setAction(ButtonAction.STOP_RESIZING_SHAPE);
            }
        });

        binding.btnClearDrawings.setOnClickListener(view -> {
            googleMap.clear();

            polygonPoints.clear();
            crossLinePoints.clear();

            buttonPalette.setAction(ButtonAction.CLEAR_DRAWINGS);
        });
    }

    private void addPoint(LatLng latLng, List<LatLng> pointsList) {
        pointsList.add(latLng);
        if (polyline != null) {
            // remove existing polyline from map and add new polyline with updated points
            polyline.remove();
        }
        polyline = googleMap.addPolyline(new PolylineOptions().addAll(pointsList).color(getResources().getColor(R.color.polyline_color)));
    }

    private void drawPolygon() {
        if (polygon != null) {
            // remove existing polygon from map and add new polygon with updated points
            polygon.remove();
        }
        polygonOptions = new PolygonOptions().addAll(polygonPoints).strokeColor(getResources()
                .getColor(R.color.polyline_color)).fillColor(getResources().getColor(R.color.polygon_fill_color));
        polygon = googleMap.addPolygon(polygonOptions);
        polygon.setTag("User Selection");
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        polygonPoints.set(Integer.parseInt(marker.getTitle()), marker.getPosition());
        drawPolygon();
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }
}
package com.marpak.gmpolygon

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.marpak.gmpolygon.InputPromptDialog.showInputDialog
import com.marpak.gmpolygon.databinding.ActivityMainBinding
import com.marpak.gmpolygon.enums.ButtonAction
import com.marpak.gmpolygon.listeners.BtnClickListener

class MainActivity : BaseActivity(), OnMapReadyCallback, OnMarkerDragListener, OnMapClickListener,
    BtnClickListener {
    private var googleMap: GoogleMap? = null
    private var mapClickListener: OnMapClickListener? = null

    private val polygonPoints: MutableList<LatLng> = ArrayList()
    private val crossLinePoints: MutableList<LatLng> = ArrayList()

    private var lastMarker: Marker? = null

    private var polyline: Polyline? = null
    private var polygon: Polygon? = null
    private var polygonOptions: PolygonOptions? = null

    private var binding: ActivityMainBinding? = null
    private var buttonPalette: ButtonPalette? = null

    private var btnListener: BtnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout file as the content view.
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding!!.getRoot()
        setContentView(view)

        // Get a handle to the fragment and register the callback.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        btnListener = this

        buttonPalette = ButtonPalette(binding!!.buttonLayout, btnListener)
        setCustomTitle(text = "Map Activity")
    }

    // Get a handle to the GoogleMap object and display marker.
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap!!.setOnMarkerDragListener(this)

        mapClickListener = this
    }

    private fun addPoint(latLng: LatLng, pointsList: MutableList<LatLng>) {
        pointsList.add(latLng)
        if (polyline != null) {
            // remove existing polyline from map and add new polyline with updated points
            polyline!!.remove()
        }
        if (lastMarker == null) { // move a marker to last clicked point in the map
            lastMarker = googleMap!!.addMarker(
                MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
        } else {
            lastMarker!!.position = latLng
        }
        polyline = googleMap!!.addPolyline(
            PolylineOptions().addAll(pointsList)
                .color(getResources().getColor(R.color.polyline_color, null))
        )
    }

    private fun drawPolygon() {
        if (polygon != null) {
            // remove existing polygon from map and add new polygon with updated points
            polygon!!.remove()
        }
        polygonOptions = PolygonOptions().addAll(polygonPoints).strokeColor(
            getResources()
                .getColor(R.color.polyline_color, null)
        ).fillColor(getResources().getColor(R.color.polygon_fill_color, null))
        polygon = googleMap!!.addPolygon(polygonOptions!!)
        polygon!!.tag = "User Selection"
    }

    override fun onMarkerDrag(marker: Marker) {}
    override fun onMarkerDragEnd(marker: Marker) {
        polygonPoints[marker.title!!.toInt()] = marker.position
        drawPolygon()
    }

    override fun onMarkerDragStart(marker: Marker) {}
    override fun onMapClick(latLng: LatLng) {
        if (buttonPalette!!.currentAction == ButtonAction.START_DRAWING_SHAPE) {
            addPoint(latLng, polygonPoints)
        } else if (buttonPalette!!.currentAction == ButtonAction.START_DRAWING_CROSS_LINE) {
            addPoint(latLng, crossLinePoints)
        }
    }

    override fun onClicked(btn: Button) {
        when (btn) {
            binding!!.btnDrawPolygon -> {
                if (buttonPalette!!.currentAction == ButtonAction.CLEAR_DRAWINGS) {
                    buttonPalette!!.setAction(ButtonAction.START_DRAWING_SHAPE)

                    // start collecting click points
                    googleMap!!.setOnMapClickListener(mapClickListener)
                } else if (buttonPalette!!.currentAction == ButtonAction.START_DRAWING_SHAPE) {
                    // stop collecting click points and draw polygon
                    if (polygonPoints.size >= 3) {
                        drawPolygon()
                        buttonPalette!!.setAction(ButtonAction.STOP_DRAWING_SHAPE)
                        googleMap!!.setOnMapClickListener(null)
                    }
                }
            }

            binding!!.btnDrawCrossLine -> {
                if (buttonPalette!!.currentAction == ButtonAction.STOP_DRAWING_SHAPE) {
                    buttonPalette!!.setAction(ButtonAction.START_DRAWING_CROSS_LINE)

                    // start collecting click points
                    googleMap!!.setOnMapClickListener(mapClickListener)
                } else if (buttonPalette!!.currentAction == ButtonAction.START_DRAWING_CROSS_LINE) {
                    // stop collecting click points and draw cross line
                    if (polygonPoints.size >= 2) {
                        buttonPalette!!.setAction(ButtonAction.STOP_DRAWING_CROSS_LINE)
                        lastMarker!!.remove()
                        googleMap!!.setOnMapClickListener(null)
                    }
                }
            }

            binding!!.btnDivideCrossLine -> {
                // get user input via an prompt
                showInputDialog(this@MainActivity,
                    getResources().getString(R.string.prompt_dialog_title),
                    getResources().getString(R.string.prompt_dialog_description),
                    {
                        // divide the cross line
                        buttonPalette!!.setAction(ButtonAction.DIVIDE_CROSS_LINE)
                    }, InputPromptDialog.OnCancelListener {})
            }

            binding!!.btnResizePolygon -> {
                if (buttonPalette!!.currentAction == ButtonAction.DIVIDE_CROSS_LINE) {
                    // show markers on the corners of the polygon, let user drag those and resize it
                    for (index in polygonPoints.indices) {
                        val marker = googleMap!!.addMarker(
                            MarkerOptions()
                                .position(polygonPoints[index])
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .draggable(true)
                        )
                        if (marker != null) marker.title = index.toString()
                    }
                    buttonPalette!!.setAction(ButtonAction.START_RESIZING_SHAPE)
                } else if (buttonPalette!!.currentAction == ButtonAction.START_RESIZING_SHAPE) {
                    // hide markers on the map
                    googleMap!!.clear()
                    googleMap!!.addPolygon(polygonOptions!!)
                    buttonPalette!!.setAction(ButtonAction.STOP_RESIZING_SHAPE)
                }
            }

            binding!!.btnClearDrawings -> {
                lastMarker = null
                googleMap!!.clear()
                polygonPoints.clear()
                crossLinePoints.clear()
                buttonPalette!!.setAction(ButtonAction.CLEAR_DRAWINGS)
            }
        }
    }
}
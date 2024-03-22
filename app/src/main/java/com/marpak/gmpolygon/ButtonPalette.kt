package com.marpak.gmpolygon

import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.marpak.gmpolygon.enums.ButtonAction
import com.marpak.gmpolygon.listeners.BtnClickListener

class ButtonPalette(buttonLayout: LinearLayout, private var btnListener: BtnClickListener?) {
    private val btnClearDrawings: Button
    private val btnDrawPolygon: Button
    private val btnDrawCrossLine: Button
    private val btnDivideCrossLine: Button
    private val btnResizePolygon: Button

    var currentAction: ButtonAction
        private set

    init {
        btnClearDrawings = buttonLayout.findViewById(R.id.btnClearDrawings)
        btnDrawPolygon = buttonLayout.findViewById(R.id.btnDrawPolygon)
        btnDrawCrossLine = buttonLayout.findViewById(R.id.btnDrawCrossLine)
        btnDivideCrossLine = buttonLayout.findViewById(R.id.btnDivideCrossLine)
        btnResizePolygon = buttonLayout.findViewById(R.id.btnResizePolygon)
        currentAction = ButtonAction.CLEAR_DRAWINGS

        updateButtonLayout()
        setEventListeners()
    }

    fun setAction(action: ButtonAction) {
        currentAction = action
        updateButtonLayout()
    }

    private fun setEventListeners() {
        btnDrawPolygon.setOnClickListener{
            btnListener?.onClicked(btnDrawPolygon)
        }
        btnDrawCrossLine.setOnClickListener{
            btnListener?.onClicked(btnDrawCrossLine)
        }
        btnDivideCrossLine.setOnClickListener{
            btnListener?.onClicked(btnDivideCrossLine)
        }
        btnResizePolygon.setOnClickListener{
            btnListener?.onClicked(btnResizePolygon)
        }
        btnClearDrawings.setOnClickListener{
            btnListener?.onClicked(btnClearDrawings)
        }
    }

    private fun updateButtonLayout() {
        when (currentAction) {
            ButtonAction.CLEAR_DRAWINGS -> {
                btnDrawPolygon.visibility = View.VISIBLE
                btnDrawCrossLine.visibility = View.GONE
                btnDivideCrossLine.visibility = View.GONE
                btnResizePolygon.visibility = View.GONE
                btnClearDrawings.visibility = View.GONE

                btnDrawPolygon.setText(R.string.start_drawing_polygon)
            }

            ButtonAction.START_DRAWING_SHAPE -> {
                btnDrawPolygon.visibility = View.VISIBLE
                btnDrawCrossLine.visibility = View.GONE
                btnDivideCrossLine.visibility = View.GONE
                btnResizePolygon.visibility = View.GONE
                btnClearDrawings.visibility = View.VISIBLE

                btnDrawPolygon.setText(R.string.stop_drawing_polygon)
            }

            ButtonAction.STOP_DRAWING_SHAPE -> {
                btnDrawPolygon.visibility = View.GONE
                btnDrawCrossLine.visibility = View.VISIBLE
                btnDivideCrossLine.visibility = View.GONE
                btnResizePolygon.visibility = View.GONE
                btnClearDrawings.visibility = View.VISIBLE

                btnDrawCrossLine.setText(R.string.start_drawing_cross_line)
            }

            ButtonAction.START_DRAWING_CROSS_LINE -> {
                btnDrawPolygon.visibility = View.GONE
                btnDrawCrossLine.visibility = View.VISIBLE
                btnDivideCrossLine.visibility = View.GONE
                btnResizePolygon.visibility = View.GONE
                btnClearDrawings.visibility = View.VISIBLE

                btnDrawCrossLine.setText(R.string.stop_drawing_cross_line)
            }

            ButtonAction.STOP_DRAWING_CROSS_LINE -> {
                btnDrawPolygon.visibility = View.GONE
                btnDrawCrossLine.visibility = View.GONE
                btnDivideCrossLine.visibility = View.VISIBLE
                btnResizePolygon.visibility = View.GONE
                btnClearDrawings.visibility = View.VISIBLE
            }

            ButtonAction.DIVIDE_CROSS_LINE -> {
                btnDrawPolygon.visibility = View.GONE
                btnDrawCrossLine.visibility = View.GONE
                btnDivideCrossLine.visibility = View.GONE
                btnResizePolygon.visibility = View.VISIBLE
                btnClearDrawings.visibility = View.VISIBLE

                btnResizePolygon.setText(R.string.start_resizing_polygon)
            }

            ButtonAction.START_RESIZING_SHAPE -> {
                btnDrawPolygon.visibility = View.GONE
                btnDrawCrossLine.visibility = View.GONE
                btnDivideCrossLine.visibility = View.GONE
                btnResizePolygon.visibility = View.VISIBLE
                btnClearDrawings.visibility = View.VISIBLE

                btnResizePolygon.setText(R.string.stop_resizing_polygon)
            }

            ButtonAction.STOP_RESIZING_SHAPE -> {
                btnDrawPolygon.visibility = View.GONE
                btnDrawCrossLine.visibility = View.GONE
                btnDivideCrossLine.visibility = View.GONE
                btnResizePolygon.visibility = View.GONE
                btnClearDrawings.visibility = View.VISIBLE
            }
        }
    }
}

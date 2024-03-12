package com.marpak.gmpolygon;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.marpak.gmpolygon.R;
import com.marpak.gmpolygon.enums.ButtonAction;

public class ButtonPalette {
    private final Button btnClearDrawings;
    private final Button btnDrawPolygon;
    private final Button btnDrawCrossLine;
    private final Button btnDivideCrossLine;
    private final Button btnResizePolygon;

    private ButtonAction currentAction;

    public ButtonPalette(LinearLayout buttonLayout) {
        btnClearDrawings = buttonLayout.findViewById(R.id.btnClearDrawings);
        btnDrawPolygon = buttonLayout.findViewById(R.id.btnDrawPolygon);
        btnDrawCrossLine = buttonLayout.findViewById(R.id.btnDrawCrossLine);
        btnDivideCrossLine = buttonLayout.findViewById(R.id.btnDivideCrossLine);
        btnResizePolygon = buttonLayout.findViewById(R.id.btnResizePolygon);

        currentAction = ButtonAction.CLEAR_DRAWINGS;
        updateButtonLayout();
    }

    public ButtonAction getCurrentAction() {
        return currentAction;
    }

    public void setAction(ButtonAction action) {
        this.currentAction = action;

        updateButtonLayout();
    }

    private void updateButtonLayout() {
        switch (currentAction) {
            case CLEAR_DRAWINGS:
                btnDrawPolygon.setVisibility(View.VISIBLE);
                btnDrawCrossLine.setVisibility(View.GONE);
                btnDivideCrossLine.setVisibility(View.GONE);
                btnResizePolygon.setVisibility(View.GONE);
                btnClearDrawings.setVisibility(View.GONE);

                btnDrawPolygon.setText(R.string.start_drawing_polygon);
                break;

            case START_DRAWING_SHAPE:
                btnDrawPolygon.setVisibility(View.VISIBLE);
                btnDrawCrossLine.setVisibility(View.GONE);
                btnDivideCrossLine.setVisibility(View.GONE);
                btnResizePolygon.setVisibility(View.GONE);
                btnClearDrawings.setVisibility(View.VISIBLE);

                btnDrawPolygon.setText(R.string.stop_drawing_polygon);
                break;

            case STOP_DRAWING_SHAPE:
                btnDrawPolygon.setVisibility(View.GONE);
                btnDrawCrossLine.setVisibility(View.VISIBLE);
                btnDivideCrossLine.setVisibility(View.GONE);
                btnResizePolygon.setVisibility(View.GONE);
                btnClearDrawings.setVisibility(View.VISIBLE);

                btnDrawCrossLine.setText(R.string.start_drawing_cross_line);
                break;

            case START_DRAWING_CROSS_LINE:
                btnDrawPolygon.setVisibility(View.GONE);
                btnDrawCrossLine.setVisibility(View.VISIBLE);
                btnDivideCrossLine.setVisibility(View.GONE);
                btnResizePolygon.setVisibility(View.GONE);
                btnClearDrawings.setVisibility(View.VISIBLE);

                btnDrawCrossLine.setText(R.string.stop_drawing_cross_line);
                break;

            case STOP_DRAWING_CROSS_LINE:
                btnDrawPolygon.setVisibility(View.GONE);
                btnDrawCrossLine.setVisibility(View.GONE);
                btnDivideCrossLine.setVisibility(View.VISIBLE);
                btnResizePolygon.setVisibility(View.GONE);
                btnClearDrawings.setVisibility(View.VISIBLE);

                break;

            case DIVIDE_CROSS_LINE:
                btnDrawPolygon.setVisibility(View.GONE);
                btnDrawCrossLine.setVisibility(View.GONE);
                btnDivideCrossLine.setVisibility(View.GONE);
                btnResizePolygon.setVisibility(View.VISIBLE);
                btnClearDrawings.setVisibility(View.VISIBLE);

                btnResizePolygon.setText(R.string.start_resizing_polygon);

                break;

            case START_RESIZING_SHAPE:
                btnDrawPolygon.setVisibility(View.GONE);
                btnDrawCrossLine.setVisibility(View.GONE);
                btnDivideCrossLine.setVisibility(View.GONE);
                btnResizePolygon.setVisibility(View.VISIBLE);
                btnClearDrawings.setVisibility(View.VISIBLE);

                btnResizePolygon.setText(R.string.stop_resizing_polygon);

                break;

            case STOP_RESIZING_SHAPE:
                btnDrawPolygon.setVisibility(View.GONE);
                btnDrawCrossLine.setVisibility(View.GONE);
                btnDivideCrossLine.setVisibility(View.GONE);
                btnResizePolygon.setVisibility(View.GONE);
                btnClearDrawings.setVisibility(View.VISIBLE);
        }
    }
}

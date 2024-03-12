package com.marpak.gmpolygon;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

public class InputPromptDialog {

    public static void showInputDialog(final Context context, String title, String message,
                                       final OnInputListener inputListener, final OnCancelListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

        builder.setView(input);

        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            String userInput = input.getText().toString();
            if (inputListener != null) {
                inputListener.onInput(userInput);
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
            dialog.cancel();
        });

        builder.setOnCancelListener(dialog -> {
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
        });

        builder.show();
    }

    public interface OnInputListener {
        void onInput(String userInput);
    }

    public interface OnCancelListener {
        void onCancel();
    }
}
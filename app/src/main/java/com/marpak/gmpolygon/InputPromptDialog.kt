package com.marpak.gmpolygon

import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

object InputPromptDialog {
    @JvmStatic
    fun showInputDialog(
        context: Context?, title: String?, message: String?,
        inputListener: OnInputListener?, cancelListener: OnCancelListener?
    ) {
        val builder = AlertDialog.Builder(context!!)

        builder.setTitle(title)
        builder.setMessage(message)

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED

        builder.setView(input)
        builder.setPositiveButton(R.string.ok) { _: DialogInterface?, _: Int ->
            val userInput = input.text.toString()
            inputListener?.onInput(userInput)
        }
        builder.setNegativeButton(R.string.cancel) { dialog: DialogInterface, _: Int ->
            cancelListener?.onCancel()
            dialog.cancel()
        }
        builder.setOnCancelListener { cancelListener?.onCancel() }
        builder.show()
    }

    fun interface OnInputListener {
        fun onInput(userInput: String?)
    }

    fun interface OnCancelListener {
        fun onCancel()
    }
}
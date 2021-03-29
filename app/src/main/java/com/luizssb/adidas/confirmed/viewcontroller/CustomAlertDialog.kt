package com.luizssb.adidas.confirmed.viewcontroller

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding

class CustomAlertDialog<TViewBinding : ViewBinding>(
        context: Context,
        contentFactory: (LayoutInflater) -> TViewBinding
) : DialogInterface {
    val content: TViewBinding

    private val proxy: AlertDialog

    init {
        val layoutInflater = LayoutInflater.from(context)
        content = contentFactory(layoutInflater)
        proxy = AlertDialog.Builder(context)
                        .setView(content.root)
                        .create()
    }

    fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) = proxy.setOnCancelListener(listener)

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) = proxy.setOnDismissListener(listener)

    override fun cancel() = proxy.cancel()

    override fun dismiss() = proxy.dismiss()

    fun show() = proxy.show()

    var isShowing: Boolean
        get() = proxy.isShowing
        set(value) {
            if (value) {
                show()
            } else {
                dismiss()
            }
        }
}

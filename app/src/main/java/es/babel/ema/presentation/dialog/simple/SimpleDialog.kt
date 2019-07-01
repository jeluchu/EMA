package es.babel.ema.presentation.dialog.simple

import android.view.View
import com.carmabs.ema.R
import es.babel.ema.presentation.dialog.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_simple.view.*


/**
 * Simple dialog
 *
 * <p>
 * Copyright (C) 2018Babel Sistemas de Información. All rights reserved.
 * </p>
 *
 * @author <a href="mailto:carlos.mateo@babel.es">Carlos Mateo Benito</a>
 */
class SimpleDialog : BaseDialog() {

    override fun getLayout(): Int {
        return R.layout.dialog_simple
    }

    override fun setupUI(view: View) {
        (data as? SimpleDialogData)?.let {
            with(it){
                view.bDialogSimpleYes.setOnClickListener {
                    dialogListener?.onConfirmClicked()
                }

                (dialogListener as? SimpleDialogListener)?.let { listener ->
                    view.bDialogSimpleNo.setOnClickListener { listener.onCancelClicked() }
                    view.ivDialogSimpleCross.setOnClickListener { listener.onCancelClicked() }
                }

                view.tvDialogSimpleTitle!!.text = title

                if (showCross)
                    view.ivDialogSimpleCross.visibility = if (showCross) View.VISIBLE else View.GONE

                view.tvDialogSimpleMessage!!.text = message

                view.bDialogSimpleYes.text = accept

                view.ivDialogSimple.visibility =
                        image?.let {
                            view.ivDialogSimple.setImageDrawable(it)
                            View.VISIBLE
                        } ?: View.GONE

                if (cancel.isEmpty()) {
                    view.bDialogSimpleNo.visibility = View.GONE
                }

                view.bDialogSimpleNo.text = cancel

                isCancelable = true
            }
        }
    }
}
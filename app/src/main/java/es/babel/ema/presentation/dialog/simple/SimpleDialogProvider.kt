package es.babel.ema.presentation.dialog.simple

import androidx.fragment.app.FragmentManager
import es.babel.ema.presentation.dialog.base.BaseDialog
import es.babel.ema.presentation.dialog.base.BaseDialogProvider

/**
 * Simple dialog implementation
 *
 * <p>
 * Copyright (C) 2018Babel Sistemas de Información. All rights reserved.
 * </p>
 *
 * @author <a href="mailto:carlos.mateo@babel.es">Carlos Mateo Benito</a>
 */

class SimpleDialogProvider constructor(fragmentManager: FragmentManager) : BaseDialogProvider(fragmentManager) {
    override fun generateDialog(): BaseDialog = SimpleDialog()
}
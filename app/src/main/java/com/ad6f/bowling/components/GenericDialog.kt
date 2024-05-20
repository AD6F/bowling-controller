package com.ad6f.bowling.components

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable

/**
 * Generic dialog to have a common base with all the Jetpack Compose dialog.
 */
@Composable
fun GenericDialog(
    isVisible: Boolean,
    title: (@Composable () -> Unit)? = null,
    closeAction: (() -> Unit)? = null,
    cancelButton: (@Composable () -> Unit)? = null,
    okButton: (@Composable () -> Unit)? = null,
    dialogBody: @Composable () -> Unit
) {
    if(isVisible) {
        AlertDialog(
            onDismissRequest = { closeAction?.invoke() },
            confirmButton = {
                okButton?.invoke()
            },
            dismissButton = {
                cancelButton?.invoke()
            },
            title = {
                title?.invoke()
            },
            text = {
                dialogBody()
            }
        )
    }
}

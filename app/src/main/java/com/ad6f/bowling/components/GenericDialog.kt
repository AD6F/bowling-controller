package com.ad6f.bowling.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * Dialog(Popup) to add a player for the game.
 */
@Composable
fun GenericDialog(
    isVisible: Boolean,
    title: String,
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
            title = { Text(title) },
            text = {
                dialogBody()
            }
        )
    }
}

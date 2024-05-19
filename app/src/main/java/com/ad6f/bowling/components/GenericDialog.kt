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
    title: @Composable () -> Unit,
    closeAction: () -> Unit,
    cancelButton: @Composable () -> Unit,
    okButton: @Composable () -> Unit,
    dialogBody: @Composable () -> Unit
) {
    if(isVisible) {
        AlertDialog(
            onDismissRequest = { closeAction() },
            confirmButton = {
                okButton()
            },
            dismissButton = {
                cancelButton()
            },
            title = { title() },
            text = {
                dialogBody()
            }
        )
    }
}

package com.ad6f.bowling.components.gameloop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.ad6f.bowling.components.GenericDialog

@Composable
fun BowlingLoadingDialog(
    isVisible: Boolean,
    message: String,
    imageId: Int,
    imageAlt: String
) {
    GenericDialog(
        isVisible,
        title = {Text("Bowling")}
    ) {
        Column {
            Text(message)
            Image(painter = painterResource(imageId), contentDescription = imageAlt)
        }
    }
}
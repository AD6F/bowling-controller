package com.ad6f.bowling.components.gameloop

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ad6f.bowling.ui.theme.DarkColorScheme
import com.ad6f.bowling.ui.theme.LightColorScheme

@Composable
fun LaunchInstruction(isLaunchTime: Boolean) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(if (isSystemInDarkTheme()) DarkColorScheme.primary else LightColorScheme.primary)
    ) {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(if(isLaunchTime) "Launch your phone." else "Press the button below.", fontSize = 20.sp, color = if(isSystemInDarkTheme()) DarkColorScheme.onPrimary else LightColorScheme.onPrimary)
        }
    }
}
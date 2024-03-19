package com.example.nytviewer.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Customizable navigation bar used throughout the app
 * @param title: Title to be displayed in the navigation bar
 * @param containerColor: background color of the navigation bar
 * @param contentColor: content color of the navigation bar
 * @param backAction: Callback to be invoked when back button is pressed
 * @param rightBarButtonIcon: Icon to be displayed in the right action item
 * @param rightBarButtonAction: Callback to be invoked when action button is pressed
 * @param content: Content to be displayed in the screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    title: String = "",
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    backAction: (() -> Unit)? = null,
    backIcon: ImageVector = Icons.Default.ArrowBack,
    rightBarButtonIcon: Painter? = null,
    rightBarButtonAction: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        Modifier
            .fillMaxSize()
            .safeContentPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(Modifier.fillMaxSize()) {
            Surface(modifier = Modifier.background(containerColor)) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = title , color = contentColor , style = MaterialTheme.typography.titleSmall)
                    },
                    navigationIcon = {
                        if (backAction != null) {
                            IconButton(onClick = backAction) {
                                Icon(
                                    backIcon,
                                    tint = contentColor,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = containerColor
                    ),
                    actions = {
                        if  (rightBarButtonAction != null && rightBarButtonIcon != null){
                            IconButton(onClick = rightBarButtonAction) {
                                Icon(
                                    painter = rightBarButtonIcon,
                                    tint =contentColor,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    }
                )
            }
            content()
        }
    }
}
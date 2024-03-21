package com.example.nytviewer.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A simple Composable view that will display an error along with a "Retry" icon.
 * When the view is tapped on, it will call [onRetry].
 */
@Composable
fun ErrorView(error: String, onRetry: () -> Unit) = with(MaterialTheme) {
    Row(
        modifier = Modifier.clickable { onRetry() },
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = error,
            style = typography.bodyMedium,
            color = colorScheme.error
        )
        Icon(
            Icons.Outlined.Refresh,
            tint = colorScheme.error,
            contentDescription = "Retry"
        )
    }
}
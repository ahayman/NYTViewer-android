package com.example.nytviewer.ui.articleDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.nytviewer.ui.articleDetail.models.ArticleDetailActions
import com.example.nytviewer.ui.articleDetail.models.ArticleDetailData
import com.example.nytviewer.ui.common.ErrorView
import com.example.nytviewer.ui.common.NavBar

/**
 * View used to display an article's detail data.
 */
@Composable
fun ArticleDetailView(viewModel : ArticleDetailVM = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    NavBar(
        backAction = { viewModel.submit(ArticleDetailActions.BackPress) },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            error?.let {
                ErrorView(error = it) {
                    viewModel.submit(ArticleDetailActions.ReloadArticle)
                }
            }
            state.data?.let {
                ArticleView(data = it) {
                    viewModel.submit(ArticleDetailActions.VisitArticle(it.url, context))
                }
            }
        }
    }
}

/**
 * Primary Layout for the Article.
 */
@Composable
private fun ArticleView(data: ArticleDetailData, onReadMore: () -> Unit) = with(MaterialTheme) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = data.title,
            style = typography.headlineSmall,
            color = colorScheme.onSurface
        )
        Text(
            text = data.dateDisplay,
            style = typography.bodyMedium,
            color = colorScheme.onSurfaceVariant
        )
        data.byline.takeIf { it.isNotBlank() }?.let {
            Text(
                text = it,
                style = typography.bodyMedium,
                color = colorScheme.onSurface
            )
        }
        data.imageUrl?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = it,
                    contentDescription = data.title,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .width(200.dp)
                )
            }
        }
        Text(
            text = data.abstract,
            style = typography.bodyMedium,
            color = colorScheme.onSurface
        )
        Row(
            modifier = Modifier.clickable { onReadMore() },
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "READ MORE...",
                style = typography.bodySmall,
                color = colorScheme.primary
            )
            Icon(
                Icons.AutoMirrored.Outlined.ExitToApp,
                tint = colorScheme.primary,
                contentDescription = "Visit Article"
            )
        }
    }
}
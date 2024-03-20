package com.example.nytviewer.ui.articleList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.nytviewer.ui.articleList.models.ArticleVMActions
import com.example.nytviewer.ui.articleList.models.BriefDisplayData

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleListView(viewModel: ArticleListVM = hiltViewModel()) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state by viewModel.state.collectAsState()
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = { viewModel.submit(
        ArticleVMActions.RefreshArticles) })

    Box(
        modifier = Modifier
            .padding(10.dp)
            .pullRefresh(pullRefreshState),
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(state.articles) {
                BriefView(
                    brief = it,
                    onSelect = {
                        viewModel.submit(ArticleVMActions.SelectBrief(it))
                    }
                )
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun BriefView(brief: BriefDisplayData, onSelect: () -> Unit) = with(MaterialTheme) {
    Surface(
        modifier = Modifier
            .clickable { onSelect() }
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(16.dp)),
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = brief.title,
                    style = typography.labelMedium,
                    color = colorScheme.onSurface
                )
                Text(
                    text = brief.dateDisplay,
                    style = typography.bodySmall,
                    color = colorScheme.onSurfaceVariant
                )
                Text(
                    text = brief.byline,
                    style = typography.bodySmall,
                    color = colorScheme.onSurface
                )
            }
            brief.imageUrl?.let {
                Box(modifier = Modifier.padding(5.dp)) {
                    AsyncImage(
                        model = it,
                        contentDescription = brief.title,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .width(100.dp)
                    )
                }
            }
        }
    }
}
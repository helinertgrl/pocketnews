package com.example.pocketnews.presentation.home

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pocketnews.presentation.Screen
import com.example.pocketnews.presentation.home.components.NewsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val isRefreshing = viewModel.isRefreshing
    val activity = (LocalActivity.current as? android.app.Activity)
    val category = viewModel.selectedCategory

    val titleText = if (category == "general" || category.isEmpty()) {
        "Haberler"
    } else {
        category.replaceFirstChar { it.uppercase() }
    }


    BackHandler {
        activity?.finish()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    titleText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                ) },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.Settings.route) })
                    {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "ayarlar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val state = viewModel.uiState
            val context = LocalContext.current

            when (state) {
                is HomeUiState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(strokeWidth = 3.dp)
                }

                is HomeUiState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = { viewModel.refreshNews() }
                    ) {
                        if (state.articles.isEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "info",
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Henüz haber yok, ilk kontrol bekleniyor...",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    items(state.articles) { article ->
                                        NewsCard(
                                            article = article,
                                            onClick = {
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse(article.url)
                                                )
                                                context.startActivity(intent)
                                            }
                                        )
                                    }
                                }
                        }
                    }
                }
                is HomeUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(state.message,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            viewModel.refreshNews()},
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Yenile")
                        }
                    }
                }
            }
        }
    }
}

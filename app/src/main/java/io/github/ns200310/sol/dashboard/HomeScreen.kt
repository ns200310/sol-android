import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.ns200310.sol.dashboard.components.NewsCard

@Composable
        fun HomeScreen(
            modifier: Modifier = Modifier,
            navController: NavController,
            newsViewModel: NewsViewModel = viewModel()
        ) {
            val newsList = newsViewModel.newsList

            LaunchedEffect(Unit) {
                newsViewModel.fetchNews()
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(newsList) { article ->
                    println("Article ID: ${article.id}")
                    NewsCard(
                        headline = article.title,
                        id = article.id,
                        imageUrl = article.image_url, // Pass the image URL
                        navigationController = navController
                    )
                }
            }
        }
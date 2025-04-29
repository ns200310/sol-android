import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import kotlinx.atomicfu.TraceBase.None.append
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class NewsViewModel : ViewModel() {
    @Serializable
    data class NewsArticle(
        val id: Int,
        val title: String,
        val summary: String,
        val url: String,
        val image_url: String,
    )
    @Serializable
    data class NewsResponse(
        val results: List<NewsArticle>
    )
    var newsList by mutableStateOf<List<NewsArticle>>(emptyList())
    var filterednewsList by mutableStateOf<List<NewsArticle>>(emptyList())
        private set

    fun fetchNews() {
        viewModelScope.launch {
            try {
                val client = HttpClient()
                val response: HttpResponse = client.get("https://api.spaceflightnewsapi.net/v4/articles/") {
                    headers {
                        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    }
                }
                val data: String = response.body()
                val newsResponse = Json {
                    ignoreUnknownKeys = true
                }.decodeFromString<NewsResponse>(data)
                newsList = newsResponse.results
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error
            }
        }
    }
    fun getArticleById(id: Int): NewsArticle? {
        viewModelScope.launch {
            try {
                val client = HttpClient()
                val response: HttpResponse = client.get("https://api.spaceflightnewsapi.net/v4/articles/") {
                    headers {
                        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    }
                }
                val data: String = response.body()
                val newsResponse = Json {
                    ignoreUnknownKeys = true
                }.decodeFromString<NewsResponse>(data)
                filterednewsList = newsResponse.results.filter { it.id == id }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error
            }
        }

        return filterednewsList.firstOrNull()
    }
}
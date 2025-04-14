import com.salir.data.local.NewsLocalDataSource
import com.salir.data.mapper.toDomain
import com.salir.data.model.NewsCache
import com.salir.data.remote.api.NyTimesApi
import com.salir.data.remote.dto.NewsResponse
import com.salir.data.repository.NewsRepositoryImpl
import com.salir.util.NetworkResponse
import com.salir.util.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class NewsRepositoryImplTest {

    @Mock
    private lateinit var api: NyTimesApi

    @Mock
    private lateinit var localDataSource: NewsLocalDataSource

    private lateinit var repository: NewsRepositoryImpl

    private val newsResponse = NewsResponse(
        id = "1",
        title = "Title",
        abstract = "Description",
        multimedia = null,
        source = "Source",
        pubDate = "2023-01-01T00:00:00Z",
        url = "https://example.com/news"
    )

    @BeforeEach
    fun setup() {
        repository = NewsRepositoryImpl(api, localDataSource)
    }

    // Кеш есть, он свежий, refresh = false
    @Test
    fun `getNews should return cached data when cache is fresh and refresh is false`() = runTest {
        val cache = NewsCache(
            news = listOf(newsResponse),
            date = LocalDateTime.now()
        )
        whenever(localDataSource.getCache()).thenReturn(cache)

        val result = repository.getNews(limit = 10, refresh = false).first()

        assertEquals(Result.Success(listOf(newsResponse.toDomain()), done = false), result)
        verify(localDataSource, never()).saveCache(any()) // Данные не должны обновляться
        verify(api, never()).getRelevantNews(any()) // API не должен вызываться
    }

    // Кеш отсутствует, refresh = false
    @Test
    fun `getNews should load from API when cache is absent`() = runTest {
        whenever(localDataSource.getCache()).thenReturn(null)
        val apiNews = listOf(newsResponse)
        whenever(api.getRelevantNews(10)).thenReturn(NetworkResponse.Success(apiNews))

        val result = repository.getNews(limit = 10, refresh = false).toList()

        assertEquals(Result.Loading, result[0]) // Первым идёт Result.Loading
        assertEquals(Result.Success(apiNews.map { it.toDomain() }), result[1])

        verify(api).getRelevantNews(10) // API должен вызываться
        verify(localDataSource).saveCache(any()) // Данные должны сохраняться
    }

    // Кеш устарел, refresh = false
    @Test
    fun `getNews should load from API when cache is expired`() = runTest {
        val expiredCache = NewsCache(
            news = listOf(newsResponse),
            date = LocalDateTime.now().minusHours(7) // Устарел
        )
        whenever(localDataSource.getCache()).thenReturn(expiredCache)
        val apiNews = listOf(newsResponse)
        whenever(api.getRelevantNews(10)).thenReturn(NetworkResponse.Success(apiNews))

        val result = repository.getNews(limit = 10, refresh = false).toList()

        assertEquals(Result.Loading, result[0]) // Первым идёт Result.Loading
        assertEquals(Result.Success(apiNews.map { it.toDomain() }), result[1])

        verify(api).getRelevantNews(10) // API должен вызываться
        verify(localDataSource).saveCache(any()) // Данные должны сохраняться
    }

    // refresh = true
    @Test
    fun `getNews should load from API when refresh is true`() = runTest {
        val cache = NewsCache(
            news = listOf(newsResponse),
            date = LocalDateTime.now()
        )
        whenever(localDataSource.getCache()).thenReturn(cache)
        val apiNews = listOf(newsResponse)
        whenever(api.getRelevantNews(10)).thenReturn(NetworkResponse.Success(apiNews))

        val result = repository.getNews(limit = 10, refresh = true).toList()

        assertEquals(Result.Loading, result[0]) // Первым идёт Result.Loading
        assertEquals(Result.Success(apiNews.map { it.toDomain() }), result[1])

        verify(api).getRelevantNews(10) // API должен вызываться
        verify(localDataSource).saveCache(any()) // Данные должны сохраняться
    }

    // Ошибка при загрузке из API
    @Test
    fun `getNews should return failure when API call fails`() = runTest {
        whenever(localDataSource.getCache()).thenReturn(null)
        whenever(api.getRelevantNews(10)).thenReturn(NetworkResponse.Error("Network error"))

        val result = repository.getNews(limit = 10, refresh = false).toList()

        assertEquals(Result.Loading, result[0]) // Первым идёт Result.Loading
        assertEquals(Result.Failure("Network error"), result[1]) // Ошибка API

        verify(api).getRelevantNews(10) // API должен вызываться
        verify(localDataSource, never()).saveCache(any()) // Данные не должны сохраняться
    }
}

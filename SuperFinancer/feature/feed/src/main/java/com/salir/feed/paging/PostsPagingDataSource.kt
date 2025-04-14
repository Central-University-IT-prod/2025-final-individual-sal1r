package com.salir.feed.paging

import com.salir.feed.models.Post
import com.salir.paging.PagingDataSource
import com.salir.paging.PagingResult
import com.salir.util.AppDispatchers
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.withContext

class PostsPagingDataSource(
    private val supabase: SupabaseClient,
    private val dispatchers: AppDispatchers
): PagingDataSource<Post>() {

    override suspend fun load(limit: Int, page: Int): PagingResult<Post> = withContext(dispatchers.io) {
        try {
            val posts = supabase.from("posts")
                .select(Columns.ALL) {
                    range((page * limit).toLong(), ((page + 1) * limit - 1).toLong())
                }
                .decodeList<Post>()

            PagingResult.Success(data = posts, isLast = posts.size < limit)
        } catch (e: Exception) {
            PagingResult.Error(message = e.toString(), isLast = true)
        }
    }
}
package com.salir.feed.viewemodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salir.feed.models.Post
import com.salir.feed.paging.PostsPagingDataSource
import com.salir.paging.Paging
import com.salir.util.AppDispatchers
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedViewModel(
    private val supabase: SupabaseClient,
    private val dispatcher: AppDispatchers
): ViewModel() {
    private val _postsPaging = MutableStateFlow(Paging(
        dataSource = PostsPagingDataSource(supabase, dispatcher),
        coroutineScope = viewModelScope,
        limit = 10
    ))
    val postsPaging = _postsPaging.asStateFlow()


    private fun createPostsPaging(): Paging<Post> = Paging(
        dataSource = PostsPagingDataSource(supabase, dispatcher),
        coroutineScope = viewModelScope,
        limit = 10
    )

    fun refreshPosts() { _postsPaging.value = createPostsPaging() }

    fun loadNextPostsPage() {
        viewModelScope.launch {
            postsPaging.value.loadNextPage()
        }
    }
}
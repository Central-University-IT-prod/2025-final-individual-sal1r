package com.salir.feed.viewemodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.salir.feed.models.Post
import com.salir.util.AppDispatchers
import com.salir.util.getFileExtension
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonPrimitive
import java.util.UUID

class CreatePostViewModel(
    private val context: Application,
    private val supabase: SupabaseClient,
    private val dispatcher: AppDispatchers
): ViewModel() {

    suspend fun createPost(
        text: String,
        tags: List<String>,
        images: List<Uri>,
        newsTitle: String?,
        newsUrl: String?,
        newsImage:String?
    ): Boolean = withContext(dispatcher.io) {
        try {
            val user = supabase.auth.currentUserOrNull() ?: return@withContext false
            val userId = user.id
            val userLogin = user.userMetadata
                ?.getValue("login")?.jsonPrimitive
                ?.content ?: return@withContext false

            val files = images.map {
                val ext = getFileExtension(context, it) ?: return@withContext false
                Pair("${userId}_${UUID.randomUUID()}.$ext", it)
            }

            val imagePathes = supabase.storage.from("posts_images").let { bucket ->
                files.map {
                    bucket.upload(it.first, it.second).path
                }
            }

            supabase.from("posts")
                .insert(
                    Post(
                    text = text,
                    tags = tags,
                    images = imagePathes.map {
                        supabase.storage.from("posts_images").publicUrl(it)
                    },
                    newsTitle = newsTitle,
                    newsUrl = newsUrl,
                    newsImage = newsImage,
                    userLogin = userLogin
                )
                )

            true
        } catch (e: Exception) {
            false
        }
    }
}
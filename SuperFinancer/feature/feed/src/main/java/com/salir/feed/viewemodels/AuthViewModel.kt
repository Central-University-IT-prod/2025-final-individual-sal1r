package com.salir.feed.viewemodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salir.util.AppDispatchers
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthViewModel(
    private val supabase: SupabaseClient,
    private val dispatchers: AppDispatchers
): ViewModel() {
    val authStatus = supabase.auth.sessionStatus

    suspend fun signIn(email: String, password: String): Boolean = withContext(dispatchers.io) {
        try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun signUp(login: String, email: String, password: String): Boolean = withContext(dispatchers.io) {
        try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password

                data = buildJsonObject {
                    put("login", login)
                }
            }

            return@withContext true
        } catch (e: Exception) {
            false
        }
    }

    fun signOut() {
        viewModelScope.launch(dispatchers.io) {
            supabase.auth.signOut()
        }
    }
}
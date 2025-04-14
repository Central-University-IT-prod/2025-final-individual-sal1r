package com.salir.superFinancer.di.data

import com.salir.data.remote.api.FinhubApi
import com.salir.data.remote.api.NyTimesApi
import com.salir.superFinancer.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
    }

    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(get())
            }
        }
    }

    single<FinhubApi> {
        FinhubApi(
            apiKey = BuildConfig.FINHUB_API_KEY,
            get(), get(),
        )
    }

    single<NyTimesApi> {
        NyTimesApi(
            apiKey = BuildConfig.NYTIMES_API_KEY,
            get(), get(), get()
        )
    }

    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = com.salir.feed.BuildConfig.SUPABASE_URL,
            supabaseKey = com.salir.feed.BuildConfig.SUPABASE_KEY
        ) {
            defaultSerializer = KotlinXSerializer(get<Json>())

            install(Auth)
            install(Postgrest)
            install(Storage)
        }
    }
}
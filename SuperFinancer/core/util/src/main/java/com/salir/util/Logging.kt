package com.salir.util

import android.util.Log

inline fun logHttpResponseSuccess(data: String) {
    Log.i("HttpResponse", "Data success fetched: $data")
}

inline fun logHttpResponseError(message: String) {
    Log.e("HttpResponse", "Error fetching data: $message")
}
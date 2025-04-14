package com.salir.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

fun getFileExtension(context: Context, uri: Uri): String? {
    return if (uri.scheme.equals("content")) {
        val mime = context.contentResolver.getType(uri)
        MimeTypeMap.getSingleton().getExtensionFromMimeType(mime)
    } else {
        val path = uri.path
        val dotIndex = path?.lastIndexOf('.')
        if (dotIndex != null && dotIndex != -1) {
            path.substring(startIndex = dotIndex + 1)
        } else {
            null
        }
    }
}
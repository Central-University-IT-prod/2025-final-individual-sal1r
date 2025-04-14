package com.salir.search

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.salir.resources.R

@Composable
fun VoiceInputButton(onVoiceResult: (String?) -> Unit) {
    val context = LocalContext.current

    val voiceInputLauncher = rememberVoiceInputLauncher(onVoiceResult)

    IconButton(onClick = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
            putExtra(RecognizerIntent.EXTRA_PROMPT, context.getString(R.string.speak_now))
        }

        voiceInputLauncher.launch(intent)
    }) {
        Icon(
            painter = painterResource(R.drawable.ic_mic_20),
            contentDescription = null
        )
    }
}
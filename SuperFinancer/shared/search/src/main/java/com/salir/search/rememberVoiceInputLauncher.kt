package com.salir.search

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun rememberVoiceInputLauncher(
    onVoiceResult: (String?) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val voiceResults = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            onVoiceResult(voiceResults?.firstOrNull())
        } else {
            onVoiceResult(null)
        }
    }
package com.salir.feed.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.salir.feed.viewemodels.CreatePostViewModel
import com.salir.resources.R
import kotlinx.coroutines.launch

@Composable
fun CreatePostScreen(
    createPostViewModel: CreatePostViewModel,
    newsTitle: String? = null,
    newsUrl: String? = null,
    newsImage: String? = null,
    navigateBack: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        images = if (uris.size > 9) uris.subList(0, 9) else uris
    }

    var loading by remember { mutableStateOf(false) }


    CreatePostScreenContent(
        loading = loading,
        images = images,
        onBackClick = navigateBack,
        onCreatePostClick = { text, tags, uris ->
            coroutineScope.launch {
                loading = true
                val result = createPostViewModel.createPost(
                    text, tags, uris, newsTitle, newsUrl, newsImage
                ).apply { Log.d("test", "result: $this") }
                if (result) navigateBack()
                loading = false
            }
        },
        onSelectImagesClick = {
            multiplePhotoPickerLauncher.launch("image/*")
        },
        onClearImagesClick = { images = emptyList() },
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CreatePostScreenContent(
    loading: Boolean = false,
    images: List<Uri> = listOf(),
    onCreatePostClick: (
        text: String,
        tags: List<String>,
        images: List<Uri>,
    ) -> Unit = { _, _, _ -> },
    onClearImagesClick: () -> Unit = {},
    onSelectImagesClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.back)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back_36),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text(stringResource(R.string.tags_input)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.post_text_input)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = onSelectImagesClick) {
                    Text(stringResource(R.string.select_images))
                }

                if (images.isNotEmpty()) {
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(onClick = onClearImagesClick) {
                        Text(stringResource(R.string.select_images))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                images.forEach { imageUri ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }
            }

            if (images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${images.size}/9",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ElevatedButton(
                onClick = {
                    val tagsList = tags.split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                    onCreatePostClick(text, tagsList, images)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = text.isNotEmpty() && tags.isNotEmpty() && (images.size in 0..9) && !loading
            ) {
                Text(stringResource(R.string.save_post))
            }
        }
    }
}


@Composable
private fun CreatePostScreenPreview() {
    CreatePostScreenContent(
        images = listOf(
            Uri.parse("android.resource://com.salir.resources/R.drawable.ic_launcher_background"),
        )
    )
}

@Preview
@Composable
private fun CreatePostScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        CreatePostScreenPreview()
    }
}

@Preview
@Composable
private fun CreatePostScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        CreatePostScreenPreview()
    }
}
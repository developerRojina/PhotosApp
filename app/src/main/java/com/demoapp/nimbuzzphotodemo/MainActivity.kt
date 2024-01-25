package com.demoapp.nimbuzzphotodemo

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.demoapp.nimbuzzphotodemo.ui.theme.NimbuzzPhotoDemoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NimbuzzPhotoDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(mainViewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current // Get the current context

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(2)
    ) { uris ->
        if (!mainViewModel.handleSelectedImages(uris)) {
            Toast.makeText(context, "Please select 2 images", Toast.LENGTH_LONG).show()
        }
    }

    val userInput by mainViewModel.userInput
    val isError by mainViewModel.isError
    val isReset by mainViewModel.resetState

    Column {
        if (!isReset) {
            TextField(
                value = userInput,
                onValueChange = {
                    mainViewModel.setUserInput(it)
                },

                label = { Text("Enter a number") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }

        Button(
            enabled = !isError, onClick = {
                if (!isReset) {
                    launcher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                } else {
                    mainViewModel.resetState()
                }

            }, modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Text(if (!isReset) "Select Photo" else "Reset")
        }

        if (isReset) {
            val count = userInput.toIntOrNull() ?: 0
            val selectedImages by mainViewModel.selectedImages.collectAsState()
            if (selectedImages.isNotEmpty() && count > 0) {
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    items(count = count) { index ->
                        val imageUri = mainViewModel.getImageForIndex(index)
                        ImageListItem(imageUri)
                    }
                }
            }
        }
    }
}

@Composable
fun ImageListItem(imageUri: Uri?) {
    Image(
        painter = rememberImagePainter(imageUri),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()


    )
}

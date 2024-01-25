package com.demoapp.nimbuzzphotodemo

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class MainViewModel : ViewModel() {

    private val _userInput = mutableStateOf("")
    val userInput: State<String> get() = _userInput

    private val _isError = mutableStateOf(true)
    val isError: State<Boolean> get() = _isError

    private val _resetState = mutableStateOf(false)
    val resetState: State<Boolean> get() = _resetState


    private val _selectedImages = MutableStateFlow<List<Uri?>>(emptyList())
    val selectedImages = _selectedImages.asStateFlow()


    fun setUserInput(input: String) {
        _userInput.value = input
        validateInput(input)
    }

    fun setResetState(reset: Boolean) {
        _resetState.value = reset
    }

    private fun validateInput(text: String) {
        _isError.value = text.isEmpty() || text.toInt() == 0
    }

    fun updateSelectedImages(images: List<Uri?>) {
        viewModelScope.launch {
            _selectedImages.emit(images)
        }
    }

    fun handleSelectedImages(uris: List<Uri>): Boolean {
        return if (uris.size == 2) {
            updateSelectedImages(uris)
            setResetState(true)
            true
        } else {
            false

        }
    }

    fun getImageForIndex(index: Int): Uri? {
        val selectedImages = _selectedImages.value
        return if (isTriangularNumber(index)) {
            selectedImages.getOrNull(0) // Assuming the first image is for triangular indices
        } else {
            selectedImages.getOrNull(1) // Assuming the second image is for non-triangular indices
        }
    }

    fun isTriangularNumber(n: Int): Boolean {
        val m = (sqrt(8 * n + 1.0) - 1) / 2
        return m % 1 == 0.0
    }

    fun resetState() {
        setResetState(false)
        clearSelectedImages()
        setUserInput("")
    }

    fun clearSelectedImages() {
        viewModelScope.launch {
            _selectedImages.emit(emptyList())
        }
    }

}
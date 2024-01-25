package com.demoapp.nimbuzzphotodemo

import android.net.Uri
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {


    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel()
    }

    @Test
    fun `setUserInput should update userInput and validateInput`() {
        viewModel.setUserInput("42")
        assertEquals("42", viewModel.userInput.value)
        assertFalse(viewModel.isError.value)

        viewModel.setUserInput("")
        assertEquals("", viewModel.userInput.value)
        assertTrue(viewModel.isError.value)
    }

    @Test
    fun `setResetState should update resetState`() {
        viewModel.setResetState(true)
        assertTrue(viewModel.resetState.value)

        viewModel.setResetState(false)
        assertFalse(viewModel.resetState.value)
    }



    @Test
    fun `isTriangularNumber should return correct result`() {
        assertTrue(viewModel.isTriangularNumber(0))
        assertTrue(viewModel.isTriangularNumber(1))
        assertTrue(viewModel.isTriangularNumber(3))
        assertFalse(viewModel.isTriangularNumber(2))
        assertFalse(viewModel.isTriangularNumber(4))
    }


}

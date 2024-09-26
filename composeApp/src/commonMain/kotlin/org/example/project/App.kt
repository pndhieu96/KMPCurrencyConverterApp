package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import di.initializeKoin
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.jetbrains.compose.resources.ExperimentalResourceApi
import presentation.screen.Home.HomeScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {

    MaterialTheme {
        Navigator(HomeScreen())
    }
}
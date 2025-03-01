package com.example.newsreader.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.example.newsreader.ui.screens.SearchNewsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNewsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                SearchNewsScreen()
            }
        }
    }
}


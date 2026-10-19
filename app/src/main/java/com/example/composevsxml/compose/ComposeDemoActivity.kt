package com.example.composevsxml.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.composevsxml.shared.DemoViewModel

class ComposeDemoActivity : ComponentActivity() {
    private val vm: DemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DemoScreen(vm)
            }
        }
    }
}

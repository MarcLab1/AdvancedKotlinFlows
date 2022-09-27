package com.advancedkotlinflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.advancedkotlinflows.ui.home.HomePage
import com.advancedkotlinflows.ui.theme.AdvancedKotlinFlowsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdvancedKotlinFlowsTheme {
                HomePage()
            }
        }
    }
}

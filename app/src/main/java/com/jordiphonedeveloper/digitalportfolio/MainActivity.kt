package com.jordiphonedeveloper.digitalportfolio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jordiphonedeveloper.digitalportfolio.core.designsystem.PortfolioTheme
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileFeatureApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PortfolioTheme {
                ProfileFeatureApp()
            }
        }
    }
}

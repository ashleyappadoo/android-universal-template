package com.example.universal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universal.ads.AdsInitializer
import com.example.universal.ads.ConsentManager

class MainActivity : ComponentActivity() {
    private lateinit var consentManager: ConsentManager
    private var showPrivacyOptions by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        consentManager = ConsentManager(this)
        consentManager.requestConsent(this) { canRequestAds ->
            showPrivacyOptions = consentManager.isPrivacyOptionsRequired()
            if (canRequestAds) AdsInitializer.initialize(this)
        }

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Universal Android Template", style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(12.dp))
                        Text("API 36 • CI/CD • Signed AAB • AdMob + UMP")

                        if (showPrivacyOptions) {
                            Spacer(Modifier.height(24.dp))
                            Button(onClick = { consentManager.showPrivacyOptions(this@MainActivity) }) {
                                Text("Privacy choices")
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.idleoffice.idledisclaimer

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.idleoffice.idleconsent.IdleConsent
import com.idleoffice.idleconsent.IdleConsentCallback
import com.idleoffice.idleconsent.IdleConsentConfig
import com.idleoffice.idleconsent.IdleInfoSource

class MainActivity : AppCompatActivity() {

    private val idleConfig = IdleConsentConfig(
        "Terms and Privacy Notice",
        "We care about your experience and privacy using Super Testy App. Please take a moment to read through and acknowledge our policies",
        "To ensure the best experience, we collect anonymized user data to inform us of crashes and how our users interact with the app.",
        listOf("GPS location", "Device information", "Usage statistics"),
        IdleInfoSource(
            "Please see our full privacy policy.",
            Uri.parse("https://idleoffice-26abd.firebaseapp.com/quicklink/privacy_policy.html")
        ),
        false,
        "Please support Super Testy App by allowing us to use your data as mentioned in the privacy policy",
        true,
        "In order to use Super Testy App we require that you agree to our terms and conditions:",
        IdleInfoSource("See full terms and condtions", DemoActivity ::class.java)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val consent = IdleConsent.getInstance(this, idleConfig)
        if (!consent.hasUserAgreedToTerms) {
            consent.showConsentDialog(supportFragmentManager, null)
        }
    }
}

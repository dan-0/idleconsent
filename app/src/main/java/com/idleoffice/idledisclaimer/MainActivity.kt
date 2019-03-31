/*
 * MIT License
 *
 * Copyright (c) 2019 Dan Lowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.idleoffice.idledisclaimer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.idleoffice.idleconsent.*

class MainActivity : AppCompatActivity() {

    private val idleConfig = IdleConsentConfig(
        "Terms and Privacy Notice",
        "We care about your experience and privacy using Super Testy App. Please take a moment to read through and acknowledge our policies",
        "To ensure the best experience, we collect anonymized user data to inform us of crashes and how our users interact with the app.",
        listOf("GPS location", "Device information", "Usage statistics"),
        IdleInfoSource.Web(
            "Please see our full privacy policy.",
            Uri.parse("https://idleoffice-26abd.firebaseapp.com/quicklink/privacy_policy.html")
        ),
        false,
        "Please support Super Testy App by allowing us to use your data as mentioned in the privacy policy",
        true,
        "In order to use Super Testy App we require that you agree to our terms and conditions:",
        IdleInfoSource.Text("See full terms and condtions", "This is just a test"),
        version = 2
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val consent = IdleConsent(this)
        if (!consent.hasUserAgreedToTerms() || consent.isNewConsentVersion(2)) {
            consent.showConsentDialog(supportFragmentManager, null, idleConfig)
        }
    }
}

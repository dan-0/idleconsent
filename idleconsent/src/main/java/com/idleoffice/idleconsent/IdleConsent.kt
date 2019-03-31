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

package com.idleoffice.idleconsent

import android.content.Context
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentManager
import com.idleoffice.idleconsent.validation.IdleConsentConfigException
import com.idleoffice.idleconsent.validation.IdleConsentConfigValidator
import com.idleoffice.idleconsent.view.IdleConsentDialog
import com.idleoffice.idleconsent.view.IdleConsentDialog.Companion.CONSENT_CONFIG_KEY

/**
 * Main class user consent actions
 */
class IdleConsent(context: Context) {

    private val sharedPrefKey = "com.idleoffice.idleconsent.prefs"
    private val tocPrefKey = "$sharedPrefKey.TOC"
    private val privacyPrefKey = "$sharedPrefKey.PRIVACY"
    private val versionPrefKey = "$sharedPrefKey.VERSION"

    private val sharedPreferences = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)

    fun hasUserAgreedToTerms(): Boolean {
        return sharedPreferences.getBoolean(tocPrefKey, false)
    }

    fun hasUserAgreedToPrivacy(): Boolean {
        return sharedPreferences.getBoolean(privacyPrefKey, false)
    }

    /**
     * Display the consent dialog to the user.
     *
     * @param fragmentManager [FragmentManager] to hold the view
     * @param onAcceptCallback [IdleConsentCallback] to receive a callback of a user action
     * @param config [IdleConsentConfig] to use in displaying consent screen to user
     * @throws IdleConsentConfigException if error with provided [config]
     */
    @Throws(IdleConsentConfigException::class)
    fun showConsentDialog(
        fragmentManager: FragmentManager,
        onAcceptCallback: IdleConsentCallback?,
        config: IdleConsentConfig
    ) {
        val dialog = IdleConsentDialog()

        IdleConsentConfigValidator.validate(config)

        val args = Bundle().also {
            it.putParcelable(CONSENT_CONFIG_KEY, config)
        }
        dialog.arguments = args

        dialog.show(fragmentManager, onAcceptCallback)
    }

    /**
     * Returns true if the [newVersion] passed in is greater than the currently accepted version by the user
     */
    fun isNewConsentVersion(newVersion: Long): Boolean = newVersion > sharedPreferences.getLong(versionPrefKey, -1)

    /**
     * Update the user preferences.
     *
     * @param isTermsAccepted True if the user accepts terms and conditions
     * @param isPrivacyAccepted True if the user accepts the privacy agreement
     */
    fun updateUserPreferences(isTermsAccepted: Boolean, isPrivacyAccepted: Boolean) {
        updateUserPrefs(isTermsAccepted, isPrivacyAccepted, null)
    }

    internal fun updateUserPrefs(
        isTermsAccepted: Boolean,
        isPrivacyAccepted: Boolean,
        version: Long?
    ) {
        val pref = sharedPreferences.edit()

        pref.putBoolean(tocPrefKey, isTermsAccepted)
        pref.putBoolean(privacyPrefKey, isPrivacyAccepted)

        if (version != null) {
            pref.putLong(versionPrefKey, version)
        }

        pref.apply()
    }
}
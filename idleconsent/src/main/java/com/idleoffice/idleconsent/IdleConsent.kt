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
import androidx.fragment.app.FragmentManager
import com.idleoffice.idleconsent.validation.IdleConsentConfigValidator
import com.idleoffice.idleconsent.view.IdleConsentDialog
import com.idleoffice.idleconsent.view.IdleConsentDialog.Companion.CONSENT_CONFIG_KEY

/**
 * Main class user consent actions
 */
class IdleConsent private constructor(versionCode: Long) {



    var hasUserAgreedToTerms = false
        private set
    var hasUserAgreedToPrivacy = false
        private set

    private val sharedPrefKey = "com.idleoffice.idleconsent.prefs$versionCode"
    private val tocPrefKey = "$sharedPrefKey.TOC"
    private val privacyPrefKey = "$sharedPrefKey.PRIVACY"

    companion object {
        private var instance: IdleConsent? = null

        @JvmStatic
        fun getInstance(context: Context): IdleConsent {
            if (instance != null) {
                return instance!!
            }

            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            info.longVersionCode

            instance = IdleConsent(info.longVersionCode).also {
                it.setup(context)
            }

            return instance!!
        }
    }

    private fun setup(context: Context) {


        val prefs = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
        hasUserAgreedToTerms = prefs.getBoolean(tocPrefKey, false)
        hasUserAgreedToPrivacy = prefs.getBoolean(privacyPrefKey, false)
    }

    /**
     * Display the consent dialog to the user.
     *
     * @param fragmentManager [FragmentManager] to hold the view
     * @param onAcceptCallback [IdleConsentCallback] to receive a callback of a user action
     */
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
     * Update the user preferences.
     *
     * @param context A context object
     * @param isTermsAccepted True if the user accepts terms and conditions
     * @param isPrivacyAccepted True if the user accepts the privacy agreement
     */
    fun updateUserPreferences(context: Context, isTermsAccepted: Boolean, isPrivacyAccepted: Boolean) {
        updateUserPrefs(context, isTermsAccepted, isPrivacyAccepted)
    }

    internal fun updateUserPrefs(context: Context, isTermsAccepted: Boolean, isPrivacyAccepted: Boolean) {
        hasUserAgreedToTerms = isTermsAccepted
        hasUserAgreedToPrivacy = isPrivacyAccepted
        context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE).edit()
            .putBoolean(tocPrefKey, isTermsAccepted)
            .putBoolean(privacyPrefKey, isPrivacyAccepted)
            .apply()
    }
}
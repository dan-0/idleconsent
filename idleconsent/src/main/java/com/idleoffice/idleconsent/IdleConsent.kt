/*
 * IdleConsent
 * Copyright (C) 2019 IdleOffice LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
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
class IdleConsent internal constructor( /* exposed for testing */) {

    var hasUserAgreedToTerms = _hasUserAgreedToTerms
        private set
    var hasUserAgreedToPrivacy = _hasUserAgreedToPrivacy
        private set

    companion object {
        internal const val CONSENT_SHARED_PREF_KEY = "com.idleoffice.idleconsent.prefs"
        internal const val TOC_KEY = "$CONSENT_SHARED_PREF_KEY.TOC"
        internal const val PRIVACY_KEY = "$CONSENT_SHARED_PREF_KEY.PRIVACY"

        private var instance: IdleConsent? = null

        private var _hasUserAgreedToTerms = false
        private var _hasUserAgreedToPrivacy = false

        @JvmStatic
        fun getInstance(context: Context): IdleConsent {
            if (instance != null) {
                return instance!!
            }

            val prefs = context.getSharedPreferences(CONSENT_SHARED_PREF_KEY, Context.MODE_PRIVATE)
            _hasUserAgreedToTerms = prefs.getBoolean(TOC_KEY, false)
            _hasUserAgreedToPrivacy = prefs.getBoolean(PRIVACY_KEY, false)

            instance = IdleConsent()

            return instance!!
        }
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
        context.getSharedPreferences(CONSENT_SHARED_PREF_KEY, Context.MODE_PRIVATE).edit()
            .putBoolean(TOC_KEY, isTermsAccepted)
            .putBoolean(PRIVACY_KEY, isPrivacyAccepted)
            .apply()
    }
}
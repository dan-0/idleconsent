package com.idleoffice.idleconsent

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.idleoffice.idleconsent.validation.IdleConsentConfigValidator
import com.idleoffice.idleconsent.view.IdleConsentDialog
import com.idleoffice.idleconsent.view.IdleConsentDialog.Companion.CONSENT_CONFIG_KEY

class IdleConsent private constructor() {

    var hasUserAgreedToTerms = Companion.hasUserAgreedToTerms
        private set
    var hasUserAgreedToPrivacy = Companion.hasUserAgreedToPrivacy
        private set

    companion object {
        internal const val CONSENT_SHARED_PREF_KEY = "com.idleoffice.idleconsent.prefs"
        internal const val TOC_KEY = "$CONSENT_SHARED_PREF_KEY.TOC"
        internal const val PRIVACY_KEY = "$CONSENT_SHARED_PREF_KEY.PRIVACY"

        private lateinit var config: IdleConsentConfig
        private var instance: IdleConsent? = null

        private var hasUserAgreedToTerms = false
        private var hasUserAgreedToPrivacy = false

        fun getInstance(context: Context, config: IdleConsentConfig): IdleConsent {
            if (instance != null) {
                return instance!!
            }

            val prefs = context.getSharedPreferences(CONSENT_SHARED_PREF_KEY, Context.MODE_PRIVATE)
            hasUserAgreedToTerms = prefs.getBoolean(TOC_KEY, false)
            hasUserAgreedToPrivacy = prefs.getBoolean(PRIVACY_KEY, false)

            this.config = config

            instance = IdleConsent()

            return instance!!
        }
    }

    fun showConsentDialog(
        fragmentManager: FragmentManager,
        onAcceptCallback: IdleConsentCallback?
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
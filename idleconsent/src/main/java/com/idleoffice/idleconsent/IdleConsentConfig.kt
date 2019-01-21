package com.idleoffice.idleconsent

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @param consentTitle Title for the consent dialog (optional)
 * @param introStatement Consent introductory paragraph
 * @param dataCollectedSummary Summary of the data collected by the app and why
 * @param dataCollected List of data collected by the app
 * @param privacyInfoSource [IdleInfoSource] for displaying an extended version of the privacy policy
 * @param requirePrivacy True if user consent to the privacy policy is required by the user, otherwise they are
 *          provided an option to opt out
 * @param acceptPrivacyPrompt When [requirePrivacy] is false, this is the text displayed next to the checkbox for
 *          accepting the privacy policy
 * @param privacyPromptChecked Default checkbox state for the optional [acceptPrivacyPrompt]
 * @param termsSummary Summary of app terms and conditions
 * @param termsInfoSource [IdleInfoSource] for displaying an extended version of the app's terms and conditions
 */
@Parcelize
data class IdleConsentConfig(
    val consentTitle: CharSequence = "",
    val introStatement: CharSequence = "",
    val dataCollectedSummary: CharSequence = "",
    val dataCollected: List<CharSequence> = listOf(),
    val privacyInfoSource: IdleInfoSource? = null,
    val requirePrivacy: Boolean = true,
    val acceptPrivacyPrompt: CharSequence = "",
    var privacyPromptChecked: Boolean = true,
    val termsSummary: CharSequence = "",
    val termsInfoSource: IdleInfoSource? = null
) : Parcelable {
    class Builder(config: IdleConsentConfig?) {
        private val newConfig = config ?: IdleConsentConfig("")

        private var consentTitle: CharSequence = newConfig.consentTitle
        private var introStatement: CharSequence = newConfig.introStatement
        private var dataCollectedSummary: CharSequence = newConfig.dataCollectedSummary
        private var dataCollected: List<CharSequence> = newConfig.dataCollected
        private var privacyInfoSource: IdleInfoSource? = newConfig.privacyInfoSource
        private var requirePrivacy: Boolean = newConfig.requirePrivacy
        private var acceptPrivacyPrompt: CharSequence = newConfig.acceptPrivacyPrompt
        private var privacyPromptChecked: Boolean = newConfig.privacyPromptChecked
        private var termsSummary: CharSequence = newConfig.termsSummary
        private var termsInfoSource: IdleInfoSource? = newConfig.termsInfoSource

        fun setConsentTitle(consentTitle: CharSequence): IdleConsentConfig.Builder {
            this.consentTitle = consentTitle
            return this
        }

        fun setIntroStatement(introStatement: CharSequence): IdleConsentConfig.Builder {
            this.introStatement = introStatement
            return this
        }

        fun setDataCollectedSummary(dataCollectedSummary: CharSequence): IdleConsentConfig.Builder {
            this.dataCollectedSummary = dataCollectedSummary
            return this
        }

        fun setDataCollected(dataCollected: List<CharSequence>): IdleConsentConfig.Builder {
            this.dataCollected = dataCollected
            return this
        }

        fun setPrivacyInfoSource(privacyInfoSource: IdleInfoSource): IdleConsentConfig.Builder {
            this.privacyInfoSource = privacyInfoSource
            return this
        }

        fun setRequirePrivacy(requirePrivacy: Boolean): IdleConsentConfig.Builder {
            this.requirePrivacy = requirePrivacy
            return this
        }

        fun setAcceptPrivacyPrompt(acceptPrivacyPrompt: CharSequence): IdleConsentConfig.Builder {
            this.acceptPrivacyPrompt = acceptPrivacyPrompt
            return this
        }

        fun setPrivacyPromptChecked(privacyPromptChecked: Boolean): IdleConsentConfig.Builder {
            this.privacyPromptChecked = privacyPromptChecked
            return this
        }

        fun setTermsSummary(termsSummary: CharSequence): IdleConsentConfig.Builder {
            this.termsSummary = termsSummary
            return this
        }

        fun settermsInfoSource(termsInfoSource: IdleInfoSource): IdleConsentConfig.Builder {
            this.termsInfoSource = termsInfoSource
            return this
        }

        fun build(): IdleConsentConfig {
            return IdleConsentConfig(
                consentTitle,
                introStatement,
                dataCollectedSummary,
                dataCollected,
                privacyInfoSource,
                requirePrivacy,
                acceptPrivacyPrompt,
                privacyPromptChecked,
                termsSummary,
                termsInfoSource
            )
        }
    }
}
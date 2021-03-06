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

package me.danlowe.idleconsent

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
 * @param version Policy [version], increase to display to user if policy is updated
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
    val termsInfoSource: IdleInfoSource? = null,
    val version: Long = 0
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
        private var version: Long = newConfig.version

        fun setConsentTitle(consentTitle: CharSequence): Builder {
            this.consentTitle = consentTitle
            return this
        }

        fun setIntroStatement(introStatement: CharSequence): Builder {
            this.introStatement = introStatement
            return this
        }

        fun setDataCollectedSummary(dataCollectedSummary: CharSequence): Builder {
            this.dataCollectedSummary = dataCollectedSummary
            return this
        }

        fun setDataCollected(dataCollected: List<CharSequence>): Builder {
            this.dataCollected = dataCollected
            return this
        }

        fun setPrivacyInfoSource(privacyInfoSource: IdleInfoSource): Builder {
            this.privacyInfoSource = privacyInfoSource
            return this
        }

        fun setRequirePrivacy(requirePrivacy: Boolean): Builder {
            this.requirePrivacy = requirePrivacy
            return this
        }

        fun setAcceptPrivacyPrompt(acceptPrivacyPrompt: CharSequence): Builder {
            this.acceptPrivacyPrompt = acceptPrivacyPrompt
            return this
        }

        fun setPrivacyPromptChecked(privacyPromptChecked: Boolean): Builder {
            this.privacyPromptChecked = privacyPromptChecked
            return this
        }

        fun setTermsSummary(termsSummary: CharSequence): Builder {
            this.termsSummary = termsSummary
            return this
        }

        fun settermsInfoSource(termsInfoSource: IdleInfoSource): Builder {
            this.termsInfoSource = termsInfoSource
            return this
        }

        fun setVersion(version: Long): Builder {
            this.version = version
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
                termsInfoSource,
                version
            )
        }
    }
}
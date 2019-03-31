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

package com.idleoffice.idleconsent.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.idleoffice.idleconsent.*
import kotlinx.android.synthetic.main.idle_consent_dialog.*

internal class IdleConsentDialog : DialogFragment() {
    private lateinit var config: IdleConsentConfig
    private var acceptedCallback: IdleConsentCallback? = null

    companion object {
        internal const val CONSENT_CONFIG_KEY = "CONSENT_CONFIG_KEY"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(CONSENT_CONFIG_KEY, config)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        config = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(CONSENT_CONFIG_KEY)!!
        } else {
            arguments!!.getParcelable(CONSENT_CONFIG_KEY)!!
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
                .setView(R.layout.idle_consent_dialog)
                .setPositiveButton(getString(R.string.agree)) { _, _ ->
                    val privacyAccepted = if (config.requirePrivacy) {
                        true
                    } else {
                        dialog.accept_privacy_policy_prompt.isChecked
                    }
                    acceptedCallback?.onAcknowledged(true, privacyAccepted)

                    IdleConsent.getInstance(it).updateUserPrefs(it, true, privacyAccepted, config.version)
                }
                .setCancelable(false)

            if (config.consentTitle.isNotEmpty()) {
                builder.setTitle(config.consentTitle)
            }

            builder.create()
        } ?: throw IllegalStateException("Null Activity")
    }

    override fun onStart() {
        super.onStart()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        dialog.intro_text.setTextOrGone(config.introStatement)

        dialog.terms_text.setTextOrGone(buildListSection(config.termsSummary, listOf()))

        addInfoSource(config.termsInfoSource, dialog.terms_link)

        dialog.data_collected_text.setTextOrGone(buildListSection(config.dataCollectedSummary, config.dataCollected))

        addInfoSource(config.privacyInfoSource, dialog.data_collected_link)

        dialog.accept_privacy_policy_prompt.isChecked = config.privacyPromptChecked
        dialog.accept_privacy_policy_prompt.setOnCheckedChangeListener { _, isChecked ->
            config.privacyPromptChecked = isChecked
        }
        dialog.accept_privacy_policy_prompt.setTextOrGone(config.acceptPrivacyPrompt, !config.requirePrivacy)

        setMandatoryTextInfo(config)
    }

    private fun addInfoSource(source: IdleInfoSource?, view: TextView) {
        source?.let { infoSource ->
            view.setTextOrGone(infoSource.linkText, true)
            view.setOnClickListener { infoSource.showInfo(it.context) }
        } ?: view.gone()
    }

    fun show(manager: FragmentManager, callback: IdleConsentCallback?) {
        acceptedCallback = callback
        super.show(manager, null)
    }

    private fun buildListSection(description: CharSequence, items: List<CharSequence>): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        builder.append(description)
        builder.appendln("")
        buildBulletedList(builder, items)
        return builder
    }

    private fun buildBulletedList(builder: SpannableStringBuilder, items: List<CharSequence>): SpannableStringBuilder {
        items.forEach {
            val item = SpannableString("\n$it")
            item.setSpan(LeadingMarginSpan.Standard(30, 60), 1, item.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            item.setSpan(BulletSpan(30), 1, item.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.append(item)
        }

        return builder
    }

    private fun setMandatoryTextInfo(config: IdleConsentConfig) {
        val mandatoryItems = mutableListOf<String>()

        if (hasTermsAndConditions(config)) {
            mandatoryItems.add(getString(R.string.terms_and_conditions))
        }

        if (config.requirePrivacy) {
            mandatoryItems.add(getString(R.string.privacy_policy))
        }

        dialog.mandatory_items.setTextOrGone(
            buildListSection(getString(R.string.mandatory_accept), mandatoryItems)
        )
    }

    private fun hasTermsAndConditions(config: IdleConsentConfig): Boolean {
        return config.termsSummary.isNotEmpty() || config.termsInfoSource != null
    }

    private fun View.gone() {
        this.visibility = View.GONE
    }

    /**
     * Sets the text of the [TextView] as long as [shouldShowConditions] are all true (or empty) and [textVal] is not
     * empty
     */
    private fun TextView.setTextOrGone(textVal: CharSequence?, vararg shouldShowConditions: Boolean) {
        val shouldShow = !shouldShowConditions.contains(false)

        if (shouldShow && !textVal.isNullOrEmpty()) {
            text = textVal
        } else {
            gone()
        }
    }
}
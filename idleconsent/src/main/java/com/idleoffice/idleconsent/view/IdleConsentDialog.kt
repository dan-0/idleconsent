package com.idleoffice.idleconsent.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
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
import com.idleoffice.idleconsent.IdleConsent
import com.idleoffice.idleconsent.IdleConsentCallback
import com.idleoffice.idleconsent.R
import com.idleoffice.idleconsent.IdleConsentConfig
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
                .setPositiveButton("agree") { _ , _ ->
                    val privacyAccepted = if (config.requirePrivacy) {
                        true
                    } else {
                        dialog.accept_privacy_policy_prompt.isChecked
                    }
                    acceptedCallback?.onAccept(true, privacyAccepted)

                    IdleConsent.getInstance(it, config).updateUserPrefs(it, true, privacyAccepted)
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

        val isTermsInfoNull = config.termsInfoSource == null
        dialog.terms_link.setTextOrGone(config.termsInfoSource?.linkText, !isTermsInfoNull)
        if (!isTermsInfoNull) {
            dialog.terms_link.setOnClickListener { config.termsInfoSource!!.showInfo(it.context) }
        }

        dialog.data_collected_text.setTextOrGone(buildListSection(config.dataCollectedSummary, config.dataCollected))

        val isPrivacyInfoNull = config.privacyInfoSource == null
        dialog.data_collected_link.setTextOrGone(config.privacyInfoSource?.linkText, !isPrivacyInfoNull)
        if (!isPrivacyInfoNull) {
            dialog.data_collected_link.setOnClickListener { config.privacyInfoSource!!.showInfo(it.context) }
        }

        dialog.accept_privacy_policy_prompt.isChecked = config.privacyPromptChecked
        dialog.accept_privacy_policy_prompt.setOnCheckedChangeListener { _, isChecked ->
            config.privacyPromptChecked = isChecked
        }
        dialog.accept_privacy_policy_prompt.setTextOrGone(config.acceptPrivacyPrompt, !config.requirePrivacy)

        setMandatoryTextInfo(config)
    }

    fun show(manager: FragmentManager, callback: IdleConsentCallback?) {
        acceptedCallback = callback
        super.show(manager, null)
    }

    private fun buildListSection(description: CharSequence, items: List<CharSequence>): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        builder.append(description)
        buildBulletedList(builder, items)
        return builder
    }

    private fun buildBulletedList(builder: SpannableStringBuilder, items: List<CharSequence>): SpannableStringBuilder {
        items.forEach {
            val item = SpannableString("\n$it")
            item.setSpan(LeadingMarginSpan.Standard(30, 60), 1, item.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            item.setSpan(BulletSpan(30, Color.BLACK), 1, item.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.append(item)
        }

        return builder
    }

    private fun setMandatoryTextInfo(config: IdleConsentConfig) {
        val mandatoryItems = mutableListOf<String>()

        if (hasTermsAndConditions(config)) {
            mandatoryItems.add("Terms and Conditions")
        }

        if (config.requirePrivacy) {
            mandatoryItems.add("Privacy Policy")
        }

        dialog.mandatory_items.setTextOrGone(
            buildListSection("By selecting AGREE you acknowledge and accept our: ", mandatoryItems))
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
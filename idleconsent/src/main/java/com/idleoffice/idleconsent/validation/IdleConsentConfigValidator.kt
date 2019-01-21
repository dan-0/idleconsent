package com.idleoffice.idleconsent.validation

import com.idleoffice.idleconsent.IdleConsentConfig

internal object IdleConsentConfigValidator {
    fun validate(consentConfig: IdleConsentConfig) {
        val errors = mutableListOf<String>()

        validateRequirePrivacy(consentConfig)?.also {
            errors.add(it)
        }

        if (errors.isNotEmpty()) {
            throw IdleConsentConfigException(errors.joinToString("|", " ", " "))
        }
    }

    private fun validateRequirePrivacy(config: IdleConsentConfig): String? {
        if (!config.requirePrivacy && config.acceptPrivacyPrompt.isEmpty()) {
            return "an acceptPrivacyPrompt must be provided if requirePrivacy is false"
        }

        return null
    }

    private class IdleConsentConfigException(msg: String): Error(msg)
}
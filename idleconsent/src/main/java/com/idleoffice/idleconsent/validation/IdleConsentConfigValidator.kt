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
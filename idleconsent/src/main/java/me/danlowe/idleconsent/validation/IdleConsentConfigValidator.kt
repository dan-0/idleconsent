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

package me.danlowe.idleconsent.validation

import me.danlowe.idleconsent.IdleConsentConfig

internal object IdleConsentConfigValidator {

    @Throws(IdleConsentConfigException::class)
    fun validate(consentConfig: IdleConsentConfig) {
        val errors = mutableListOf<String>()

        validateRequirePrivacy(
            consentConfig
        )?.also {
            errors.add(it)
        }

        validateVersion(
            consentConfig
        )?.also {
            errors.add(it)
        }

        if (errors.isNotEmpty()) {
            throw IdleConsentConfigException(
                errors.joinToString(
                    "|",
                    " ",
                    " "
                )
            )
        }
    }

    private fun validateRequirePrivacy(config: IdleConsentConfig): String? {
        return if (!config.requirePrivacy && config.acceptPrivacyPrompt.isEmpty()) {
            "an acceptPrivacyPrompt must be provided if requirePrivacy is false"
        } else {
            null
        }
    }

    private fun validateVersion(config: IdleConsentConfig): String? {
        return if (config.version < 0) {
            "version must be greater than 0"
        } else {
            null
        }
    }
}

/**
 * Thrown if there is an error with the [IdleConsentConfig]
 *
 * @param msg A `|` separated list of errors
 */
class IdleConsentConfigException(msg: String) : Error(msg)
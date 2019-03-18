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
import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class IdleConsentTest {

    private val testConfig = IdleConsentConfig(
        "Terms and Privacy Notice",
        "We care about your experience and privacy using Super Testy App. Please take a moment to read through and acknowledge our policies",
        "To ensure the best experience, we collect anonymized user data to inform us of crashes and how our users interact with the app.",
        listOf("GPS location", "Device information", "Usage statistics"),
        IdleInfoSource("Please see our full privacy policy.","Just a test"),
        false,
        "Please support Super Testy App by allowing us to use your data as mentioned in the privacy policy",
        true,
        "In order to use Super Testy App we require that you agree to our terms and conditions:",
        IdleInfoSource("See full terms and condtions", "Just a test")
    )

    private lateinit var mockContext: Context
    private lateinit var mockPrefs: SharedPreferences

    private lateinit var ut: IdleConsent

    @Before
    fun setUp() {
        mockContext = mock()
        mockPrefs = mock()

        whenever(mockContext.getSharedPreferences(IdleConsent.CONSENT_SHARED_PREF_KEY, Context.MODE_PRIVATE)).thenReturn(mockPrefs)
        whenever(mockPrefs.getBoolean(IdleConsent.TOC_KEY, false)).thenReturn(true)
        whenever(mockPrefs.getBoolean(IdleConsent.PRIVACY_KEY, false)).thenReturn(true)

        ut = IdleConsent()
    }

    @Test
    fun `test singleton instantiation`() {
        IdleConsent.getInstance(mockContext)
        verify(mockContext, times(1)).getSharedPreferences(any(), any())
        verify(mockPrefs, times(1)).getBoolean(IdleConsent.TOC_KEY, false)
        verify(mockPrefs, times(1)).getBoolean(IdleConsent.PRIVACY_KEY, false)
    }

    @Test
    fun `update user preferences happy path`() {

        val mockEditor: SharedPreferences.Editor = mock()
        whenever(mockPrefs.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putBoolean(any(), any())).thenReturn(mockEditor)

        ut.updateUserPreferences(mockContext, false, false)

        verify(mockEditor, times(1)).putBoolean(IdleConsent.TOC_KEY, false)
        verify(mockEditor, times(1)).putBoolean(IdleConsent.PRIVACY_KEY, false)
        verify(mockEditor, times(1)).apply()
    }
}
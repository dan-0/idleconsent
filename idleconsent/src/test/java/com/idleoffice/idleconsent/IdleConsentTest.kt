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

package com.idleoffice.idleconsent

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IdleConsentTest {

    private lateinit var mockContext: Context
    private lateinit var mockPrefs: SharedPreferences

    private lateinit var ut: IdleConsent

    private val sharedPrefKey = "com.idleoffice.idleconsent.prefs"
    private val tocPrefKey = "$sharedPrefKey.TOC"
    private val privacyPrefKey = "$sharedPrefKey.PRIVACY"
    private val versionPrefKey = "$sharedPrefKey.VERSION"

    @Before
    fun setUp() {
        mockContext = mock()
        mockPrefs = mock()

        whenever(mockContext.getSharedPreferences(any(), any())).thenReturn(mockPrefs)
        whenever(mockPrefs.getBoolean(tocPrefKey, false)).thenReturn(true)
        whenever(mockPrefs.getBoolean(privacyPrefKey, false)).thenReturn(true)
        whenever(mockPrefs.getLong(versionPrefKey, -1)).thenReturn(0)

        ut = IdleConsent(mockContext)
    }

    @Test
    fun `test instantiation happy`() {
        // will be true due to forced in setup
        assertTrue(ut.hasUserAgreedToPrivacy())
        assertTrue(ut.hasUserAgreedToTerms())

        verify(mockContext, times(1)).getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
    }

    @Test
    fun `update user preferences happy path`() {
        val mockEditor: SharedPreferences.Editor = mock()
        whenever(mockPrefs.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putBoolean(any(), any())).thenReturn(mockEditor)

        ut.updateUserPreferences(isTermsAccepted = false, isPrivacyAccepted = false)

        verify(mockEditor, times(1)).putBoolean(tocPrefKey, false)
        verify(mockEditor, times(1)).putBoolean(privacyPrefKey, false)
        verify(mockEditor, times(1)).apply()
    }

    @Test
    fun `new version check happy`() {
        // Will be true because setup forces 0
        assertTrue(ut.isNewConsentVersion(1))
    }

    @Test
    fun `new version check equals`() {
        assertFalse(ut.isNewConsentVersion(0))
    }

    @Test
    fun `new version less than`() {
        assertFalse(ut.isNewConsentVersion(-2))
    }
}
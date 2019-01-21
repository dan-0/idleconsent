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

        ut = IdleConsent.getInstance(mockContext, testConfig)
    }

    @Test
    fun `getInstance happy path`() {
        assertTrue(ut.hasUserAgreedToPrivacy)
        assertTrue(ut.hasUserAgreedToTerms)

        // Try twice, ensure item isn't reinstantiated
        val consentTwo = IdleConsent.getInstance(mockContext, testConfig)

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
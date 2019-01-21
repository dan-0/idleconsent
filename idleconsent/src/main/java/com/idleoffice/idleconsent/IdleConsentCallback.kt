package com.idleoffice.idleconsent

abstract class IdleConsentCallback {
    abstract fun onAccept(hasUserAgreedToTerms: Boolean, hasUserAgreedToPrivacy: Boolean)
}
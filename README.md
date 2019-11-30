![pipeline](https://gitlab.com/dan-0/idleconsent/badges/master/build.svg?sanitize=true)
![Bintray](https://img.shields.io/bintray/v/idleoffice/IdleConsent/idleconsent.svg)


# IdleConsent
A simple library for adding a **privacy** and **terms and conditions** dialog
to your application.

IdleConsent allows you to specify a basic configuration and reliably retrieve acceptance for your 
(optional) privacy policy and your terms and conditions.

### Examples

<div>
<img height="600" src="/graphics/example.png" alt="Example 1"/>
<img height="600" src="/graphics/example2.png" alt="Example 2"/>
</div>

## Official Repo
Please use the [official IdleConsent repository on GitLab](https://gitlab.com/dan-0/idleconsent/)
for filing issues and PRs.

## Basic Usage

Simply specify a config and put it to use:

```kotlin
class ConsentActivity : AppCompatActivity() {

    private val idleConfig = IdleConsentConfig(
         "Terms and Privacy Notice",
         "We care about your experience and privacy using Super Test App. Please take a moment to read through and acknowledge our policies",
         "To ensure the best experience, we collect anonymized user data to inform us of crashes and how our users interact with the app.",
         listOf("GPS location", "Device information", "Usage statistics"),
         IdleInfoSource.Web(
             "Please see our full privacy policy.",
             Uri.parse("https://idleoffice-26abd.firebaseapp.com/quicklink/privacy_policy.html")
         ),
         false,
         "Please support Super Test App by allowing us to use your data as mentioned in the privacy policy",
         true,
         "In order to use Super Test App we require that you agree to our terms and conditions:",
         IdleInfoSource.Text(
             "See full terms and conditions",
             "This is just a test"
         ),
         version = 2
     )

    private val consentCallback = object : IdleConsentCallback() {
        override fun onAcknowledged(hasUserAgreedToTerms: Boolean, hasUserAgreedToPrivacy: Boolean) {
            if (hasUserAgreedToTerms) {
                startMainActivity()
                return
            }
        }
    }

    private fun startMainActivity() {
        Intent(this@ConsentActivity, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consent)
        val consent = IdleConsent.getInstance(this)
        if (!consent.hasUserAgreedToTerms || consent.isNewConsentVersion(2)) {
            consent.showConsentDialog(supportFragmentManager, consentCallback, idleConfig)
        } else {
            startMainActivity()
        }
    }

}
```

### Gradle
To import using gradle ensure you have the following in your main `build.gradle` file:
```groovy
    repositories {
        jcenter()
    }
```

Add this to your application level `build.gradle`:
```groovy
    dependencies {
        // Consent library
        implementation 'me.danlowe:idleconsent:[versionNumber]'
    }
```

### Configuration

IdleConsent aims to be customizable. You can spannable strings to the configurable text fields or specify a custom
information source for your terms and conditions and/or privacy statement.

To specify a custom information source, simply use `IdleInfoSource` and one of the follow types:
* `Web` to display your information in the devices default browser
* `Activity` to display a custom information activity
* `Text` to provide information in the form of a `CharSequence` displayed in a separate Activity
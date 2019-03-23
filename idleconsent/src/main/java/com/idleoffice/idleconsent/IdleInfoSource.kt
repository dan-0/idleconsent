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
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import com.idleoffice.idleconsent.IdleInfoSource.Activity
import com.idleoffice.idleconsent.view.InfoTextActivity
import kotlinx.android.parcel.Parcelize


/**
 * Provider for source of information that should be displayed to the user in a specified medium.
 *
 * You can use this class to provide your user access a terms of service or privacy policy via a raw text string ,
 * an external web address, or in a custom [Activity].
 */
sealed class IdleInfoSource(val linkText: CharSequence) : Parcelable {
    abstract fun showInfo(context: Context)

    /**
     * A [IdleInfoSource] that starts the given [activity] to display information to the user when the user
     * clicks on the provided [displayedLinkText] in the dialog
     */
    @Parcelize
    class Activity(
        private val displayedLinkText: CharSequence,
        private val activity: Class<out android.app.Activity>
    ) : IdleInfoSource(displayedLinkText) {
        override fun showInfo(context: Context) {
            Intent(context, activity).also {
                context.startActivity(it)
            }
        }
    }

    /**
     * A [IdleInfoSource] that launches the the given [url] to display information to the user when the user
     * clicks on the provided [displayedLinkText] in the dialog
     */
    @Parcelize
    class Web(private val displayedLinkText: CharSequence, private val url: Uri) : IdleInfoSource(displayedLinkText) {
        override fun showInfo(context: Context) {
            Intent(Intent.ACTION_VIEW).also {
                it.data = url
                context.startActivity(it)
            }
        }
    }

    /**
     * A [IdleInfoSource] that starts an [Activity], passing in the given [text] to it's [android.widget.TextView]
     * and [title] it it's [android.app.ActionBar] when the user clicks on the provided [displayedLinkText] in the dialog
     */
    @Parcelize
    class Text(
        private val displayedLinkText: CharSequence,
        private val text: CharSequence,
        private val title: CharSequence? = null
    ) : IdleInfoSource(displayedLinkText) {
        override fun showInfo(context: Context) {
            val textIntent = Intent(context, InfoTextActivity::class.java)
            textIntent.putExtra(InfoTextActivity.INFO_TEXT_KEY, text)
            textIntent.putExtra(InfoTextActivity.TITLE_TEXT_KEY, title)
            context.startActivity(textIntent)
        }
    }
}
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
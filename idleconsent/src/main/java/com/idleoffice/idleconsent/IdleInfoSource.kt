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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import com.idleoffice.idleconsent.view.InfoTextActivity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * Provider for source of information that should be displayed to the user in a specified medium
 */
@Parcelize
class IdleInfoSource private constructor(
    val linkText: CharSequence,
    private val infoType: InfoType,
    private val data: @RawValue Any
) : Parcelable {

    companion object {
        internal const val INFO_TEXT_KEY = "INFO_TEXT_KEY"
    }

    /**
     * A [IdleInfoSource] that starts the given [activity] to display information to the user when the user
     * clicks on the provided [linkText] in the dialog
     */
    constructor(linkText: CharSequence, activity: Class<out Activity>) : this(linkText, InfoType.ACTIVITY, activity)

    /**
     * A [IdleInfoSource] that launches the the given [url] to display information to the user when the user
     * clicks on the provided [linkText] in the dialog
     */
    constructor(linkText: CharSequence, url: Uri) : this(linkText, InfoType.WEB, url)

    /**
     * A [IdleInfoSource] that starts an [Activity], passing in the given [text] to it's [android.widget.TextView]
     * when the user clicks on the provided [linkText] in the dialog
     */
    constructor(linkText: CharSequence, text: CharSequence) : this(linkText, InfoType.TEXT, text)

    /**
     * Show the information related to the given [data]
     */
    internal fun showInfo(context: Context) {
        when (infoType) {
            InfoType.WEB -> {
                Intent(Intent.ACTION_VIEW).also {
                    it.data = data as Uri
                    context.startActivity(it)
                }
            }
            InfoType.TEXT -> {
                val textIntent = Intent(context, InfoTextActivity::class.java)
                textIntent.putExtra(INFO_TEXT_KEY, data as CharSequence)
                context.startActivity(textIntent)
            }
            InfoType.ACTIVITY -> {
                context.startActivity(Intent(context, data as Class<*>))
            }
        }
    }

    private enum class InfoType {
        WEB, TEXT, ACTIVITY
    }
}
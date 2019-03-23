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

package com.idleoffice.idleconsent.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.idleoffice.idleconsent.R
import kotlinx.android.synthetic.main.activity_info_text.*

internal class InfoTextActivity: AppCompatActivity() {
    companion object {
        internal const val INFO_TEXT_KEY = "INFO_TEXT_KEY"
        internal const val TITLE_TEXT_KEY = "TITLE_TEXT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_text)

        intent.getCharSequenceExtra(TITLE_TEXT_KEY)?.let {
            setSupportActionBar(infoTextToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = it
        } ?: run {
            supportActionBar?.hide()
            infoTextToolbar.visibility = View.GONE
        }

        infoText.text = intent.getCharSequenceExtra(INFO_TEXT_KEY)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
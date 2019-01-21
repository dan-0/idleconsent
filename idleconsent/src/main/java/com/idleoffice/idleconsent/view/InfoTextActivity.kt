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

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import com.idleoffice.idleconsent.IdleInfoSource
import com.idleoffice.idleconsent.R
import kotlinx.android.synthetic.main.activity_info_text.*

internal class InfoTextActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_text)
        setActionBar(infoTextToolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null

        infoText.text = intent.getCharSequenceExtra(IdleInfoSource.INFO_TEXT_KEY)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
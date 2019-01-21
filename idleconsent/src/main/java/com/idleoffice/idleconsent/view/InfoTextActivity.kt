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
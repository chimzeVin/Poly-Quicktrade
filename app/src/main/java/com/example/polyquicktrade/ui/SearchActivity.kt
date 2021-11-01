package com.example.polyquicktrade.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.polyquicktrade.R
import android.content.Intent
import android.app.SearchManager
import android.widget.Toast

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Get the intent, verify the action and get the query
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //TODO doMySearch(query);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show()
        }
    }
}
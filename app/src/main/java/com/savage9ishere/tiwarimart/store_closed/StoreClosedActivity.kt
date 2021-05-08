package com.savage9ishere.tiwarimart.store_closed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.savage9ishere.tiwarimart.R

class StoreClosedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_closed)

        findViewById<TextView>(R.id.opening_again_text).text = intent.getStringExtra("openingAgainTimeString")

        findViewById<Button>(R.id.store_close_button).setOnClickListener {
            finish()
        }
    }
}
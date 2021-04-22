package com.savage9ishere.tiwarimart.fragment_container

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.fragment_container.particular_category.ParticularCategoryFragment

class FragmentContainerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed();
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}
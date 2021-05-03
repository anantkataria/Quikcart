package com.savage9ishere.tiwarimart.main_flow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.ActivityMainBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import com.savage9ishere.tiwarimart.search.SearchActivity
import com.savage9ishere.tiwarimart.search.SearchViewModel
import com.savage9ishere.tiwarimart.search.SearchViewModelFactory
import com.savage9ishere.tiwarimart.search.sample.SampleActivity
import com.savage9ishere.tiwarimart.search.search_database.CategoryDao
import com.savage9ishere.tiwarimart.search.search_database.CategoryEntity
import com.savage9ishere.tiwarimart.search.search_database.CategoryWiseDao
import com.savage9ishere.tiwarimart.search.search_database.CategoryWiseEntity

class MainActivity : AppCompatActivity() {

    private lateinit var categoryDataSource: CategoryDao
    private lateinit var categoryWiseDataSource: CategoryWiseDao
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_cart, R.id.navigation_user
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val application = this.application
        categoryDataSource = CartItemsDatabase.getInstance(application).categoryDao
        categoryWiseDataSource = CartItemsDatabase.getInstance(application).categoryWiseDao

        val viewModelFactory = SearchViewModelFactory(categoryDataSource, categoryWiseDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

        val auth = Firebase.auth
        val user = auth.currentUser

        if (user == null) {
            auth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.container.visibility = View.VISIBLE
                    addCategoriesToSearchDatabase()
                    addCategoryWiseItemsToDatabase()
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.container.visibility = View.VISIBLE
        }
    }

    private fun addCategoriesToSearchDatabase() {
        val databaseRef = Firebase.database.reference
        databaseRef.child("categories").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModel.deleteAllCategoriesFromDatabase()
                for (s1: DataSnapshot in snapshot.children) {
                    val category: CategoryEntity? = s1.getValue(CategoryEntity::class.java)
                    viewModel.insertCategory(category)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun addCategoryWiseItemsToDatabase() {
        viewModel.deleteAllCategoryWiseItemsFromDatabase()
        val databaseRef = Firebase.database.reference
        databaseRef.child("categoryWiseItems").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val itemCategory = snapshot.key
                for (s1: DataSnapshot in snapshot.children) {
                    val item: Item? = s1.getValue(Item::class.java)
                    val discount = item!!.discount.toFloat()
                    val price = item.price.toFloat()
                    val priceWithDiscount = price -(price*discount/100)
                    val categoryWiseEntity =
                        CategoryWiseEntity(itemName = item!!.name, priceWithDiscount = priceWithDiscount,itemCategory = itemCategory!!, discount = item.discount, ratingCount = item.peopleRatingCount, ratingTotal = item.ratingTotal, price = item.price, imageUrl = item.photosUrl[0], inStock = item.inStock)
                    viewModel.insertCategoryItem(categoryWiseEntity)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.e("hithereMainActivity", snapshot.toString())
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.e("hithereMainActivity", snapshot.toString())
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
//                val intent = Intent(this, SearchActivity::class.java)
                val intent = Intent(this, SampleActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        //return super.onSupportNavigateUp()
        return navController.navigateUp()
    }
}
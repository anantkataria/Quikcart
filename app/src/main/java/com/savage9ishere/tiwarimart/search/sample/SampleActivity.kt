package com.savage9ishere.tiwarimart.search.sample

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.search.SearchViewModel
import com.savage9ishere.tiwarimart.search.SearchViewModelFactory
import com.savage9ishere.tiwarimart.search.search_database.CategoryDao
import com.savage9ishere.tiwarimart.search.search_database.CategoryWiseDao
import com.savage9ishere.tiwarimart.search.search_dialog.SearchDialogAdapter
import com.savage9ishere.tiwarimart.search.search_result.SearchResultActivity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SampleActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var intentt: Intent
    private lateinit var searchView : SearchView

    private var categories : ArrayList<String> = arrayListOf()
    private val totalSuggestion : ArrayList<String> = arrayListOf()
    private var itemNames : List<String> = listOf()

    private val disposable = CompositeDisposable()

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var categoryDataSource: CategoryDao
    private lateinit var categoryWiseDataSource: CategoryWiseDao
    private lateinit var viewModel: SearchViewModel

    private lateinit var adapter : SearchDialogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        recyclerView = findViewById(R.id.suggestions_recycler_view)
        adapter = SearchDialogAdapter{onClick(it)}
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val application = this.application
        categoryDataSource = CartItemsDatabase.getInstance(application).categoryDao
        categoryWiseDataSource = CartItemsDatabase.getInstance(application).categoryWiseDao

        val viewModelFactory = SearchViewModelFactory(categoryDataSource, categoryWiseDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)


        intentt = intent
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu2, menu)

        Log.e("hihihi", "this is running")
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu!!.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = "Search Products, Categories here..."
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)

        val editText : EditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.grey3))
        editText.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))

        val searchCloseIcon : ImageView = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        searchCloseIcon.setImageResource(R.drawable.ic_clear)

        val searchIcon : ImageView = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setImageResource(R.drawable.ic_search)

        searchView.maxWidth = Integer.MAX_VALUE

        observableFunction()

        return true
    }

    private fun observableFunction() {
        val observable = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val searchQuery: String = if (categories.contains(query)){
                            "categories $query"
                        } else {
                            "items $query"
                        }

                        totalSuggestion.clear()
                        val intent = Intent(this@SampleActivity, SearchResultActivity::class.java)
                        intent.action = Intent.ACTION_SEARCH
                        intent.putExtra("query", searchQuery)
                        searchView.setQuery(null, false)
                        startActivity(intent)
                        finish()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!emitter.isDisposed){
                            emitter.onNext(newText!!)
                        }
                        return true
                    }

                })
            }
        }).debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())

        observable.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onNext(t: String) {
                if (t.isNotEmpty()){
                    val searchQuery = "%$t%"
                    handler.post {
                        itemNames(searchQuery)
                        categoryNames(searchQuery)
                    }
                }
                else {
                    totalSuggestion.clear()
                }
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }

        })
    }

    private fun categoryNames(searchQuery: String) {
        viewModel.getCategoryByName(searchQuery).observe(this, {
            it?.let {
                categories = ArrayList(it)
                setTotalSuggestion()
            }
        })
    }

    private fun itemNames(searchQuery: String) {
        viewModel.getItemNamesByName(searchQuery).observe(this, {
            it?.let {
                val nameList = it
                itemNames = nameList
                setTotalSuggestion()
            }
        })
    }

    private fun setTotalSuggestion() {
        totalSuggestion.clear()
        totalSuggestion.addAll(itemNames)
        totalSuggestion.addAll(categories)
        adapter.submitList(totalSuggestion.toList())
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.search -> {
////                val intent = Intent(this, SearchActivity::class.java)
////                val intent = Intent(this, SampleActivity::class.java)
//                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()
////                startActivity(intent)
//                true
//            }
//            else -> {
//                super.onOptionsItemSelected(item)
//            }
//        }
//    }

    private fun onClick(i: Int) {
        val s = totalSuggestion[i]
        searchView.setQuery(s, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
        disposable.dispose()
    }
}
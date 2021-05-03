package com.savage9ishere.tiwarimart.search.search_result

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.search.SearchViewModel
import com.savage9ishere.tiwarimart.search.SearchViewModelFactory
import com.savage9ishere.tiwarimart.search.search_database.CategoryDao
import com.savage9ishere.tiwarimart.search.search_database.CategoryWiseDao
import com.savage9ishere.tiwarimart.search.search_database.CategoryWiseEntity
import com.savage9ishere.tiwarimart.search.search_dialog.SearchDialogActivity

class SearchResultActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter : SearchResultAdapter

    private lateinit var categoryDataSource: CategoryDao
    private lateinit var categoryWiseDataSource: CategoryWiseDao
    private lateinit var viewModel: SearchViewModel

    private lateinit var progressBar : ProgressBar
    private lateinit var searchBar : TextView
    private lateinit var searchBox : LinearLayout
    private lateinit var emptyView : TextView
    private lateinit var sortButton : Button
    private lateinit var filterButton : Button

    private var query : String = ""
    private var column : String = ""

    private val columnName = arrayOf(
        "category_of_item",
        "item_name",
        "discount",
        "rating",
        "price_with_discount",
        "in_stock",
        "item_id"
    )

    private val priceFiltersList : ArrayList<Pair<Float, Float>> = arrayListOf()
    private val discountFiltersList : ArrayList<Float> = arrayListOf()
    private val ratingFiltersList : ArrayList<Float> = arrayListOf()
    private val isStockFiltersList : ArrayList<Boolean> = arrayListOf()

    private val categoryWiseItems : ArrayList<CategoryWiseEntity> = arrayListOf()

    private var sortColumnName = columnName[6]
    private var order = "ASC"
    private val limit = 10
    private var offset = 0

    private var shouldLoad = true
    private var loading = false
    private var visibleItemCount = 0
    private var scrollOutTimes = 0
    private var totalItemCount = 0

    private lateinit var bottomSheetDialog : BottomSheetDialog
    private lateinit var bottomSheetDialogFilter : BottomSheetDialog
    private lateinit var sheetView : View
    private lateinit var filterSheetView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        recyclerView = findViewById(R.id.search_result_recycler_view)
        adapter = SearchResultAdapter { onClick(it) }
        recyclerView.adapter = adapter
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //todo add items to adapter
        adapter.submitList(categoryWiseItems.toList())

        val application = this.application
        categoryDataSource = CartItemsDatabase.getInstance(application).categoryDao
        categoryWiseDataSource = CartItemsDatabase.getInstance(application).categoryWiseDao

        val viewModelFactory = SearchViewModelFactory(categoryDataSource, categoryWiseDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

        progressBar = findViewById(R.id.progressbar)
        searchBar = findViewById(R.id.search_product_name)
        searchBox = findViewById(R.id.search)
        emptyView = findViewById(R.id.empty_view)

        searchBox.setOnClickListener {
            val intent = Intent(this, SearchDialogActivity::class.java)
            intent.action = Intent.ACTION_GET_CONTENT
            intent.putExtra("query", searchBar.text)
            startActivity(intent)
            finish()
        }

        val intent = intent
        if(Intent.ACTION_SEARCH == intent.action){
            val queryWithFlag = intent.getStringExtra("query")

            val tag = queryWithFlag!!.split(" ")[0]
            query = queryWithFlag.substring(queryWithFlag.indexOf(" ") + 1)
            searchBar.text = query

            column = if(tag == "categories"){
                columnName[0]
            } else {
                columnName[1]
            }

            applyFilters(
                query,
                priceFiltersList,
                discountFiltersList,
                ratingFiltersList,
                isStockFiltersList,
                column,
                sortColumnName,
                order,
                limit,
                offset
            )
        }

        fetchDataOnScrolling()

        sortButton = findViewById(R.id.sort_button)
        filterButton = findViewById(R.id.filter_button)

        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        sheetView = LayoutInflater.from(applicationContext).inflate(R.layout.sort_bottom_sheet, findViewById(R.id.bottom_sheet))

        bottomSheetDialogFilter = BottomSheetDialog(this, R.style.BottomSheetTheme)
        filterSheetView = LayoutInflater.from(applicationContext).inflate(R.layout.filter_bottom_sheet, findViewById(R.id.bottom_filter_sheet_main))

        filterButton.setOnClickListener {
            filterItems()
        }

        sortButton.setOnClickListener {
            sortItems()
        }
    }

    private fun sortItems() {
        val priceLow = sheetView.findViewById<RadioButton>(R.id.price_low)
        val priceHigh = sheetView.findViewById<RadioButton>(R.id.price_high)
        val relevance = sheetView.findViewById<RadioButton>(R.id.relevance)
        val popularity = sheetView.findViewById<RadioButton>(R.id.popularity)
        offset = 0
        shouldLoad = true

        priceLow.setOnClickListener{
            if (priceLow.isChecked){
                order = "ASC"
                categoryWiseItems.clear()

                sortColumnName = columnName[4]
                applyFilters(
                    query,
                    priceFiltersList,
                    discountFiltersList,
                    ratingFiltersList,
                    isStockFiltersList,
                    column,
                    sortColumnName,
                    order,
                    limit,
                    offset
                )

                bottomSheetDialog.dismiss()
            }
        }

        priceHigh.setOnClickListener {
            if (priceHigh.isChecked){
                order = "DESC"
                categoryWiseItems.clear()
                sortColumnName = columnName[4]

                applyFilters(
                    query,
                    priceFiltersList,
                    discountFiltersList,
                    ratingFiltersList,
                    isStockFiltersList,
                    column,
                    sortColumnName,
                    order,
                    limit,
                    offset
                )

                bottomSheetDialog.dismiss()
            }
        }

        relevance.setOnClickListener {
            if (relevance.isChecked){
                order = "ASC"
                categoryWiseItems.clear()
                sortColumnName = columnName[6]

                applyFilters(
                    query,
                    priceFiltersList,
                    discountFiltersList,
                    ratingFiltersList,
                    isStockFiltersList,
                    column,
                    sortColumnName,
                    order,
                    limit,
                    offset
                )

                bottomSheetDialog.dismiss()
            }
        }

        popularity.setOnClickListener {
            if (popularity.isChecked){
                order = "DESC"
                sortColumnName = columnName[3]
                categoryWiseItems.clear()

                applyFilters(
                    query,
                    priceFiltersList,
                    discountFiltersList,
                    ratingFiltersList,
                    isStockFiltersList,
                    column,
                    sortColumnName,
                    order,
                    limit,
                    offset
                )

                bottomSheetDialog.dismiss()
            }
        }

        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.show()
    }

    private fun filterItems() {
        val priceFilter = filterSheetView.findViewById<TextView>(R.id.price_filter)
        val discountFilter = filterSheetView.findViewById<TextView>(R.id.discount_filter)
        val availabilityFilter = filterSheetView.findViewById<TextView>(R.id.avaliability_filter)
        val ratingFilter = filterSheetView.findViewById<TextView>(R.id.rating_filter)

        val priceFilterLayout = filterSheetView.findViewById<LinearLayout>(R.id.price_filter_layout)
        val discountFilterLayout = filterSheetView.findViewById<LinearLayout>(R.id.discount_filter_layout)
        val availabilityFilterLayout = filterSheetView.findViewById<LinearLayout>(R.id.outof_stock_filter_layout)
        val ratingFilterLayout = filterSheetView.findViewById<LinearLayout>(R.id.customer_filter_layout)

        val applyFilter = filterSheetView.findViewById<Button>(R.id.apply_button)
        val clearFilter = filterSheetView.findViewById<TextView>(R.id.clear_filters)

        val below_250 = filterSheetView.findViewById<CheckBox>(R.id.below_250)
        val between_251_500 = filterSheetView.findViewById<CheckBox>(R.id.between_251_500)
        val between_501_1000 = filterSheetView.findViewById<CheckBox>(R.id.between_501_1000)
        val between_1001_2000 = filterSheetView.findViewById<CheckBox>(R.id.between_1001_2000)
        val between_2001_5000 = filterSheetView.findViewById<CheckBox>(R.id.between_2001_5000)
        val between_5001_10000 = filterSheetView.findViewById<CheckBox>(R.id.between_5001_10000)
        val above_10001 = filterSheetView.findViewById<CheckBox>(R.id.above_10001)

        val more_than_30 = filterSheetView.findViewById<CheckBox>(R.id.more_than_30)
        val more_than_40 = filterSheetView.findViewById<CheckBox>(R.id.more_than_40)
        val more_than_50 = filterSheetView.findViewById<CheckBox>(R.id.more_than_50)
        val more_than_60 = filterSheetView.findViewById<CheckBox>(R.id.more_than_60)
        val more_than_70 = filterSheetView.findViewById<CheckBox>(R.id.more_than_70)
        val below_30 = filterSheetView.findViewById<CheckBox>(R.id.below_30)

        val above_1 = filterSheetView.findViewById<CheckBox>(R.id.more_than_1)
        val above_2 = filterSheetView.findViewById<CheckBox>(R.id.more_than_2)
        val above_3 = filterSheetView.findViewById<CheckBox>(R.id.more_than_3)
        val above_4 = filterSheetView.findViewById<CheckBox>(R.id.more_than_4)

        val exclude_out_of_stock = filterSheetView.findViewById<CheckBox>(R.id.exclude_out_of_stock)

        priceFilter.setOnClickListener {
            priceFilter.setBackgroundColor(Color.WHITE)
            discountFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))
            availabilityFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))
            ratingFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))

            priceFilterLayout.visibility = View.VISIBLE
            discountFilterLayout.visibility = View.GONE
            availabilityFilterLayout.visibility = View.GONE
            ratingFilterLayout.visibility = View.GONE
        }

        discountFilter.setOnClickListener {
            discountFilter.setBackgroundColor(Color.WHITE)
            priceFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))
            availabilityFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))
            ratingFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))

            priceFilterLayout.visibility = View.GONE
            discountFilterLayout.visibility = View.VISIBLE
            availabilityFilterLayout.visibility = View.GONE
            ratingFilterLayout.visibility = View.GONE
        }

        availabilityFilter.setOnClickListener {
            availabilityFilter.setBackgroundColor(Color.WHITE)
            discountFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))
            priceFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))
            ratingFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))

            priceFilterLayout.visibility = View.GONE
            discountFilterLayout.visibility = View.GONE
            availabilityFilterLayout.visibility = View.VISIBLE
            ratingFilterLayout.visibility = View.GONE
        }

        ratingFilter.setOnClickListener {
            ratingFilter.setBackgroundColor(Color.WHITE)
            discountFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))
            availabilityFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))
            priceFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.lightest_grey))

            priceFilterLayout.visibility = View.GONE
            discountFilterLayout.visibility = View.GONE
            availabilityFilterLayout.visibility = View.GONE
            ratingFilterLayout.visibility = View.VISIBLE
        }

        applyFilter.setOnClickListener {
            offset = 0
            val isFiltered = true
            shouldLoad = true
            categoryWiseItems.clear()
            priceFiltersList.clear()
            discountFiltersList.clear()
            isStockFiltersList.clear()
            ratingFiltersList.clear()

            if (below_250.isChecked){
                val p = Pair(0F, 250F)
                priceFiltersList.add(p)
            }
            if (between_251_500.isChecked){
                val p = Pair(251F, 500F)
                priceFiltersList.add(p)
            }
            if (between_501_1000.isChecked){
                val p = Pair(501F, 1000F)
                priceFiltersList.add(p)
            }
            if (between_1001_2000.isChecked){
                val p = Pair(1001F, 2000F)
                priceFiltersList.add(p)
            }
            if (between_2001_5000.isChecked){
                val p = Pair(2001F, 5000F)
                priceFiltersList.add(p)
            }
            if (between_5001_10000.isChecked){
                val p = Pair(5001F, 10000F)
                priceFiltersList.add(p)
            }
            if (above_10001.isChecked){
                val p =Pair(10001F, Float.MAX_VALUE)
                priceFiltersList.add(p)
            }

            if(more_than_70.isChecked){
                discountFiltersList.add(70F)
            }
            if(more_than_60.isChecked){
                discountFiltersList.add(60F)
            }
            if(more_than_50.isChecked){
                discountFiltersList.add(50F)
            }
            if(more_than_40.isChecked){
                discountFiltersList.add(40F)
            }
            if(more_than_30.isChecked){
                discountFiltersList.add(30F)
            }
            if(below_30.isChecked){
                discountFiltersList.add(29F)
            }

            if(above_1.isChecked){
                ratingFiltersList.add(1F)
            }
            if(above_2.isChecked){
                ratingFiltersList.add(2F)
            }
            if(above_3.isChecked){
                ratingFiltersList.add(3F)
            }
            if(above_4.isChecked){
                ratingFiltersList.add(4F)
            }

            if (exclude_out_of_stock.isChecked){
                isStockFiltersList.add(false)
            }

            applyFilters(
                query,
                priceFiltersList,
                discountFiltersList,
                ratingFiltersList,
                isStockFiltersList,
                column,
                sortColumnName,
                order,
                limit,
                offset
            )

            bottomSheetDialogFilter.dismiss()
        }

        clearFilter.setOnClickListener {
            offset = 0
            bottomSheetDialogFilter.dismiss()
            categoryWiseItems.clear()
            priceFiltersList.clear()
            discountFiltersList.clear()
            ratingFiltersList.clear()
            isStockFiltersList.clear()

            applyFilters(
                query,
                priceFiltersList,
                discountFiltersList,
                ratingFiltersList,
                isStockFiltersList,
                column,
                sortColumnName,
                order,
                limit,
                offset
            )

            allCheckBoxFalse()
        }

        bottomSheetDialogFilter.setContentView(filterSheetView)
        bottomSheetDialogFilter.show()
    }

    private fun allCheckBoxFalse() {
        filterSheetView = LayoutInflater.from(applicationContext).inflate(R.layout.filter_bottom_sheet, findViewById(R.id.bottom_filter_sheet_main))
    }

    private fun fetchDataOnScrolling() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    loading = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = layoutManager.childCount
                totalItemCount = layoutManager.itemCount
                scrollOutTimes = layoutManager.findFirstVisibleItemPosition()

                if (shouldLoad && loading && scrollOutTimes + visibleItemCount >= totalItemCount) {
                    loading = false
                    offset += limit
                    applyFilters(
                        query,
                        priceFiltersList,
                        discountFiltersList,
                        ratingFiltersList,
                        isStockFiltersList,
                        column,
                        sortColumnName,
                        order,
                        limit,
                        offset
                    )
                }
            }
        })
    }

    private fun applyFilters(
        query: String,
        priceFiltersList: ArrayList<Pair<Float, Float>>,
        discountFiltersList: ArrayList<Float>,
        ratingFiltersList: ArrayList<Float>,
        stockFiltersList: ArrayList<Boolean>,
        column: String,
        sortColumnName: String,
        order: String,
        limit: Int,
        offset: Int
    ) {
        viewModel.applyFilters(
            "%$query%",
            priceFiltersList,
            discountFiltersList,
            ratingFiltersList,
            stockFiltersList,
            column,
            sortColumnName,
            order,
            limit,
            offset
        ).observe(this, {
            it?.let {
                fetchDataFromRoom(it, limit, offset)
            }
        })
    }

    private fun fetchDataFromRoom(items: List<CategoryWiseEntity>, limit: Int, offset: Int) {
        if(items.size < limit){
            shouldLoad = false
        }
        var index = offset
        for (i in items.indices){
            if(categoryWiseItems.size >= index){
                categoryWiseItems.add(items[i])
            }
            else {
                categoryWiseItems[index] = items[i]
            }
            index++
        }
        if (items.isEmpty() && categoryWiseItems.size == 0){
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        }
        else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
        adapter.submitList(categoryWiseItems.toList())
//        adapter.notifyDataSetChanged()
    }

    private fun onClick(it: CategoryWiseEntity) {
        Toast.makeText(this, "YO IT CLICKED", Toast.LENGTH_SHORT).show()
    }
}
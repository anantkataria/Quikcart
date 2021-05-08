package com.savage9ishere.tiwarimart.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.savage9ishere.tiwarimart.search.search_database.CategoryDao
import com.savage9ishere.tiwarimart.search.search_database.CategoryEntity
import com.savage9ishere.tiwarimart.search.search_database.CategoryWiseDao
import com.savage9ishere.tiwarimart.search.search_database.CategoryWiseEntity
import kotlinx.coroutines.launch

class SearchViewModel(
    private val categoryDatabase: CategoryDao,
    private val categoryWiseDatabase: CategoryWiseDao
) : ViewModel() {
    fun deleteAllCategoriesFromDatabase() {
        viewModelScope.launch {
            categoryDatabase.deleteAllCategories()
        }
    }

    fun insertCategory(category: CategoryEntity?) {
        viewModelScope.launch {
            categoryDatabase.insert(category!!)
        }
    }

    fun insertCategoryItem(categoryWiseEntity: CategoryWiseEntity) {
        viewModelScope.launch {
            categoryWiseDatabase.insert(categoryWiseEntity)
        }
    }

    fun deleteAllCategoryWiseItemsFromDatabase() {
        viewModelScope.launch {
            categoryWiseDatabase.deleteAllItems()
        }
    }

    fun getItemNamesByName(query: String): LiveData<List<String>> {
        return categoryWiseDatabase.getItemNamesByName(query)
    }

    fun getCategoryByName(query: String): LiveData<List<String>> {
        return categoryDatabase.getCategoryByName(query)
    }

    fun applyFilters(
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
    ) : LiveData<List<CategoryWiseEntity>>{
        var queryy : SupportSQLiteQuery
        var filter = ""

        for (i in priceFiltersList.indices){
            filter += if (i == 0) {
                " and (("
            } else {
                " or ( "
            }

            val p : Pair<Float, Float> = priceFiltersList[i]
            val minvalue = p.first.toString()
            val maxValue = p.second.toString()
            filter += "price_with_discount >= $minvalue and price_with_discount <= $maxValue )"
            if (i == priceFiltersList.size-1){
                filter +=")"
            }
        }

        for (i in discountFiltersList.indices){
            filter += if(i == 0){
                " and (("
            } else {
                " or ("
            }

            val f = discountFiltersList[i]

            filter += if (f == 29F){
                "discount <= $f ) "
            } else {
                "discount >= $f ) "
            }

            if (i == discountFiltersList.size - 1){
                filter += ")"
            }
        }

        for (i in ratingFiltersList.indices){
            filter += if(i == 0){
                " and (("
            } else {
                " or ("
            }

            val f = ratingFiltersList[i].toString()

            filter += "rating >= $f ) "

            if (i == ratingFiltersList.size - 1){
                filter += ")"
            }
        }

        for (i in stockFiltersList.indices){
            val f = stockFiltersList[i].toString()
            filter += " and (( in_stock = 1 ))"
        }

        val queryString = "SELECT * FROM category_wise_items_table WHERE ( $column LIKE '$query' $filter ) ORDER BY $sortColumnName $order LIMIT $limit OFFSET $offset"
        queryy = SimpleSQLiteQuery(queryString)
        return categoryWiseDatabase.filterProducts(queryy)
    }
}
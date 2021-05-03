package com.savage9ishere.tiwarimart.search.search_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories_table")
class CategoryEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    val categoryId : Long = 0L,

    @ColumnInfo(name = "category_name")
    val name : String = "",

)
package com.algo1.myshoppinglist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category): Long

    @Insert
    fun insertItem(item: ShoppingItem): Long

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM shopping_items")
    fun getAllItems(): LiveData<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE categoryId = :categoryId ORDER BY isChecked ASC")
    fun getItemsByCategory(categoryId: Int): LiveData<List<ShoppingItem>>

    @Update
    fun updateItem(item: ShoppingItem): Int // Returns the number of rows affected
}


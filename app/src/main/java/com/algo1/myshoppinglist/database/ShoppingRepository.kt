package com.algo1.myshoppinglist.database

import androidx.lifecycle.LiveData

class ShoppingRepository(private val dao: ShoppingDao) {
    val allCategories: LiveData<List<Category>> = dao.getAllCategories()

    fun getItemsByCategory(categoryId: Int): LiveData<List<ShoppingItem>> =
        dao.getItemsByCategory(categoryId)

    fun getAllItems(): LiveData<List<ShoppingItem>> {
        return dao.getAllItems()
    }


    fun insertCategory(category: Category) = dao.insertCategory(category)
    fun insertItem(item: ShoppingItem) = dao.insertItem(item)
    fun updateItem(item: ShoppingItem) = dao.updateItem(item)
}

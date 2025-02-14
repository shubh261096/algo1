package com.algo1.myshoppinglist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algo1.myshoppinglist.database.Category
import com.algo1.myshoppinglist.database.ShoppingItem
import com.algo1.myshoppinglist.database.ShoppingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingViewModel(private val repository: ShoppingRepository) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val allCategories: LiveData<List<Category>> = repository.allCategories

    fun getItemsByCategory(categoryId: Int): LiveData<List<ShoppingItem>> {
        return repository.getItemsByCategory(categoryId)
    }

    fun getAllItems(): LiveData<List<ShoppingItem>> {
        return repository.getAllItems()
    }


    fun insertCategory(category: Category) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertCategory(category)
            }
        }
    }

    fun insertItem(item: ShoppingItem) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertItem(item)
            }
        }
    }

    fun updateItem(item: ShoppingItem) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateItem(item)
            }
        }
    }
}

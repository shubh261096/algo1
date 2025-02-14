package com.algo1.myshoppinglist

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.algo1.myshoppinglist.database.Category
import com.algo1.myshoppinglist.database.ShoppingDatabase
import com.algo1.myshoppinglist.database.ShoppingItem
import com.algo1.myshoppinglist.database.ShoppingRepository
import com.algo1.myshoppinglist.databinding.ActivityMainBinding
import com.algo1.myshoppinglist.view.ShoppingAdapter
import com.algo1.myshoppinglist.viewModel.ShoppingViewModel
import com.algo1.myshoppinglist.viewModel.ShoppingViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingViewModel
    private lateinit var adapter: ShoppingAdapter
    private lateinit var completedAdapter: ShoppingAdapter
    private lateinit var categoryAdapter: ArrayAdapter<String>
    private var selectedCategoryId: Int = -1
    private var selectedCategoryName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = ShoppingDatabase.getDatabase(this).shoppingDao()
        val repository = ShoppingRepository(dao)
        val factory = ShoppingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ShoppingViewModel::class.java]


        // Adapter for active items (first RecyclerView)
        adapter = ShoppingAdapter { item ->
            viewModel.updateItem(item)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Adapter for completed items (second RecyclerView)
        completedAdapter = ShoppingAdapter { item ->
            viewModel.updateItem(item)
        }
        binding.recyclerViewCompleted.adapter = completedAdapter
        binding.recyclerViewCompleted.layoutManager = LinearLayoutManager(this)


        categoryAdapter = ArrayAdapter(this, R.layout.spinner_item, mutableListOf())
        binding.spinnerCategories.adapter = categoryAdapter

        binding.spinnerCategories.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCategory = viewModel.allCategories.value?.get(position)
                    selectedCategory?.let {
                        selectedCategoryId = it.id
                        selectedCategoryName = it.name
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


        viewModel.allCategories.observe(this) { categories ->
            categoryAdapter.clear()
            categoryAdapter.addAll(categories.map { it.name })

            if (categories.isNotEmpty()) {
                selectedCategoryId = categories.last().id
                selectedCategoryName = categories.last().name
                binding.spinnerCategories.setSelection(categories.size - 1)
            }
        }




        binding.btnAddCategory.setOnClickListener {
            val category = Category(name = binding.etCategory.text.toString())
            viewModel.insertCategory(category)
        }

        binding.btnAddItem.setOnClickListener {
            val itemName = binding.etItem.text.toString()
            if (itemName.isNotEmpty() && selectedCategoryName.isNotEmpty()) {
                val aisleNumber = selectedCategoryName.first().uppercase()

                val item = ShoppingItem(
                    name = itemName,
                    aisle = aisleNumber,
                    categoryId = selectedCategoryId
                )
                viewModel.insertItem(item)
            }
        }


        binding.ivFilter.setOnClickListener {
            showSortingDialog()
        }

        observeAllItems()
    }

    private var currentSortOption: Int = -1 // 0 for Alphabetical, 1 for Aisle

    private fun showSortingDialog() {
        val options = arrayOf("Alphabetically", "Aisle")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sort Items")
            .setSingleChoiceItems(options, currentSortOption) { dialog, which ->
                currentSortOption = which
                observeAllItems()
                dialog.dismiss()
            }
            .show()
    }

    private fun observeAllItems() {
        viewModel.getAllItems().observe(this) { items ->
            val sortedItems = if (currentSortOption == -1) {
                items
            } else {
                when (currentSortOption) {
                    0 -> items.sortedBy { it.name } // Sort Alphabetically
                    1 -> items.sortedBy { it.aisle } // Sort by Aisle Number
                    else -> items
                }
            }

            adapter.submitList(sortedItems.filter { !it.isChecked }) // Active items
            completedAdapter.submitList(items.filter { it.isChecked }) // Completed items
        }
    }


}

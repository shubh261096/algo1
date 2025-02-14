package com.algo1.myshoppinglist.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val aisle: String,
    val categoryId: Int,
    var isChecked: Boolean = false,
)

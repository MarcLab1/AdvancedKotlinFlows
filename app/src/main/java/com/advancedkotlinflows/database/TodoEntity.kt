package com.advancedkotlinflows.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.advancedkotlinflows.ui.Todo

@Entity(tableName = "Todo")
data class TodoEntity(
    @PrimaryKey val id: String,
    val description: String,
    val date: Long,
    val completed: Boolean
)

fun Todo.toTodoEntity(): TodoEntity {
    return TodoEntity(this.id, this.description, this.date, this.completed)
}

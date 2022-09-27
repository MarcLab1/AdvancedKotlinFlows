package com.advancedkotlinflows.ui

import com.advancedkotlinflows.database.TodoEntity

data class Todo(val id : String, val description : String, val date : Long, val completed : Boolean)

fun TodoEntity.toTodo(): Todo
{
    return Todo(this.id, this.description, this.date, this.completed)
}



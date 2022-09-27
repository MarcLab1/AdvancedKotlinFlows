package com.advancedkotlinflows.ui.home

import com.advancedkotlinflows.ui.Todo

data class HomeState(
    val todos: List<Todo> = emptyList(),
    val todo: Todo? = null,
    val todosCompleted: List<Todo> = emptyList(),
    val todosNotCompleted : List<Todo> = emptyList(),
    val date : Long = System.currentTimeMillis()
)

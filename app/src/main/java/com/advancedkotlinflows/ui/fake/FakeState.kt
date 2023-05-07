package com.advancedkotlinflows.ui.fake

import com.advancedkotlinflows.ui.Todo

data class FakeState(
    val todos: List<Todo> = emptyList(),
)
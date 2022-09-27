package com.advancedkotlinflows.repository

import com.advancedkotlinflows.database.TodoDatabase
import com.advancedkotlinflows.database.toTodoEntity
import com.advancedkotlinflows.ui.Todo
import com.advancedkotlinflows.ui.toTodo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TodoRepository {
    fun getTodos() : Flow<List<Todo>>
    fun getTodos2(completed: Boolean) : Flow<List<Todo>>
    fun getTodo(id : Int) : Flow<Todo>
    suspend fun insertTodo(todo : Todo)
    suspend fun deleteTodo(todo : Todo)
    suspend fun updateTodo(todo : Todo)
}

class TodoRepository_Impl(private val db : TodoDatabase) : TodoRepository
{
    private val todoDao = db.todoDao()
    override fun getTodos(): Flow<List<Todo>>
    {
        return todoDao.getTodos().map { it.map { it.toTodo() } }
    }

    override fun getTodos2(completed : Boolean): Flow<List<Todo>>
    {
        return todoDao.getTodos2(completed).map { it.map { it.toTodo() } }
    }

    override fun getTodo(id: Int): Flow<Todo> {
        return todoDao.getTodo(id).map { it.toTodo() }
    }

    override suspend fun insertTodo(todo: Todo) {
        return todoDao.insertTodo(todo.toTodoEntity())
    }

    override suspend fun deleteTodo(todo: Todo) {
        return todoDao.deleteTodo(todo.toTodoEntity())
    }

    override suspend fun updateTodo(todo: Todo) {
        return todoDao.updateTodo(todo.toTodoEntity())
    }
}
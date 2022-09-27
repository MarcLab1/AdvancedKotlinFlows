package com.advancedkotlinflows.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//we don't need no stinking repository
//Gabor recommends this pattern instead of the repository pattern

interface ITodoDao {

    fun getTodos(): Flow<List<TodoEntity>>
    fun getTodos2(completed : Boolean): Flow<List<TodoEntity>>
    fun getTodosNotCompleted(): Flow<List<TodoEntity>>
    fun getTodo(id : Int): Flow<TodoEntity>
    suspend fun insertTodo(todoEntity: TodoEntity)
    suspend fun deleteTodo(todoEntity: TodoEntity)
    suspend fun updateTodo(todoEntity: TodoEntity)
}
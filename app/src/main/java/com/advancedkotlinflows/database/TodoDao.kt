package com.advancedkotlinflows.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao : ITodoDao {

    @Query("SELECT * FROM Todo")
    override fun getTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM Todo where completed =:completed")
    override fun getTodos2(completed : Boolean): Flow<List<TodoEntity>>

    @Query("SELECT * FROM Todo where completed = 0")
    override fun getTodosNotCompleted(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM Todo where id =:id")
    override fun getTodo(id : Int): Flow<TodoEntity>

    @Insert
    override suspend fun insertTodo(todoEntity: TodoEntity)

    @Delete
    override suspend fun deleteTodo(todoEntity: TodoEntity)

    @Update
    override suspend fun updateTodo(todoEntity: TodoEntity)
}
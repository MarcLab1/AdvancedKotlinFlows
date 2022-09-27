package com.advancedkotlinflows.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.advancedkotlinflows.database.ITodoDao
import com.advancedkotlinflows.database.TodoDatabase
import com.advancedkotlinflows.repository.TodoRepository
import com.advancedkotlinflows.repository.TodoRepository_Impl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideString() = "bababab"

    @Singleton
    @Provides
    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
        return TodoRepository_Impl(db)
    }

    @Singleton
    @Provides
    fun provideTodoDatabase(application: Application): TodoDatabase {
        return Room.databaseBuilder(application, TodoDatabase::class.java, "todo_database").build()
    }

    @Singleton
    @Provides
    fun provideTodoDao(todoDatabase: TodoDatabase): ITodoDao {
        return todoDatabase.todoDao()
    }
}
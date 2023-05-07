package com.advancedkotlinflows.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancedkotlinflows.database.ITodoDao
import com.advancedkotlinflows.repository.TodoRepository
import com.advancedkotlinflows.ui.Todo
import com.advancedkotlinflows.ui.toTodo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val dao: ITodoDao,
) : ViewModel() {

    val todos: MutableState<List<Todo>> = mutableStateOf(emptyList())
    val todosCompleted: MutableState<List<Todo>> = mutableStateOf(emptyList())
    val todosNotCompleted: MutableState<List<Todo>> = mutableStateOf(emptyList())
    val todo: MutableState<Todo?> = mutableStateOf(null)

    private val _homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()
    var todoFlow: Flow<Todo>? = null
    var list : MutableList<Int> = mutableListOf()

    init {
        getTodos10()
    }

    fun insertTodo(text: String) {
        viewModelScope.launch {
            todoRepository.insertTodo(
                Todo(
                    UUID.randomUUID().toString(),
                    text,
                    System.currentTimeMillis(),
                    if (System.currentTimeMillis() % 2 == 0L) true else false
                )
            )
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }

    fun getTodos() {

        todoRepository.getTodos()
            .mapLatest { it ->
                todos.value = it
                todo.value = it.last()
                it
                //.distinctUntilChanged() -- don't think it's necessary to use this
            }

            .launchIn(viewModelScope)
    }

    fun getTodos2() {
        _homeState
            .map { viewState ->
                viewState.date
            }
            .distinctUntilChanged()
            .flatMapLatest {
                val todoFlow = todoRepository.getTodos()
                todoFlow
            }
            .onEach {
                val newList = it.filter { it.description.contains("a") }
                _homeState.value = _homeState.value.copy(todos = newList, todo = it.lastOrNull())
            }
            .launchIn(viewModelScope)
    }

    fun getTodos3() {
        viewModelScope.launch {
            todoRepository.getTodos().collectLatest {
                todos.value = it.map { todo -> todo }?.filter { it.description.length > 5 }.onEach {
                }
            }
        }
    }

    fun getTodos4() {
        viewModelScope.launch {
            todoRepository.getTodos().collectLatest { todoFlow = it.asFlow() }
        }
    }

    fun getTodos5() {
        todoRepository.getTodos()
            .distinctUntilChanged()
            .onEach { todos.value = it }
            .launchIn(viewModelScope)
    }

    fun getTodos6() {
        todoRepository.getTodos()
            .distinctUntilChanged()
            .mapLatest {
                todos.value = it.map { it.copy(description = it.description.uppercase()) }
            }
            .launchIn(viewModelScope)
    }

    fun getTodos7() {
        viewModelScope.launch {
            todoRepository.getTodos2(true).collectLatest {
                todosCompleted.value = it.map { it.copy(description = it.description.uppercase()) }
            }
        }

        viewModelScope.launch {
            todoRepository.getTodos2(false).collectLatest {
                todosNotCompleted.value =
                    it.map { it.copy(description = it.description.lowercase()) }
            }
        }
    }

    //observing from 2 different views
    //race conditions concerns?
    fun getTodos8() {
        getCompletedTodos()
        getNotCompletedTodos()
    }

    //Gabor zomg
    //queries are most optimal in the db layer, so we have 2 flows and combine them here
    fun getTodos9() {

        dao.getTodos2(true).combine(dao.getTodos2(false))
        { completedTodoFlow, notCompletedTodoFlow ->
            todosCompleted.value = completedTodoFlow.map { it.toTodo() }
            todosNotCompleted.value = notCompletedTodoFlow.map { it.toTodo() }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, "hello")
    }

    //does this work with MutableStateFlow(ViewState) ???
    //Gabor says it kills reactivity because you mutate the same property with combine
    //and there is the potential of data overwriting itself
    //also MutableStateFlow prevents you from using combine + stateIn ???
    //stateIn is generally used to improve performance
    fun getTodos10() {

        _homeState
            .map { viewState ->
                //viewState.todosCompleted //to viewState.todosNotCompleted
                viewState.date
            }
            .distinctUntilChanged()
            .flatMapLatest {

                todoRepository.getTodos2(true).combine(todoRepository.getTodos2(false)){ f1,f2->
                    (f1 to f2)
                }
            }
            .onEach { (r1, r2)->
                _homeState.value = _homeState.value.copy(todosCompleted = r1, todosNotCompleted = r2)
            }.launchIn(viewModelScope)
            //.stateIn(viewModelScope, SharingStarted.Eagerly, "hello?")
    }

    private fun getNotCompletedTodos() {
        viewModelScope.launch {
            dao.getTodos2(false).collectLatest {
                todosNotCompleted.value =
                    it.map { it.toTodo() }.map { it.copy(description = it.description.lowercase()) }
            }
        }
    }

    private fun getCompletedTodos() {
        viewModelScope.launch {
            dao.getTodos2(true).collectLatest {
                todosCompleted.value =
                    it.map { it.toTodo() }.map { it.copy(description = it.description.uppercase()) }
            }
        }
    }

    fun getTodo(id: Int) {
        viewModelScope.launch {
            todoRepository.getTodo(id).collectLatest { it ->
                todo.value = it
            }
        }
    }
}
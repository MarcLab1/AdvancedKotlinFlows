package com.advancedkotlinflows.ui.fake

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.advancedkotlinflows.database.ITodoDao
import com.advancedkotlinflows.repository.TodoRepository
import com.advancedkotlinflows.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

@HiltViewModel
class FakeViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val dao: ITodoDao,
) : ViewModel() {

    private val _fakeState: MutableStateFlow<FakeState> = MutableStateFlow(FakeState())
    val fakeState: StateFlow<FakeState> = _fakeState.asStateFlow()
    var list: MutableList<Int> = mutableListOf()

    var job: Job? = null

    init {
        //flowsAreFun()
        //flowsAreFun2()
        // startFlowCollection()
        startFlowCollection3()
    }

    fun stopFlowCollection() {
        job?.cancel()
    }

    fun flowsAreFun(): Unit {
        (1 until 4).map {
            list.add(it)
        }
        viewModelScope.launch {
            flowOf<Int>(1, 2, 3).collect {
                println("flow on= " + it)
            }

            flow<Int> { list.map { emit(it) } }.collect {
                println("flow= " + it)
            }

            list.asFlow().collect {
                println("as flow=" + it)
            }

            flow<Int> { emitAll(list.asReversed().asFlow()) }.collect {
                println("emit all=" + it)
            }
        }
    }

    fun flowsAreFun2() {
        viewModelScope.launch {
            todoRepository.getTodos().collectLatest {
                it.map { it2 -> Log.i("TAG", it2.toString()) }
            }
        }
    }

    fun flowsAreFun3() {
        (7 until 11).map {
            list.add(it)
        }


        val flow1 = flow<Int> { list.map { emit(it) } }
        flow1.launchIn(viewModelScope)

        val scope = CoroutineScope(EmptyCoroutineContext)
        val flow2 = flow<Int> { list.map { emit(it) } }


        flow2.onEach { println(it) }
            .launchIn(scope)

    }

    fun startFlowCollection() {

        job = todoRepository.getTodos()
            .onStart {
                //loading == true
            }
            .onCompletion { cause: Throwable? ->
                if (cause != null)
                    println(cause.localizedMessage?.toString())

                //loading == false
            }
            .onEach {
                _fakeState.value = _fakeState.value.copy(todos = it)

            }
            .launchIn(viewModelScope)
    }

    fun startFlowCollection2() {

        todoRepository.getTodos()
            .onStart {
                //loading == true
            }
            .map { list ->
                val newList = list.filter { todo -> todo.completed == true }
                _fakeState.value = _fakeState.value.copy(todos = newList)
            }
            .onCompletion { cause: Throwable? ->
                if (cause != null)
                    println(cause.localizedMessage?.toString())
            }
            .launchIn(viewModelScope)
    }

    fun startFlowCollection3() {
        todoRepository.getTodos()
            .flatMapLatest { list ->
                _fakeState.value =
                    _fakeState.value.copy(todos = list.filter { it.completed == true })
                todoRepository.getTodos()
            }
            .onEach { Log.i("TAG", it.toString()) }
            .buffer()
            .launchIn(viewModelScope)
    }
}
package com.advancedkotlinflows.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.advancedkotlinflows.ui.Todo

@Composable
fun HomePage(vm: HomeViewModel = viewModel()) {
    var text by remember { mutableStateOf("") }
    var homeState = vm.homeState.collectAsState()

    //Gabor says to put this logic in the DAO to make the streams reactive
  /*  val (completed, notCompleted) = homeState.value.todos.partition {
        it.completed
    }*/

    Column()
    {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center)
        {
            homeState.value.todo?.description?.let { Text(it) }
        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth())
        {
            TextField(value = text, onValueChange = { text = it })
            Button(onClick = {
                vm.insertTodo(text)
                text = ""
            })
            {
                Icon(Icons.Default.Add, "")
            }
        }


        LazyColumn()
        {
            item{
                Text("Completed", style = MaterialTheme.typography.h6)
            }
            itemsIndexed(homeState.value.todosCompleted)
            //itemsIndexed(vm.todosCompleted.value)
            { index, todo ->
                ShowTodo(index, todo)
            }
            item{
                Spacer(modifier = Modifier.padding(5.dp))
            }
            item{
                Text("Not Completed", style = MaterialTheme.typography.h6)
            }
            itemsIndexed(homeState.value.todosNotCompleted)
            //itemsIndexed(vm.todosNotCompleted.value)
            { index, todo ->
                ShowTodo(index, todo)
            }
        }
    }
}

@Composable
fun ShowTodo(index: Int, todo: Todo)
{
    Card(elevation = 3.dp, modifier = Modifier.fillMaxWidth().padding(4.dp))
    {
        Row() {
            Text(todo.description, modifier = Modifier.padding(end = 5.dp))
            Text(todo.completed.toString())
        }

    }
}
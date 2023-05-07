package com.advancedkotlinflows.ui.fake

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.advancedkotlinflows.ui.home.HomeViewModel
import com.advancedkotlinflows.ui.home.ShowTodo


@Composable
fun FakePage(vm: FakeViewModel = viewModel()) {
    val fakeState = vm.fakeState.collectAsState()

    Column()
    {
        Button(onClick = { vm.startFlowCollection() }) {
            Text("Start Collection")
        }
        Button(onClick = { vm.stopFlowCollection() }) {
            Text("Stop Collection")
        }

        LazyColumn()
        {

            itemsIndexed(fakeState.value.todos)
            //itemsIndexed(vm.todosCompleted.value)
            { index, todo ->
                ShowTodo(index, todo)
            }
        }
    }
}

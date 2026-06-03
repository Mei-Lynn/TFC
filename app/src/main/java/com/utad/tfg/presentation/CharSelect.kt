package com.utad.tfg.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.utad.tfg.remote.DndResource

@Composable
fun CharSelectScreen() {
    val vm = hiltViewModel<MainViewModel>()
    val races by vm.races.collectAsState()
    val classes by vm.classes.collectAsState()
    var selectMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { CharSelectTopbar() },
        floatingActionButton = { if (selectMode) RemoveCharacterButton() else AddCharacterButton() },
    ) { paddingValues ->

        LazyColumn(modifier = Modifier.padding(paddingValues).padding(16.dp)) {

        }
    }
}

@Composable
fun CharacterCard() {
    Card() { }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharSelectTopbar() {
    TopAppBar(
        title = { Text("") },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}

@Composable
fun AddCharacterButton() {
    FloatingActionButton(
        onClick = { /* TODO */ },
        shape = CircleShape
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add character")
    }
}

@Composable
fun RemoveCharacterButton() {
    FloatingActionButton(
        onClick = { /* TODO */ },
        shape = CircleShape
    ) {
        Icon(Icons.Default.Delete, contentDescription = "Remove character")
    }
}

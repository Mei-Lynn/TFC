package com.utad.tfg.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.utad.tfg.model.CharState

import com.utad.tfg.model.DndClass

@Composable
fun CharSelectScreen() {
    val vm = hiltViewModel<MainViewModel>()
    var selectMode = false

    Scaffold(
        topBar = {CharSelectTopbar()},
        floatingActionButton = {if (selectMode) RemoveCharacterButton() else AddCharacterButton()},
    ) { paddingValues ->
        LazyColumn (modifier = Modifier.padding(paddingValues)) {

        }
    }
}

@Composable
fun CharacterCard(name: String, race: String, dndClass: DndClass, imgUri: String, state: CharState) {
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

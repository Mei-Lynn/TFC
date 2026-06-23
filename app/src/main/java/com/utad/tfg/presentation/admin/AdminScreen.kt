package com.utad.tfg.presentation.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: AdminViewModel = hiltViewModel()) {
    val selectedCollection by viewModel.selectedCollection.collectAsStateWithLifecycle()
    val documents by viewModel.documents.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var editingDocument by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isEditorOpen by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (selectedCollection != null) {
                FloatingActionButton(onClick = {
                    editingDocument = emptyMap()
                    isEditorOpen = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Document")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Collection Selector
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.collections) { collection ->
                    FilterChip(
                        selected = collection == selectedCollection,
                        onClick = { viewModel.selectCollection(collection) },
                        label = { Text(collection.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
                    items(documents) { doc ->
                        val docId = doc["_id"] as? String ?: "Unknown ID"
                        val name = doc["name"] as? String ?: "No Name"
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    editingDocument = doc
                                    isEditorOpen = true
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = name, style = MaterialTheme.typography.titleMedium)
                                Text(text = "ID: $docId", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        if (isEditorOpen && editingDocument != null) {
            JsonEditorDialog(
                document = editingDocument!!,
                onDismiss = { isEditorOpen = false },
                onSave = { docId, data ->
                    viewModel.saveDocument(docId, data)
                    isEditorOpen = false
                },
                onDelete = { docId ->
                    viewModel.deleteDocument(docId)
                    isEditorOpen = false
                }
            )
        }
    }
}

@Composable
fun JsonEditorDialog(
    document: Map<String, Any>,
    onDismiss: () -> Unit,
    onSave: (String?, Map<String, Any>) -> Unit,
    onDelete: (String) -> Unit
) {
    val gson = remember { GsonBuilder().setPrettyPrinting().create() }
    val mapType = object : TypeToken<Map<String, Any>>() {}.type

    val docId = document["_id"] as? String
    
    // Remove _id from the editable JSON to prevent user from modifying the document ID directly inside the JSON
    val editableMap = document.toMutableMap().apply { remove("_id") }
    val initialJson = remember { gson.toJson(editableMap) }
    
    var jsonText by remember { mutableStateOf(if (initialJson == "null") "{}" else initialJson) }
    var parseError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (docId == null) "New Document" else "Edit Document") },
        text = {
            Column {
                if (parseError != null) {
                    Text(text = parseError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                OutlinedTextField(
                    value = jsonText,
                    onValueChange = { 
                        jsonText = it
                        parseError = null
                    },
                    modifier = Modifier.fillMaxWidth().height(400.dp),
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                try {
                    val parsedData: Map<String, Any>? = gson.fromJson(jsonText, mapType)
                    if(parsedData == null) {
                         onSave(docId, emptyMap())
                    } else {
                         onSave(docId, parsedData)
                    }
                } catch (e: Exception) {
                    parseError = e.localizedMessage
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Row {
                if (docId != null) {
                    IconButton(onClick = { onDelete(docId) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}

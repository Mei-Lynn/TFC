package com.utad.tfg.presentation.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import coil3.compose.AsyncImage
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
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
fun AdminScreen(vm: AdminViewModel = hiltViewModel()) {
    val selectedCollection by vm.selectedCollection.collectAsStateWithLifecycle()
    val documents by vm.documents.collectAsStateWithLifecycle()
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()

    var editingDocument by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isEditorOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.selectCollection("weapons")
    }

    Scaffold(
        floatingActionButton = {
            if (selectedCollection != null) {
                FloatingActionButton(onClick = {
                    editingDocument = emptyMap()
                    isEditorOpen = true
                },
                    shape = CircleShape
                ) {
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
                items(vm.collections) { collection ->
                    FilterChip(
                        selected = collection == selectedCollection,
                        onClick = { vm.selectCollection(collection) },
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
                    vm.saveDocument(docId, data)
                    isEditorOpen = false
                },
                onDelete = { docId ->
                    vm.deleteDocument(docId)
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
    var imageUri by remember { mutableStateOf(document["imgUri"] as? String) }
    
    // Remove _id and imgUri from the editable JSON
    val editableMap = document.toMutableMap().apply { 
        remove("_id") 
        remove("imgUri")
    }
    val initialJson = remember { gson.toJson(editableMap) }
    
    var jsonText by remember { mutableStateOf(if (initialJson == "null") "{}" else initialJson) }
    var parseError by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri.toString()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (docId == null) "New Document" else "Edit Document") },
        text = {
            Column {
                if (parseError != null) {
                    Text(text = parseError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Image:")
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.size(64.dp).clickable { launcher.launch("image/*") }
                        )
                    } else {
                        Button(onClick = { launcher.launch("image/*") }) {
                            Text("Select Image")
                        }
                    }
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
                         val finalData = parsedData.toMutableMap()
                         if (imageUri != null) {
                             finalData["imgUri"] = imageUri!!
                         } else {
                             finalData.remove("imgUri")
                         }
                         onSave(docId, finalData)
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

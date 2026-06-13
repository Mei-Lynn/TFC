package com.utad.tfg.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.utad.tfg.ui.theme.TFGTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TFGTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf("Characters", /*"Campaigns",*/ "Bestiary")
    val routes = listOf("charSelect", /*"campaigns",*/ "bestiary")
    val icons = listOf(Icons.Default.Person, /*Icons.Default.Home,*/ Icons.AutoMirrored.Filled.List)

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = currentDestination?.hierarchy?.any { it.route == routes[index] } == true,
                        onClick = {
                            navController.navigate(routes[index]) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }

    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = "charSelect",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("charSelect") { CharSelectScreen(
                onAddCharacter = { navController.navigate("charCreate") },
                onCharacterClick = { navController.navigate("charDetails") }
            ) }
            composable("charCreate") { CharCreateScreen(onCharacterCreated = { navController.navigate("charSelect") }) }
            composable("campaigns") { Campaigns() }
            composable("bestiary") { BestiaryScreen() }
            composable("charDetails") { CharDetailsScreen(
                onBack = { navController.popBackStack() },
                onLevelUp = { navController.navigate("levelUp") }
            ) }
            composable("levelUp") { LevelUpScreen(onComplete = { navController.popBackStack() }) }
        }
    }
}
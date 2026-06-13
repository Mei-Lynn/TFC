package com.utad.tfg.presentation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.utad.tfg.model.Ability

@Composable
fun LevelUpScreen() {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val character by vm.selectedCharacter.collectAsStateWithLifecycle()

    val newHP: Int
    val statIncrease: Map<Ability, Int>
}
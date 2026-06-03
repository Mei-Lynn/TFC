package com.utad.tfg.model

sealed class CharState {
    object active : CharState()
    object dead : CharState()
    object retired: CharState()
    object noCampaign: CharState()
}
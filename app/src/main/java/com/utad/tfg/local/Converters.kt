package com.utad.tfg.local

import androidx.room.TypeConverter
import com.utad.tfg.model.CharState

class Converters {
    @TypeConverter
    fun fromCharState(state: CharState): String {
        return when (state) {
            is CharState.active -> "active"
            is CharState.dead -> "dead"
            is CharState.retired -> "retired"
            is CharState.noCampaign -> "noCampaign"
        }
    }

    @TypeConverter
    fun toCharState(state: String): CharState {
        return when (state) {
            "active" -> CharState.active
            "dead" -> CharState.dead
            "retired" -> CharState.retired
            else -> CharState.noCampaign
        }
    }
}

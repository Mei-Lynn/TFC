package com.utad.tfg.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.utad.tfg.model.CharState
import com.utad.tfg.remote.MonsterAction

class Converters {
    private val gson = Gson()

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

    @TypeConverter
    fun fromMonsterActionList(actions: List<MonsterAction>?): String? {
        return actions?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toMonsterActionList(actionsJson: String?): List<MonsterAction>? {
        if (actionsJson == null) return null
        val type = object : TypeToken<List<MonsterAction>>() {}.type
        return gson.fromJson(actionsJson, type)
    }
}

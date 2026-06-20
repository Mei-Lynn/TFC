package com.utad.tfg.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.utad.tfg.local.entities.SpellEntity
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for querying Firestore collections (spells, creatures).
 * Maps Firestore document schemas (Open5e format) to the app's models.
 */
@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val TAG = "FirestoreRepository"

    // ========================= Spells =========================

    /**
     * Fetches all spells from the "spells" Firestore collection
     * and maps them to [SpellEntity] objects.
     */
    suspend fun fetchAllSpells(): List<SpellEntity> {
        return try {
            val snapshot = firestore.collection("spells").get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    mapSpellDocument(doc.data ?: return@mapNotNull null)
                } catch (e: Exception) {
                    Log.e(TAG, "Error mapping spell document ${doc.id}", e)
                    null
                }
            }
        } catch (e: FirebaseFirestoreException) {
            handleFirestoreError("fetchAllSpells", e)
            emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchAllSpells", e)
            emptyList()
        }
    }

    private fun handleFirestoreError(method: String, e: FirebaseFirestoreException) {
        if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
            Log.e(TAG, "PERMISSION_DENIED in $method: Missing or insufficient permissions. " +
                    "Check your Firestore Security Rules in the Firebase Console. " +
                    "Ensure the 'spells' and 'creatures' collections allow read access.")
        } else {
            Log.e(TAG, "Firestore error in $method: ${e.code}", e)
        }
    }


    /**
     * Fetches a single spell by its document key.
     */
    suspend fun fetchSpellByKey(key: String): SpellEntity? {
        return try {
            val doc = firestore.collection("spells").document(key).get().await()
            val data = doc.data ?: return null
            mapSpellDocument(data)
        } catch (e: FirebaseFirestoreException) {
            handleFirestoreError("fetchSpellByKey($key)", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchSpellByKey($key)", e)
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun mapSpellDocument(data: Map<String, Any?>): SpellEntity {
        val key = data["key"] as? String ?: ""
        val name = data["name"] as? String ?: ""
        val level = (data["level"] as? Number)?.toInt() ?: 0

        // Classes: list of {name, key} maps → extract class name from key
        // e.g., "srd-2024_wizard" → "wizard"
        val classesRaw = data["classes"] as? List<Map<String, Any?>> ?: emptyList()
        val classes = classesRaw.mapNotNull { classMap ->
            val classKey = classMap["key"] as? String ?: return@mapNotNull null
            // Strip source prefix: "srd-2024_wizard" → "wizard"
            classKey.substringAfter("_").lowercase()
        }.distinct()

        val desc = data["desc"] as? String
        val higherLevel = data["higher_level"] as? String
        val damageRoll = data["damage_roll"] as? String
        val damageTypes = data["damage_types"] as? List<String> ?: emptyList()
        val rangeText = data["range_text"] as? String
        val castingTime = data["casting_time"] as? String
        val concentration = data["concentration"] as? Boolean ?: false
        val ritual = data["ritual"] as? Boolean ?: false
        val duration = data["duration"] as? String

        // School: {name, key} map
        val schoolMap = data["school"] as? Map<String, Any?>
        val school = schoolMap?.get("name") as? String

        // Components
        val verbal = data["verbal"] as? Boolean ?: false
        val somatic = data["somatic"] as? Boolean ?: false
        val material = data["material"] as? Boolean ?: false
        val materialSpecified = data["material_specified"] as? String

        val components = buildList {
            if (verbal) add("V")
            if (somatic) add("S")
            if (material) add("M")
        }.joinToString(", ")

        return SpellEntity(
            index = key,
            name = name,
            level = level,
            classes = classes,
            damageDice = damageRoll?.takeIf { it.isNotBlank() },
            damageType = damageTypes.firstOrNull(),
            range = rangeText,
            castingTime = castingTime,
            description = desc,
            isConcentration = concentration,
            school = school,
            ritual = ritual,
            components = components,
            material = materialSpecified?.takeIf { it.isNotBlank() },
            duration = duration,
            higherLevel = higherLevel?.takeIf { it.isNotBlank() },
            isSynced = true
        )
    }

    // ========================= Creatures =========================

    /**
     * Fetches a lightweight list of all creatures (key + name only)
     * for the Bestiary browse list.
     */
    suspend fun fetchCreatureList(): List<JsonResource> {
        return try {
            val snapshot = firestore.collection("creatures").get().await()
            snapshot.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                val key = data["key"] as? String ?: return@mapNotNull null
                val name = data["name"] as? String ?: return@mapNotNull null
                JsonResource(index = key, name = name, url = "")
            }
        } catch (e: FirebaseFirestoreException) {
            handleFirestoreError("fetchCreatureList", e)
            emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchCreatureList", e)
            emptyList()
        }
    }

    /**
     * Fetches a single creature by its document key and maps to [DndMonsterResponse].
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun fetchCreatureByKey(key: String): DndMonsterResponse? {
        return try {
            val doc = firestore.collection("creatures").document(key).get().await()
            val data = doc.data ?: return null
            mapCreatureDocument(data)
        } catch (e: FirebaseFirestoreException) {
            handleFirestoreError("fetchCreatureByKey($key)", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchCreatureByKey($key)", e)
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun mapCreatureDocument(data: Map<String, Any?>): DndMonsterResponse {
        val key = data["key"] as? String ?: ""
        val name = data["name"] as? String ?: ""

        // size and type are {name, key} maps
        val sizeMap = data["size"] as? Map<String, Any?>
        val size = sizeMap?.get("name") as? String ?: ""

        val typeMap = data["type"] as? Map<String, Any?>
        val type = typeMap?.get("name") as? String ?: ""

        val alignment = data["alignment"] as? String ?: ""
        val hitPoints = (data["hit_points"] as? Number)?.toInt() ?: 0
        val armorClass = (data["armor_class"] as? Number)?.toInt() ?: 10

        // Ability scores: nested map
        val abilityScores = data["ability_scores"] as? Map<String, Number> ?: emptyMap()
        val strength = abilityScores["strength"]?.toInt() ?: 10
        val dexterity = abilityScores["dexterity"]?.toInt() ?: 10
        val constitution = abilityScores["constitution"]?.toInt() ?: 10
        val intelligence = abilityScores["intelligence"]?.toInt() ?: 10
        val wisdom = abilityScores["wisdom"]?.toInt() ?: 10
        val charisma = abilityScores["charisma"]?.toInt() ?: 10

        // Speed: {walk: Int, unit: String, ...}
        val speedMap = data["speed"] as? Map<String, Any?> ?: emptyMap()
        val walkSpeed = (speedMap["walk"] as? Number)?.toInt()
        val unit = speedMap["unit"] as? String ?: "feet"
        val walkString = if (walkSpeed != null) "$walkSpeed $unit" else "0 ft."

        val challengeRating = (data["challenge_rating"] as? Number)?.toFloat() ?: 0f
        val xp = (data["experience_points"] as? Number)?.toInt() ?: 0

        // Actions: list of {name, desc, ...}
        val actionsRaw = data["actions"] as? List<Map<String, Any?>> ?: emptyList()
        val actions = actionsRaw.map { actionMap ->
            MonsterAction(
                name = actionMap["name"] as? String ?: "",
                desc = actionMap["desc"] as? String ?: ""
            )
        }

        // Traits → also include as "actions" for display purposes
        val traitsRaw = data["traits"] as? List<Map<String, Any?>> ?: emptyList()
        val traits = traitsRaw.map { traitMap ->
            MonsterAction(
                name = traitMap["name"] as? String ?: "",
                desc = traitMap["desc"] as? String ?: ""
            )
        }

        val illustration = data["illustration"] as? String

        return DndMonsterResponse(
            index = key,
            name = name,
            size = size,
            type = type,
            alignment = alignment,
            hitPoints = hitPoints,
            armorClass = listOf(ArmorClass("base", armorClass)),
            strength = strength,
            dexterity = dexterity,
            constitution = constitution,
            intelligence = intelligence,
            wisdom = wisdom,
            charisma = charisma,
            speed = MonsterSpeed(walk = walkString),
            challengeRating = challengeRating,
            xp = xp,
            actions = actions + traits,
            image = illustration
        )
    }
}

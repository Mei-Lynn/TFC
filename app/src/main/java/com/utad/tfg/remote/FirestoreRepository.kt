package com.utad.tfg.remote

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.utad.tfg.local.daos.CharacterDao
import com.utad.tfg.local.entities.Character
import com.utad.tfg.local.entities.SpellEntity
import com.utad.tfg.model.CharState
import com.utad.tfg.security.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for querying Firestore collections (spells, creatures).
 * Maps Firestore document schemas (Open5e format) to the app's models.
 */
@Singleton
class FirestoreRepository @Inject constructor(
    private val application: Application,
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository,
    private val characterDao: CharacterDao,
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

    //===================== Characters (Cloud saving) ====================
    suspend fun downloadCharactersByUID() {
        val user = authRepository.currentUser.first()

        if (user != null) {
            try {
                val snapshot = firestore.collection("characters")
                    .whereEqualTo("uid", user.uid)
                    .get()
                    .await()

                characterDao.deleteAllCharacters()

                snapshot.documents.forEach { doc ->
                    val data = doc.data ?: return@forEach
                    val remoteId = doc.id

                    val charState = when (data["state"] as? String) {
                        "active" -> CharState.active
                        "dead" -> CharState.dead
                        "retired" -> CharState.retired
                        else -> CharState.noCampaign
                    }

                    val character = Character(
                        remoteId = remoteId,
                        campaignId = data["campaignId"] as? String,
                        name = data["name"] as? String ?: "Unknown",
                        raceIndex = data["raceIndex"] as? String ?: "",
                        subraceIndex = data["subraceIndex"] as? String,
                        classIndex = data["classIndex"] as? String ?: "",
                        subclassIndex = data["subclassIndex"] as? String,
                        level = (data["level"] as? Long)?.toInt() ?: 1,
                        maxHp = (data["maxHp"] as? Long)?.toInt() ?: 10,
                        currentHp = (data["currentHp"] as? Long)?.toInt() ?: 10,
                        armorClass = (data["armorClass"] as? Long)?.toInt() ?: 10,
                        initiative = (data["initiative"] as? Long)?.toInt() ?: 0,
                        strength = (data["strength"] as? Long)?.toInt() ?: 10,
                        dexterity = (data["dexterity"] as? Long)?.toInt() ?: 10,
                        constitution = (data["constitution"] as? Long)?.toInt() ?: 10,
                        intelligence = (data["intelligence"] as? Long)?.toInt() ?: 10,
                        wisdom = (data["wisdom"] as? Long)?.toInt() ?: 10,
                        charisma = (data["charisma"] as? Long)?.toInt() ?: 10,
                        imgUri = data["imgUri"] as? String,
                        state = charState,
                        cantrips = data["cantrips"] as? List<String> ?: emptyList(),
                        preparedSpells = data["preparedSpells"] as? List<String> ?: emptyList(),
                        mainHandIndex = data["mainHandIndex"] as? String,
                        offHandIndex = data["offHandIndex"] as? String,
                        armorIndex = data["armorIndex"] as? String
                    )

                    characterDao.insertCharacter(character)
                }
                Log.d(TAG, "Downloaded ${snapshot.size()} characters for user ${user.uid}")

                // Decode Base64 images for characters that have them
                characterDao.getAllCharacters().first().forEach { char ->
                    val imgUri = char.imgUri
                    if (imgUri != null && imgUri.startsWith("data:image/jpeg;base64,")) {
                        val localUri = decodeBase64ToImage(imgUri, char.remoteId ?: "char_${char.id}")
                        if (localUri != null) {
                            characterDao.updateCharacter(char.copy(imgUri = localUri))
                        }
                    }
                }

            } catch (e: FirebaseFirestoreException) {
                handleFirestoreError("downloadCharactersbyUID(${user.uid})", e)
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error in downloadCharactersByUID", e)
            }
        }
    }

    private fun mapCharacterToData(character: Character, uid: String): HashMap<String, Any?> {
        val stateStr = when (character.state) {
            is CharState.active -> "active"
            is CharState.dead -> "dead"
            is CharState.retired -> "retired"
            is CharState.noCampaign -> "noCampaign"
        }

        return hashMapOf(
            "localId" to character.id,
            "uid" to uid,
            "name" to character.name,
            "campaignId" to character.campaignId,
            "raceIndex" to character.raceIndex,
            "subraceIndex" to character.subraceIndex,
            "classIndex" to character.classIndex,
            "subclassIndex" to character.subclassIndex,
            "level" to character.level,
            "maxHp" to character.maxHp,
            "currentHp" to character.currentHp,
            "armorClass" to character.armorClass,
            "initiative" to character.initiative,
            "strength" to character.strength,
            "dexterity" to character.dexterity,
            "constitution" to character.constitution,
            "intelligence" to character.intelligence,
            "wisdom" to character.wisdom,
            "charisma" to character.charisma,
            "imgUri" to character.imgUri,
            "state" to stateStr,
            "cantrips" to character.cantrips,
            "preparedSpells" to character.preparedSpells,
            "mainHandIndex" to character.mainHandIndex,
            "offHandIndex" to character.offHandIndex,
            "armorIndex" to character.armorIndex
        )
    }

    suspend fun uploadCharacter(character: Character) {
        val user = authRepository.currentUser.first()

        if (user != null) {
            try {
                // Encode image if it's a local file
                var imgUriForRemote = character.imgUri
                if (imgUriForRemote != null && imgUriForRemote.startsWith("file://")) {
                    val base64Str = encodeImageToBase64(imgUriForRemote)
                    if (base64Str != null) {
                        imgUriForRemote = base64Str
                    }
                }

                val characterData = mapCharacterToData(
                    character.copy(imgUri = imgUriForRemote), user.uid
                )

                val result = firestore.collection("characters").add(characterData).await()

                val characterWithRemoteID = character.copy(remoteId = result.id)
                characterDao.updateCharacter(characterWithRemoteID)

                Log.d(TAG, "Character uploaded successfully: ${character.name}")
            } catch (e: FirebaseFirestoreException) {
                handleFirestoreError("uploadCharacter($character)", e)
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error in uploadCharacter", e)
            }
        }
    }

    suspend fun updateCharacterRemote(character: Character) {
        val user = authRepository.currentUser.first()
        val remoteId = character.remoteId

        if (user != null && remoteId != null) {
            try {
                // Encode image if it's a local file
                var imgUriForRemote = character.imgUri
                if (imgUriForRemote != null && imgUriForRemote.startsWith("file://")) {
                    val base64Str = encodeImageToBase64(imgUriForRemote)
                    if (base64Str != null) {
                        imgUriForRemote = base64Str
                    }
                }

                val characterData = mapCharacterToData(
                    character.copy(imgUri = imgUriForRemote), user.uid
                )

                firestore.collection("characters")
                    .document(remoteId)
                    .set(characterData)
                    .await()

                Log.d(TAG, "Character updated successfully: ${character.name}")
            } catch (e: FirebaseFirestoreException) {
                handleFirestoreError("updateCharacterRemote($character)", e)
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error in updateCharacterRemote", e)
            }
        } else {
            Log.w(TAG, "Cannot update character remote: user=$user, remoteId=$remoteId")
        }
    }

    suspend fun deleteCharacters(remoteIds : List<String>) {
        val user = authRepository.currentUser.first()

        if (user != null) {
            try {
                remoteIds.forEach { id ->
                    firestore.collection("characters").document(id).delete().await()
                    Log.d(TAG, "Succesfully deleted character with id $id")
                }
            } catch (e: FirebaseFirestoreException) {
                handleFirestoreError("deleteCharacter()", e)
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error in deleteCharacter", e)
            }
        }
    }

    // ========================= Image Helpers =========================

    /**
     * Reads a local image file, heavily compresses it, and encodes to Base64.
     * @return The Base64 string prefixed with data:image/jpeg;base64,, or null on failure.
     */
    private fun encodeImageToBase64(localUri: String): String? {
        return try {
            val uri = Uri.parse(localUri)
            application.contentResolver.openInputStream(uri)?.use { input ->
                val original = BitmapFactory.decodeStream(input)
                val maxDimension = 400 // Heavily downscale to stay under Firestore 1MB limit
                val scaled = if (original.width > maxDimension || original.height > maxDimension) {
                    val ratio = maxDimension.toFloat() / maxOf(original.width, original.height)
                    Bitmap.createScaledBitmap(
                        original,
                        (original.width * ratio).toInt(),
                        (original.height * ratio).toInt(),
                        true
                    )
                } else original

                val outputStream = ByteArrayOutputStream()
                // 60% JPEG quality to further reduce size
                scaled.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
                if (scaled !== original) scaled.recycle()
                original.recycle()

                val byteArray = outputStream.toByteArray()
                val base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
                Log.d(TAG, "Encoded image to Base64. Size in bytes: ${byteArray.size}")
                "data:image/jpeg;base64,$base64String"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to encode image to Base64", e)
            null
        }
    }

    /**
     * Decodes a Base64 string back into a local file.
     * @return The local file:// URI string, or null on failure.
     */
    private fun decodeBase64ToImage(base64Str: String, filename: String): String? {
        return try {
            val cleanBase64 = if (base64Str.startsWith("data:image/jpeg;base64,")) {
                base64Str.substring("data:image/jpeg;base64,".length)
            } else base64Str

            val imageBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

            val dir = File(application.filesDir, "character_images")
            if (!dir.exists()) dir.mkdirs()
            val destFile = File(dir, "$filename.jpg")

            FileOutputStream(destFile).use { it.write(imageBytes) }

            val localUri = Uri.fromFile(destFile).toString()
            Log.d(TAG, "Image decoded and saved for $filename: $localUri")
            localUri
        } catch (e: Exception) {
            Log.e(TAG, "Failed to decode Base64 image for $filename", e)
            null
        }
    }
}

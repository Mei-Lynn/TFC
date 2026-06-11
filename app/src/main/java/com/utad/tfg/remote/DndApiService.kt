package com.utad.tfg.remote

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz de control de la API de DnD 5e
 *
 * Guías de uso:
 *
 * [Documentación oficial](https://5e-bits.github.io/docs/introduction)
 *
 * [API Reference](https://dnd5e.magical20.com)
 */
interface DndApiService {
    @GET("api/backgrounds")
    suspend fun getBackgrounds(): JsonResourceList

    @GET("api/backgrounds/{index}")
    suspend fun getBackgroundDetails(@Path("index") index: String): DndBackgroundResponse

    @GET("api/monsters")
    suspend fun getMonsters(): JsonResourceList
    
    @GET("api/monsters/{index}")
    suspend fun getMonsterDetails(@Path("index") index: String): DndMonsterResponse

    @GET("api/spells")
    suspend fun getSpells(): JsonResourceList

    @GET("api/spells/{index}")
    suspend fun getSpellDetails(@Path("index") index: String): DndSpellResponse
}

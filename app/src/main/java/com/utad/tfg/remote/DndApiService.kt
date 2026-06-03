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
    @GET("api/races")
    suspend fun getRaces(): DndResourceList

    @GET("api/races/{index}")
    suspend fun getRaceDetails(@Path("index") index: String): DndRaceResponse

    @GET("api/classes")
    suspend fun getClasses(): DndResourceList

    @GET("api/classes/{index}")
    suspend fun getClassDetails(@Path("index") index: String): DndClassResponse

    @GET("api/monsters")
    suspend fun getMonsters(): DndResourceList
    
    @GET("api/monsters/{index}")
    suspend fun getMonsterDetails(@Path("index") index: String): DndResource
}

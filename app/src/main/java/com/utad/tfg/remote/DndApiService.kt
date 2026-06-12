package com.utad.tfg.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
    @GET("api/monsters")
    suspend fun getMonsters(@Query("lang") lang: String): JsonResourceList
    
    @GET("api/monsters/{index}")
    suspend fun getMonsterDetails(@Path("index") index: String, @Query("lang") lang: String): DndMonsterResponse

    @GET("api/spells")
    suspend fun getSpells(@Query("level") level: Int? = null, @Query("lang") lang: String): JsonResourceList

    @GET("api/spells/{index}")
    suspend fun getSpellDetails(@Path("index") index: String, @Query("lang") lang: String): DndSpellResponse

    @GET("api/classes/{index}/spells")
    suspend fun getClassSpells(@Path("index") index: String, @Query("lang") lang: String): JsonResourceList
}

/*
    Supuestamente, la api tiene soporte multi-lenguaje para inglés, francés y portugués añadiendo "?lang=" al enlace de llamada.
    Pero por alguna razón, la página de referencia traduce al español sin importar la query de idioma. En las llamadas desde la app, por otro lado, simplemente no traduce nada.

    Voy a dejar el código de queries de lenguaje pero no parece que llegue a hacer nada
*/
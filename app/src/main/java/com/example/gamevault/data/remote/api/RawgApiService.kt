package com.example.gamevault.data.remote.api

import com.example.gamevault.data.remote.dto.GameDetailDto
import com.example.gamevault.data.remote.dto.GamesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz de Retrofit para interactuar con la API pública de RAWG Video Games Database.
 *
 * Documentación oficial: https://rawg.io/apidocs
 */
interface RawgApiService {

    /**
     * Obtiene una lista paginada de videojuegos con soporte para búsqueda y ordenamiento.
     *
     * @param apiKey Clave de API requerida por RAWG.
     * @param search Texto para buscar por nombre (opcional).
     * @param pageSize Número de resultados por página (por defecto 20).
     * @param page Número de página.
     * @param ordering Criterio de orden (-rating, -released, name, etc.).
     */
    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("search") search: String? = null,
        @Query("page_size") pageSize: Int = 20,
        @Query("page") page: Int = 1,
        @Query("ordering") ordering: String? = null
    ): GamesResponseDto

    /**
     * Obtiene la información detallada de un videojuego por su ID.
     *
     * @param gameId ID del videojuego en RAWG.
     * @param apiKey Clave de API requerida.
     */
    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String
    ): GameDetailDto
}

package com.example.project8

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for interacting with the OMDB API.
 */
public interface OmdbService {
    /**
     * Performs a search for movies based on the provided search string and API key.
     */
    @GET("/")
    fun searchMovies(
        @Query("s") searchString: String,
        @Query("apikey") apiKey: String
    ): Call<OmdbSearchResult>
    /**
     * Performs a detailed search for a specific movie based on the provided IMDb ID and API key.
     */
    @GET("/")
   suspend fun searchDetailedMovies(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String
    ): OmdbMovie
}

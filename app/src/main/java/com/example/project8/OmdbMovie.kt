package com.example.project8

import com.google.gson.annotations.SerializedName


/**
 * Data class representing a movie obtained from the OMDB API.
 */
data class OmdbMovie (

    @SerializedName("Title") var Title: String? = null,
    @SerializedName("Year") var Year: String? = null,
    @SerializedName("Rated") var Rated: String? = null,
    @SerializedName("Runtime") var Runtime: String? = null,
    @SerializedName("Genre") var Genre: String? = null,
    @SerializedName("Poster") var Poster: String? = null,
    @SerializedName("imdbRating") var imdbRating: String? = null,
    @SerializedName("imdbID") var imdbID: String? = null,

    )


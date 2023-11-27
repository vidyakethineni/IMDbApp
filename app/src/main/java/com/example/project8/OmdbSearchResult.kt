package com.example.project8

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the search result obtained from the OMDB API.
 */
data class OmdbSearchResult(

    @SerializedName("Search") var Search: ArrayList<OmdbMovie> = arrayListOf(),
    @SerializedName("totalResults") var totalResults: String? = null,
    @SerializedName("Response") var Response: String? = null

)



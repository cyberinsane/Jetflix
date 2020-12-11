package com.cyberinsane.jetflix.data.model


import com.google.gson.annotations.SerializedName

data class ShowResult(
    @SerializedName("results")
    val shows: List<Show>?
)
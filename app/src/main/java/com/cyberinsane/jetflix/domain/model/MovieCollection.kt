package com.cyberinsane.jetflix.domain.model

import com.cyberinsane.jetflix.data.model.Show

data class MovieCollection(
    val nowPlaying: List<Show>? = null,
    val trending: List<Show>? = null,
    val topRated: List<Show>? = null,
    val popular: List<Show>? = null
)
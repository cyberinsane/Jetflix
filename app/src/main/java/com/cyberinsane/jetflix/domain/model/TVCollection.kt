package com.cyberinsane.jetflix.domain.model

import androidx.compose.runtime.Immutable
import com.cyberinsane.jetflix.data.model.Show

@Immutable
data class TVCollection(
    val popular: List<Show>? = null,
    val trending: List<Show>? = null,
    val topRated: List<Show>? = null
)
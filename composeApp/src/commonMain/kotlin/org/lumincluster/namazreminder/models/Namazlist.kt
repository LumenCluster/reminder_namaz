package org.lumincluster.namazreminder.models

import org.jetbrains.compose.resources.DrawableResource

data class Namazlist(
    val topText: String,
    val imageResId: DrawableResource,
    val bottomText: String
)

package backloggd.models

import kotlinx.serialization.Serializable

@Serializable
data class SlimGame(
    val name: String,
    val imageUrl: String
)

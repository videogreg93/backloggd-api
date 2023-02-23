package backloggd.models

import kotlinx.serialization.Serializable

@Serializable
data class SlimGame(
    val id: String,
    val name: String,
    val imageUrl: String
)

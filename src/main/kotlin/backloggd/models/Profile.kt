package backloggd.models

import kotlinx.serialization.Serializable

@Serializable
data class Profile(val username: String,
                   val totalGamesPlayed: Int,
                   val gamesBackloggd: Int,
                   val playedThisYear: Int
)
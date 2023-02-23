package backloggd.models.api.request

import kotlinx.serialization.Serializable

@Serializable
data class AppGameRateRequest(val gameId: String, val rating: Int)

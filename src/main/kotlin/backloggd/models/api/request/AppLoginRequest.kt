package backloggd.models.api.request

import kotlinx.serialization.Serializable

@Serializable
data class AppLoginRequest(val username: String, val password: String)
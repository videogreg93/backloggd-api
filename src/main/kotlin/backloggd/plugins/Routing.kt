package backloggd.plugins

import backloggd.converters.ProfileConverter
import backloggd.models.UserSession
import backloggd.models.api.request.AppLoginRequest
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureRouting() {
    routing {
        val client = HttpClient(CIO)
        get("/login") {
            val loginRequest = call.receive<AppLoginRequest>()
            val response = client.post("https://backloggd.com/users/sign_in") {
                method = HttpMethod.Post
                cookie("ne_cookies_consent", "true")
                headers {
                    append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
                    append(HttpHeaders.ContentLength, "215")
                    append(HttpHeaders.Host, "backloggd.com")
                    append(HttpHeaders.Origin, "https://backloggd.com")
                    append(HttpHeaders.AcceptEncoding, "gzip, deflate, br")
                    append(HttpHeaders.Accept, "*/*")
                    append(HttpHeaders.UserAgent, "PostmanRuntime/7.31.0")
                    append(
                        "Cookie",
                        " _august_app_session=ZE0xNHQxeHZKbUY4YnRLZ2FiRkkrdWYyc1BIZ2dTZlpJWldhWG04KytOYUhQOFFtcVNqalRTTFhPaUcvTDZxZXE0RGtDOSt0V0R2ZHJDMVkrVk1pcDhtcTZlaGpZSS9RRDdYSGhKN1FzL1NoL3FQUi9nUlVkU1pZZU9HN0c2SFFCNXd6Y2liMUdDUnFkcXhWMzYvM21pSThLZkxINmtHa3VQV3pVK1JuZlRqdVlRRkRDK0pJUzhBMkZuUXNINm5vLS0rSUt0YXhBa3FWNER0c0VxWVdSY2tnPT0%3D--82aa38d0805f57b26cff33810ef6a1d2b84486ab; _august_app_session=c2VaajhkaFh4bFg4SXpzWjFFOWtsb0xub0dVU3kzTngrWFNIamRXVU5FbGtwaWw3ODNZaXlpZFZqM0pKRS91NWZYOXgzVkdyMVVSN3NTeTdkOE9GNmwyY2pRSTAwNCtTWVI4cDcxMjRIYVBsdUlBcVV3UGQ0a2JzN3lkdlV5eUhOWmc2VzZqcGp5NEdPYkM1SzRRWis3aW12RnRhOHFqQTU3bkZISXJEbVJOV3p2NEd3NzJlL29helVMUHlLTzNHLS0xNUp2OWN5TVpQb1hlWURTNWdJc1FRPT0%3D--be02f85bb6f50829f22dba64c4e9b68e83f89a1d"
                    )
                }

                setBody("utf8=%E2%9C%93&authenticity_token=PsCx04P_5OtAr1-9YW9ozjLYwzu3zyDdaqfNV-V4d2xL2Wo0PYCyvfVb25G-mPRhFTNbe7Il9tpr87YHGMqc5g&user%5Blogin%5D=${loginRequest.username}&user%5Bpassword%5D=${loginRequest.password}&user%5Bremember_me%5D=0&commit=")
            }
            if (response.status != HttpStatusCode.UnprocessableEntity) {
                val cookieId = response.setCookie().find { it.name == "_august_app_session" }?.value ?: error("No session cookie found")
                call.sessions.set(UserSession(cookieId))
                call.respondText("Success login")
            } else {
                call.respondText("Failed login")
            }
        }
        get("/profile/{id}") {
//            val session = call.sessions.get<UserSession>()
//            if (session == null) {
//                error("No Session")
//            }
            val user = call.parameters["id"].orEmpty()
            val profileResponse = client.get("https://backloggd.com/u/${user}")
            val gamesResponse = client.get("https://backloggd.com/u/${user}/games/")
            if (profileResponse.status == HttpStatusCode.OK) {
                val profile = ProfileConverter.toProfile(user, profileResponse, gamesResponse)
                call.respond(profile)
            } else {
                call.response.status(HttpStatusCode.NotFound)
            }
        }
    }
}

package backloggd.plugins

import backloggd.models.Profile
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.date.*
import org.jsoup.Jsoup

fun Application.configureRouting() {
    routing {
        val client = HttpClient(CIO)
        get("/") {
            val response = client.request("https://backloggd.com/users/sign_in") {
                // Configure request parameters exposed by HttpRequestBuilder
                method = HttpMethod.Post
                headers {
                    append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
                    append(HttpHeaders.ContentLength, "215")
                    append(HttpHeaders.AcceptEncoding, "gzip, deflate, br")
                    append(HttpHeaders.Accept, "*/*")
                    append(HttpHeaders.UserAgent, "PostmanRuntime/7.31.0")
                }
                cookie(
                    name = "_august_app_session",
                    value = "ZE0xNHQxeHZKbUY4YnRLZ2FiRkkrdWYyc1BIZ2dTZlpJWldhWG04KytOYUhQOFFtcVNqalRTTFhPaUcvTDZxZXE0RGtDOSt0V0R2ZHJDMVkrVk1pcDhtcTZlaGpZSS9RRDdYSGhKN1FzL1NoL3FQUi9nUlVkU1pZZU9HN0c2SFFCNXd6Y2liMUdDUnFkcXhWMzYvM21pSThLZkxINmtHa3VQV3pVK1JuZlRqdVlRRkRDK0pJUzhBMkZuUXNINm5vLS0rSUt0YXhBa3FWNER0c0VxWVdSY2tnPT0%3D--82aa38d0805f57b26cff33810ef6a1d2b84486ab",
                    expires = GMTDate(
                        seconds = 0,
                        minutes = 0,
                        hours = 10,
                        dayOfMonth = 1,
                        month = Month.APRIL,
                        year = 2023
                    )
                )
                // See ContentNegotiation plugin to use data class for body
                setBody("utf8=%E2%9C%93&authenticity_token=PsCx04P_5OtAr1-9YW9ozjLYwzu3zyDdaqfNV-V4d2xL2Wo0PYCyvfVb25G-mPRhFTNbe7Il9tpr87YHGMqc5g&user%5Blogin%5D=videogreg93&user%5Bpassword%5D=sJ7uyrySVK4S3rL&user%5Bremember_me%5D=0&commit=")
            }
            if (response.status == HttpStatusCode.Found) {
                call.respondText("Success login")
            } else {
                call.respondText("Failed login")
            }
        }
        get("/profile/{id}") {
            val user = call.parameters["id"].orEmpty()
            val response = client.request("https://backloggd.com/u/${user}") {
                // Configure request parameters exposed by HttpRequestBuilder
                method = HttpMethod.Get
                headers {
                    append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
                    append(HttpHeaders.ContentLength, "215")
                    append(HttpHeaders.AcceptEncoding, "gzip, deflate, br")
                    append(HttpHeaders.Accept, "*/*")
                    append(HttpHeaders.UserAgent, "PostmanRuntime/7.31.0")
                }
                cookie(
                    name = "_august_app_session",
                    value = "ZE0xNHQxeHZKbUY4YnRLZ2FiRkkrdWYyc1BIZ2dTZlpJWldhWG04KytOYUhQOFFtcVNqalRTTFhPaUcvTDZxZXE0RGtDOSt0V0R2ZHJDMVkrVk1pcDhtcTZlaGpZSS9RRDdYSGhKN1FzL1NoL3FQUi9nUlVkU1pZZU9HN0c2SFFCNXd6Y2liMUdDUnFkcXhWMzYvM21pSThLZkxINmtHa3VQV3pVK1JuZlRqdVlRRkRDK0pJUzhBMkZuUXNINm5vLS0rSUt0YXhBa3FWNER0c0VxWVdSY2tnPT0%3D--82aa38d0805f57b26cff33810ef6a1d2b84486ab",
                    expires = GMTDate(
                        seconds = 0,
                        minutes = 0,
                        hours = 10,
                        dayOfMonth = 1,
                        month = Month.APRIL,
                        year = 2023
                    )
                )
            }
            if (response.status == HttpStatusCode.OK) {
                val doc = Jsoup.parse(response.bodyAsText())
                val totalGamesPlayed = doc.select("h4").first { it.text().contains("Total Games Played") }.previousElementSibling().text()
                val gamesBackloggs = doc.select("h4").first { it.text().contains("Games Backloggd") }.previousElementSibling().text()
                call.respond(
                    Profile(
                        user,
                        totalGamesPlayed.toInt(),
                        gamesBackloggs.toInt()
                    )
                )
            } else {
                call.respondText("Failed to find user $user")
            }
        }
    }
}

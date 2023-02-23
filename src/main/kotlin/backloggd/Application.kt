package backloggd

import backloggd.models.UserSession
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import backloggd.plugins.*
import io.ktor.client.plugins.cookies.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.sessions.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    configureHTTP()
    configureRouting()
    install(Sessions) {
        header<UserSession>("user_session", SessionStorageMemory())
    }
}

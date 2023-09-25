package net.dixq.webserver2

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface OnMessageListener {
    fun OnMessage(str: String)
}

object WebServer {

    fun start(listener: OnMessageListener){

        // サーバーを起動するためのコルーチンを起動します。
        GlobalScope.launch {

            embeddedServer(Netty, port = 8080) {

                install(ContentNegotiation) {
                    jackson { }
                }

                install(StatusPages) {
                    exception<Throwable> { cause ->
                        call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
                    }
                }

                routing {
                    get("/{...}") {
                        val fullPath = call.request.uri
                        withContext(Dispatchers.Main) {
                            listener.OnMessage(fullPath)
                        }
                        call.respond(HttpStatusCode.OK, "アクセスされたURL: $fullPath")
                    }
                }

                routing {
                    post("/message") {
                        val message = call.receive<String>()
                        withContext(Dispatchers.Main) {
                            listener.OnMessage(message)
                        }
                        call.respond(HttpStatusCode.OK, "Received: $message")
                    }
                }

            }.start(wait = true)

        }

    }

}
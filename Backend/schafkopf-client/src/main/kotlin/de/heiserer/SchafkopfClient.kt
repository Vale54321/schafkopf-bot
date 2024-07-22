package de.heiserer

import CmdSchafkopfMessager
import de.heiserer.player.NPCPlayer
import de.heiserer.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val test = SchafkopfGameController(listOf(NPCPlayer("NPC 1"), NPCPlayer("NPC 2"), CmdPlayer("Dev"), NPCPlayer("NPC 4")), CmdSchafkopfMessager())

    test.playRound()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
}

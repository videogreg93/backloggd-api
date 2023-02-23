package backloggd.converters

import backloggd.models.Profile
import backloggd.models.SlimGame
import io.ktor.client.statement.*
import org.jsoup.Jsoup
import java.util.*
import java.util.Calendar.YEAR

object ProfileConverter {

    suspend fun toProfile(user: String, response: HttpResponse, gamesResponse: HttpResponse): Profile {
        val doc = Jsoup.parse(response.bodyAsText())
        val totalGamesPlayed =
            doc.select("h4").first { it.text().contains("Total Games Played") }.previousElementSibling().text()
        val gamesBackloggs =
            doc.select("h4").first { it.text().contains("Games Backloggd") }.previousElementSibling().text()
        val currentYear = Calendar.getInstance().get(YEAR)
        val playedThisYear =
            doc.select("h4").first { it.text().contains("Played in $currentYear") }.previousElementSibling().text()
        return Profile(
            user,
            totalGamesPlayed.toInt(),
            gamesBackloggs.toInt(),
            playedThisYear.toInt(),
            getGames(gamesResponse)
        )
    }

    private suspend fun getGames(gamesResponse: HttpResponse): List<SlimGame> {
        val doc = Jsoup.parse(gamesResponse.bodyAsText())
        val games = doc.select(".cover-link").map {
            val id = it.parent().attr("game-id")
            val imageUrl = it
                .siblingElements().select(".overflow-wrapper").first()
                .children().select("img").first()
                .attr("src")
            val name = it.siblingElements().select(".game-text-centered").first().text()
            SlimGame(id, name, imageUrl)
        }
        return games
    }
}
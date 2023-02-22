package backloggd.converters

import backloggd.models.Profile
import io.ktor.client.statement.*
import org.jsoup.Jsoup
import java.util.Calendar
import java.util.Calendar.YEAR

object ProfileConverter {

    suspend fun toProfile(user: String, response: HttpResponse): Profile {
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
            playedThisYear.toInt()
        )
    }
}
package fr.wildcodeschool.kotlinsample

import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.*
import java.io.*
import java.text.*
import java.util.*

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsChannel
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.nio.file.Files
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.URL
import java.security.Provider.Service

class SpaceXApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun getAllLaunches(): List<RocketLaunch> {
        return httpClient.get("https://api.spacexdata.com/v5/launches").body()
    }

    suspend fun downloadArticle(launch: RocketLaunch) {

        var url = URL(launch.links.article)
        println(url)

        val targetPath = "/Users/Sophi/Downloads/articles-quete-kotlin/article_"+ launch.flightNumber
        println("Enregistrement des articles dans : " + targetPath)

        httpClient.get(url).bodyAsChannel().copyAndClose(File(targetPath).writeChannel())
    }

}


fun main() = runBlocking<Unit> {
    val service = SpaceXApi()
    val launches: List<RocketLaunch> = service.getAllLaunches()

    var i = 0
    for (l in launches) {
        if(l.links.article != null){
            println("Launch $i : ${l.links.article}")
            async {
                SpaceXApi().downloadArticle(l);
            }
        }
        i++
    }
}


@Serializable
data class RocketLaunch(
    @SerialName("flight_number")
    val flightNumber: Int,
    @SerialName("name")
    val missionName: String,
    @SerialName("date_utc")
    val launchDateUTC: String,
    @SerialName("details")
    val details: String?,
    @SerialName("success")
    val launchSuccess: Boolean?,
    @SerialName("links")
    val links: Links
) {
}

@Serializable
data class Links(
    @SerialName("patch")
    val patch: Patch?,
    @SerialName("article")
    val article: String?
)

@Serializable
data class Patch(
    @SerialName("small")
    val small: String?,
    @SerialName("large")
    val large: String?
)
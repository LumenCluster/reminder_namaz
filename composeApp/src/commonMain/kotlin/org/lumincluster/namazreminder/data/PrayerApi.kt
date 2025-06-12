package org.lumincluster.namazreminder.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.lumincluster.namazreminder.models.PrayerTimes

class PrayerApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getPrayerTimes(
        latitude: Double,
        longitude: Double
    ): PrayerTimes {
        val response: HttpResponse = client.get("https://api.aladhan.com/v1/timings") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("method", 2) // Choose calculation method if needed
        }

        val body = response.bodyAsText()
        val json = kotlinx.serialization.json.Json.parseToJsonElement(body).jsonObject
        val data = json["data"]?.jsonObject
        val timings = data?.get("timings")?.jsonObject

        return PrayerTimes(
            fajr = timings?.get("Fajr")?.jsonPrimitive?.contentOrNull ?: "N/A",
            dhuhr = timings?.get("Dhuhr")?.jsonPrimitive?.contentOrNull ?: "N/A",
            asr = timings?.get("Asr")?.jsonPrimitive?.contentOrNull ?: "N/A",
            maghrib = timings?.get("Maghrib")?.jsonPrimitive?.contentOrNull ?: "N/A",
            isha = timings?.get("Isha")?.jsonPrimitive?.contentOrNull ?: "N/A"
        )
    }





}
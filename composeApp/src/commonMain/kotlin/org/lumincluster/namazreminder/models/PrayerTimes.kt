package org.lumincluster.namazreminder.models

import kotlinx.serialization.Serializable

@Serializable
data class PrayerTimes(
    val fajr: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)


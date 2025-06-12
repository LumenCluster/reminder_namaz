package org.lumincluster.namazreminder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
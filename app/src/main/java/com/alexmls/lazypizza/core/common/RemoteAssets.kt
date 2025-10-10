package com.alexmls.lazypizza.core.common

object RemoteAssets {
    const val BASE = "https://pl-coding.com/wp-content/uploads/lazypizza/"
}

fun buildImageUrl(relativePath: String): String {
    val trimmed = relativePath.trim().removePrefix("/")
    val encoded = trimmed
        .split('/')
        .joinToString("/") { android.net.Uri.encode(it) }

    return RemoteAssets.BASE + encoded
}
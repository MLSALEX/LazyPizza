package com.alexmls.lazypizza.core.common

object RemoteAssets {
    const val BASE = "https://pl-coding.com/wp-content/uploads/lazypizza/"
}

fun buildImageUrl(relativePath: String): String {
    // trim & remove leading slash
    var p = relativePath.trim().removePrefix("/")

    // collapse accidental double slashes
    while ("//" in p) p = p.replace("//", "/")

    // common folder typos → "toppings/"
    p = p.replaceFirst("^topping[s]?/".toRegex(RegexOption.IGNORE_CASE), "toppings/")

    // URL-encode each segment (preserves case for filenames like "Pepper Oni.png")
    val encoded = p.split('/').joinToString("/") { android.net.Uri.encode(it) }

    return RemoteAssets.BASE + encoded
}
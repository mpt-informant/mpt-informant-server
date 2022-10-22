package me.kofesst.ktor.mptinformant.features.data.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

suspend inline fun <T : Any> parseDocument(
    url: String,
    crossinline block: suspend Document.() -> T?,
): T? = withContext(Dispatchers.IO) {
    runCatching {
        val document = Jsoup.connect(url).get()
        block(document)
    }.getOrNull()
}

suspend inline fun <T : Any> parseDocument(
    url: String,
    defaultValue: T,
    crossinline block: suspend Document.() -> T,
): T = withContext(Dispatchers.IO) {
    runCatching {
        val document = Jsoup.connect(url).get()
        block(document)
    }.getOrDefault(defaultValue)
}

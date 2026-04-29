package com.adrscan.util

object UrlHelper {
    private val URL_REGEX = Regex("^(https?|ftp)://.*", RegexOption.IGNORE_CASE)
    private val WWW_REGEX = Regex("^www\\..*", RegexOption.IGNORE_CASE)

    fun isUrl(content: String): Boolean {
        val trimmed = content.trim()
        return URL_REGEX.matches(trimmed) || WWW_REGEX.matches(trimmed)
    }

    fun normalizeUrl(content: String): String {
        val trimmed = content.trim()
        return if (WWW_REGEX.matches(trimmed)) "https://$trimmed" else trimmed
    }

    fun extractDomain(url: String): String {
        return try {
            normalizeUrl(url).substringAfter("://").substringBefore("/").substringBefore("?")
        } catch (e: Exception) {
            url.take(30)
        }
    }

    fun getDisplayLabel(content: String, isUrl: Boolean, maxLength: Int = 20): String {
        return if (isUrl) {
            extractDomain(content)
        } else {
            if (content.length > maxLength) content.take(maxLength) + "…" else content
        }
    }
}

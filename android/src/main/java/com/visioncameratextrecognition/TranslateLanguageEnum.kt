package com.visioncameratextrecognition

import com.google.mlkit.nl.translate.TranslateLanguage.*

fun translateLanguage(language: String): String? {
    return when (language.lowercase()) {
        "af" -> AFRIKAANS
        "ar" -> ARABIC
        "be" -> BELARUSIAN
        "bg" -> BULGARIAN
        "bn" -> BENGALI
        "ca" -> CATALAN
        "cs" -> CZECH
        "cy" -> WELSH
        "da" -> DANISH
        "de" -> GERMAN
        "el" -> GREEK
        "en" -> ENGLISH
        "eo" -> ESPERANTO
        "es" -> SPANISH
        "et" -> ESTONIAN
        "fa" -> PERSIAN
        "fi" -> FINNISH
        "fr" -> FRENCH
        "ga" -> IRISH
        "gl" -> GALICIAN
        "gu" -> GUJARATI
        "he" -> HEBREW
        "hi" -> HINDI
        "hr" -> CROATIAN
        "ht" -> HAITIAN_CREOLE
        "hu" -> HUNGARIAN
        "id" -> INDONESIAN
        "is" -> ICELANDIC
        "it" -> ITALIAN
        "ja" -> JAPANESE
        "ka" -> GEORGIAN
        "kn" -> KANNADA
        "ko" -> KOREAN
        "lt" -> LITHUANIAN
        "lv" -> LATVIAN
        "mk" -> MACEDONIAN
        "mr" -> MARATHI
        "ms" -> MALAY
        "mt" -> MALTESE
        "nl" -> DUTCH
        "no" -> NORWEGIAN
        "pl" -> POLISH
        "pt" -> PORTUGUESE
        "ro" -> ROMANIAN
        "ru" -> RUSSIAN
        "sk" -> SLOVAK
        "sl" -> SLOVENIAN
        "sq" -> ALBANIAN
        "sv" -> SWEDISH
        "sw" -> SWAHILI
        "ta" -> TAMIL
        "te" -> TELUGU
        "th" -> THAI
        "tl" -> TAGALOG
        "tr" -> TURKISH
        "uk" -> UKRAINIAN
        "ur" -> URDU
        "vi" -> VIETNAMESE
        "zh" -> CHINESE
        else -> null
    }
}

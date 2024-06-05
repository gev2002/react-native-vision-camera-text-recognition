package com.visioncameratextrecognition

import com.google.mlkit.nl.translate.TranslateLanguage.*


fun translateLanguage(language: String): String? {
    return when (language.lowercase()) {
        "afrikaans" -> AFRIKAANS
        "albanian" -> ALBANIAN
        "arabic" -> ARABIC
        "belarusian" -> BELARUSIAN
        "bengali" -> BENGALI
        "bulgarian" -> BULGARIAN
        "catalan" -> CATALAN
        "chinese" -> CHINESE
        "czech" -> CZECH
        "danish" -> DANISH
        "dutch" -> DUTCH
        "english" -> ENGLISH
        "esperanto" -> ESPERANTO
        "estonian" -> ESTONIAN
        "finnish" -> FINNISH
        "french" -> FRENCH
        "galician" -> GALICIAN
        "georgian" -> GEORGIAN
        "german" -> GERMAN
        "greek" -> GREEK
        "gujarati" -> GUJARATI
        "haitiancreole" -> HAITIAN_CREOLE
        "hebrew" -> HEBREW
        "hindi" -> HINDI
        "hungarian" -> HUNGARIAN
        "icelandic" -> ICELANDIC
        "indonesian" -> INDONESIAN
        "irish" -> IRISH
        "italian" -> ITALIAN
        "japanese" -> JAPANESE
        "kannada" -> KANNADA
        "korean" -> KOREAN
        "latvian" -> LATVIAN
        "lithuanian" -> LITHUANIAN
        "macedonian" -> MACEDONIAN
        "malay" -> MALAY
        "maltese" -> MALTESE
        "marathi" -> MARATHI
        "norwegian" -> NORWEGIAN
        "persian" -> PERSIAN
        "polish" -> POLISH
        "portuguese" -> PORTUGUESE
        "romanian" -> ROMANIAN
        "russian" -> RUSSIAN
        "slovak" -> SLOVAK
        "slovenian" -> SLOVENIAN
        "spanish" -> SPANISH
        "swahili" -> SWAHILI
        "tagalog" -> TAGALOG
        "tamil" -> TAMIL
        "telugu" -> TELUGU
        "thai" -> THAI
        "turkish" -> TURKISH
        "ukrainian" -> UKRAINIAN
        "urdu" -> URDU
        "vietnamese" -> VIETNAMESE
        "welsh" -> WELSH
        else -> null
    }
}


package info.toughlife.mcdev.commons

fun String.prettyPrint(): String {
    return prettyPrintJSON(this)
}

private fun prettyPrintJSON(unformattedJsonString: String): String {
    val prettyJSONBuilder = StringBuilder()
    var indentLevel = 0
    var inQuote = false
    for (charFromUnformattedJson in unformattedJsonString.toCharArray()) {
        when (charFromUnformattedJson) {
            '"' -> {
                inQuote = !inQuote
                prettyJSONBuilder.append(charFromUnformattedJson)
            }
            ' ' ->
                if (inQuote) {
                    prettyJSONBuilder.append(charFromUnformattedJson)
                }
            '{', '[' -> {
                prettyJSONBuilder.append(charFromUnformattedJson)
                indentLevel++
                appendIndentedNewLine(indentLevel, prettyJSONBuilder)
            }
            '}', ']' -> {
                indentLevel--
                appendIndentedNewLine(indentLevel, prettyJSONBuilder)
                prettyJSONBuilder.append(charFromUnformattedJson)
            }
            ',' -> {
                prettyJSONBuilder.append(charFromUnformattedJson)
                if (!inQuote) {
                    appendIndentedNewLine(indentLevel, prettyJSONBuilder)
                }
            }
            else -> prettyJSONBuilder.append(charFromUnformattedJson)
        }
    }
    return prettyJSONBuilder.toString()
}

private fun appendIndentedNewLine(indentLevel: Int, stringBuilder: StringBuilder) {
    stringBuilder.append("\n")
    for (i in 0 until indentLevel) {
        // Assuming indention using 2 spaces
        stringBuilder.append("  ")
    }
}
package nl.gardensnakes.ukrainefield.helper

class StringHelper {
    companion object {
        fun capitalizeWords(text: String): String = text.split(" ").joinToString(" ") { it.capitalize() }
    }
}
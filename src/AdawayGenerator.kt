import java.io.File

const val IGNORE_WEB_BLACKLIST = false
const val OUTPUT_FOLDER = "generated/"
const val OUTPUT_FILE = "hosts"

class AdawayGenerator {

    fun generate(): List<String> {
        val whitelist = whitelist()

        return blacklist().filter { !whitelist.any { e -> it.contains(e) } }
                .map { e -> "0.0.0.0 $e" }
                .toList()
    }

    private fun blacklist(): List<String> {
        val blacklist = file("blacklist.txt")
        val webBlacklist = if (IGNORE_WEB_BLACKLIST) emptyList() else file("webblacklist.txt")

        return blacklist.plus(webBlacklist).distinct()
    }

    private fun whitelist(): List<String> = file("whitelist.txt")

    private fun file(filename: String): List<String> {
        return try {
            AdawayGenerator::class.java.getResource(filename).readText().split("\n").toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

fun main() {
    File(OUTPUT_FOLDER).mkdir().also {
        File(OUTPUT_FOLDER, OUTPUT_FILE)
                .printWriter()
                .use { writer ->
                    AdawayGenerator().generate().forEach {
                        writer.println(it)
                    }
                }
    }
}


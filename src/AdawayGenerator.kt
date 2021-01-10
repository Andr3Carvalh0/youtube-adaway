import java.io.File

class AdawayGenerator {

    fun generate(): List<String> {
        val whitelist = whitelist()

        return blacklist().filter {
            whitelist.forEach { e ->
                if (it.contains(e)) { return@filter false }
            }

            return@filter true
        }
        .map { e -> "0.0.0.0 $e" }
        .toList()
    }

    private fun blacklist(): List<String> {
        val blacklist = file("blacklist.txt")
        val webBlacklist = file("webblacklist.txt")

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
    File("generated/").mkdir()
    File("generated/list.txt").apply {
        printWriter().use { writer ->
            AdawayGenerator().generate().forEach {
                writer.println(it)
            }
        }
    }
}


import java.io.IOException
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

fun main() {
    val code = Path("src/day00/Day00.kt").readText()
    for (day in 1..25) {
        try {
            val dayStr = day.toString().padStart(2, '0')
            val dayDirectory = Path("src/day$dayStr")
            val dayCodeFile = dayDirectory.resolve("Day$dayStr.kt")
            val dayInputFile = dayDirectory.resolve("Day$dayStr.txt")
            val dayTestFile = dayDirectory.resolve("Day${dayStr}_test.txt")
            Files.createDirectory(dayDirectory)
            dayCodeFile.writeText(code.replace("00", dayStr))
            Files.createFile(dayInputFile)
            Files.createFile(dayTestFile)
        }
        catch (e: IOException) {
            println(e)
        }
    }
}
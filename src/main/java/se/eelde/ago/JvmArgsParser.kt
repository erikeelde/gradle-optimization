package se.eelde.ago

import java.nio.charset.Charset

class JvmArgsParser {
    private val maxRegExp = """.*-Xmx(\d*)([kKmMgG]).*""".toRegex()
    private val fileEncodingRegexp = """.*-Dfile.encoding=([^\s.]*).*""".toRegex()

    fun parseMaxJmvMem(jvmArgs: String): Memory {
        val matches = maxRegExp.matchEntire(jvmArgs)
        matches?.let { matchResult ->

            if (matchResult.groupValues.size == 3) {
                when (matchResult.groupValues[2]) {
                    "k", "K" -> return Memory.Kilobyte(matchResult.groupValues[1].toInt())
                    "m", "M" -> return Memory.Megabyte(matchResult.groupValues[1].toInt())
                    "g", "G" -> return Memory.Gigabyte(matchResult.groupValues[1].toInt())
                }
            }
        }

        return Memory.UNDEFINED
    }

    // -Dfile.encoding=UTF-8
    fun parseFileEncoding(jvmArgs: String): Charset? {
        val matches = fileEncodingRegexp.matchEntire(jvmArgs)
        matches?.let { matchResult ->
            if (matchResult.groupValues.size == 2) {
                try {
                    return charset(matchResult.groupValues[1])
                } catch (ignored: Exception) {
                }
            }
        }

        return null
    }
}

sealed class Memory {
    abstract fun asBytes(): Int

    object UNDEFINED : Memory() {
        override fun asBytes() = -1
    }

    data class Kilobyte(val size: Int) : Memory() {
        override fun asBytes() = size * 1000
    }

    data class Megabyte(val size: Int) : Memory() {
        override fun asBytes() = size * 1000 * 1000
    }

    data class Gigabyte(val size: Int) : Memory() {
        override fun asBytes() = size * 1000 * 1000 * 1000
    }
}
package se.eelde.build_optimization

import java.nio.charset.Charset

class JvmArgsParser {
    private val xmxRegExp = """.*-Xmx(\d*)([kKmMgG]).*""".toRegex()
    private val xmsRegExp = """.*-Xms(\d*)([kKmMgG]).*""".toRegex()
    private val fileEncodingRegexp = """.*-Dfile.encoding=([^\s.]*).*""".toRegex()

    fun parseJvmXmsMemory(jvmArgs: String): Memory {
        val matches = xmsRegExp.matchEntire(jvmArgs)
        matches?.let { matchResult ->

            if (matchResult.groupValues.size == 3) {
                when (matchResult.groupValues[2]) {
                    "k", "K" -> return Memory.Kilobyte(matchResult.groupValues[1].toLong())
                    "m", "M" -> return Memory.Megabyte(matchResult.groupValues[1].toLong())
                    "g", "G" -> return Memory.Gigabyte(matchResult.groupValues[1].toLong())
                }
            }
        }

        return Memory.UNDEFINED
    }

    fun parseJvmXmxMemory(jvmArgs: String): Memory {
        val matches = xmxRegExp.matchEntire(jvmArgs)
        matches?.let { matchResult ->

            if (matchResult.groupValues.size == 3) {
                when (matchResult.groupValues[2]) {
                    "k", "K" -> return Memory.Kilobyte(matchResult.groupValues[1].toLong())
                    "m", "M" -> return Memory.Megabyte(matchResult.groupValues[1].toLong())
                    "g", "G" -> return Memory.Gigabyte(matchResult.groupValues[1].toLong())
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
    abstract fun asBytes(): Long

    object UNDEFINED : Memory() {
        override fun asBytes() = -1L
    }

    data class Kilobyte(val size: Long) : Memory() {
        override fun asBytes() = size * 1000
    }

    data class Megabyte(val size: Long) : Memory() {
        override fun asBytes() = size * 1000 * 1000
    }

    data class Gigabyte(val size: Long) : Memory() {
        override fun asBytes() = size * 1000 * 1000 * 1000
    }
}
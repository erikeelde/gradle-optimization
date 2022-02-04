package se.eelde.buildOptimization

import org.gradle.api.tasks.Input
import java.nio.charset.Charset

class JvmArgsParser {
    private val xmxRegExp =
        """.*-Xmx(\d*)([kKmMgG]).*""".toRegex()
    private val xmsRegExp =
        """.*-Xms(\d*)([kKmMgG]).*""".toRegex()
    private val fileEncodingRegexp =
        """.*-Dfile.encoding=([^\s.]*).*""".toRegex()

    fun parseJvmXmsMemory(jvmArgs: String): Memory {
        val matches = xmsRegExp.matchEntire(jvmArgs)
        matches?.let { matchResult ->

            if (matchResult.groupValues.size == expectedSizeMatchingGroups) {
                return when (matchResult.groupValues[sizeQuantifierGroupPosition]) {
                    "k", "K" -> Memory.Kilobyte(matchResult.groupValues[sizeGroupPosition].toLong())
                    "m", "M" -> Memory.Megabyte(matchResult.groupValues[sizeGroupPosition].toLong())
                    "g", "G" -> Memory.Gigabyte(matchResult.groupValues[sizeGroupPosition].toLong())
                    else -> throw IllegalArgumentException("Unable to parse ${matchResult.groupValues[sizeQuantifierGroupPosition]}")
                }
            }
        }

        return Memory.UNDEFINED
    }

    fun parseJvmXmxMemory(jvmArgs: String): Memory {
        val matches = xmxRegExp.matchEntire(jvmArgs)
        matches?.let { matchResult ->

            if (matchResult.groupValues.size == expectedSizeMatchingGroups) {
                return when (matchResult.groupValues[sizeQuantifierGroupPosition]) {
                    "k", "K" -> Memory.Kilobyte(matchResult.groupValues[sizeGroupPosition].toLong())
                    "m", "M" -> Memory.Megabyte(matchResult.groupValues[sizeGroupPosition].toLong())
                    "g", "G" -> Memory.Gigabyte(matchResult.groupValues[sizeGroupPosition].toLong())
                    else -> throw IllegalArgumentException("Unable to parse ${matchResult.groupValues[sizeQuantifierGroupPosition]}")
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

    companion object {
        const val expectedSizeMatchingGroups = 3
        const val sizeQuantifierGroupPosition = 2
        const val sizeGroupPosition = 1
    }
}

sealed class Memory {
    companion object {
        const val ONE_THOUSAND = 1_000
    }

    abstract fun asBytes(): Long

    object UNDEFINED : Memory() {
        override fun asBytes() = -1L
    }

    data class Kilobyte(@Input val size: Long) : Memory() {
        override fun asBytes() = size * ONE_THOUSAND
    }

    data class Megabyte(@Input val size: Long) : Memory() {
        override fun asBytes() = size * ONE_THOUSAND * ONE_THOUSAND
    }

    data class Gigabyte(@Input val size: Long) : Memory() {
        override fun asBytes() = size * ONE_THOUSAND * ONE_THOUSAND * ONE_THOUSAND
    }
}

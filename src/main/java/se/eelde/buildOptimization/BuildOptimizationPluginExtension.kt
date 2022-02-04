package se.eelde.buildOptimization

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional

open class BuildOptimizationPluginExtension {
    private val maxRegExp =
        """(\d*)([kKmMgG])([bB])""".toRegex()

    @Nested
    fun getJvmXmxMemory(): Memory {
        jvmXmx?.let { jvmXmx ->
            val matches = maxRegExp.matchEntire(jvmXmx)
            matches?.let { matchResult ->

                if (matchResult.groupValues.size == expectedSizeMatchingGroups) {
                    return parseDaMem(matchResult)
                }
            }
        }
        return Memory.Gigabyte(2)
    }

    @Nested
    fun getJvmXmsMemory(): Memory {
        jvmXms?.let { jvmXms ->
            val matches = maxRegExp.matchEntire(jvmXms)
            matches?.let { matchResult ->

                if (matchResult.groupValues.size == expectedSizeMatchingGroups) {
                    return parseDaMem(matchResult)
                }
            }
        }
        return Memory.Megabyte(defaultJmvMxsMegabytes)
    }

    private fun parseDaMem(matchResult: MatchResult) = when (matchResult.groupValues[sizeQuantifierGroupPosition]) {
        "k", "K" -> Memory.Kilobyte(matchResult.groupValues[sizeGroupPosition].toLong())
        "m", "M" -> Memory.Megabyte(matchResult.groupValues[sizeGroupPosition].toLong())
        "g", "G" -> Memory.Gigabyte(matchResult.groupValues[sizeGroupPosition].toLong())
        else -> throw IllegalArgumentException(
            "Unable to parse ${matchResult.groupValues[sizeQuantifierGroupPosition]}"
        )
    }

    @Input
    @Optional
    var jvmXmx: String? = null

    @Input
    @Optional
    var jvmXms: String? = null

    companion object {
        const val expectedSizeMatchingGroups = 4
        const val sizeQuantifierGroupPosition = 2
        const val sizeGroupPosition = 1
        const val defaultJmvMxsMegabytes = 250L
    }
}

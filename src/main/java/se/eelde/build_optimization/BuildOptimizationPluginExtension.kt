package se.eelde.build_optimization

open class BuildOptimizationPluginExtension {
    private val maxRegExp = """(\d*)([kKmMgG])([bB])""".toRegex()

    fun getJvmXmxMemory(): Memory {
        jvmXmx?.let { jvmXmx ->
            val matches = maxRegExp.matchEntire(jvmXmx)
            matches?.let { matchResult ->

                if (matchResult.groupValues.size == 4) {
                    when (matchResult.groupValues[2]) {
                        "k", "K" -> return Memory.Kilobyte(matchResult.groupValues[1].toLong())
                        "m", "M" -> return Memory.Megabyte(matchResult.groupValues[1].toLong())
                        "g", "G" -> return Memory.Gigabyte(matchResult.groupValues[1].toLong())
                    }
                }
            }
        }
        return Memory.Gigabyte(2)
    }

    fun getJvmXmsMemory(): Memory {
        jvmXms?.let { jvmXms ->
            val matches = maxRegExp.matchEntire(jvmXms)
            matches?.let { matchResult ->

                if (matchResult.groupValues.size == 4) {
                    when (matchResult.groupValues[2]) {
                        "k", "K" -> return Memory.Kilobyte(matchResult.groupValues[1].toLong())
                        "m", "M" -> return Memory.Megabyte(matchResult.groupValues[1].toLong())
                        "g", "G" -> return Memory.Gigabyte(matchResult.groupValues[1].toLong())
                    }
                }
            }
        }
        return Memory.Megabyte(250)
    }

    var jvmXmx: String? = null
    var jvmXms: String? = null
}
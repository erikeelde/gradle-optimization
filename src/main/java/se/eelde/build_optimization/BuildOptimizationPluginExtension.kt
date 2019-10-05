package se.eelde.build_optimization

open class BuildOptimizationPluginExtension {
    private val maxRegExp = """(\d*)([kKmMgG])([bB])""".toRegex()

    fun getMaxJvmMem(): Memory {
        val matches = maxRegExp.matchEntire(jvmMinMem!!)
        matches?.let { matchResult ->

            if (matchResult.groupValues.size == 4) {
                when (matchResult.groupValues[2]) {
                    "k", "K" -> return Memory.Kilobyte(matchResult.groupValues[1].toInt())
                    "m", "M" -> return Memory.Megabyte(matchResult.groupValues[1].toInt())
                    "g", "G" -> return Memory.Gigabyte(matchResult.groupValues[1].toInt())
                }
            }
        }

        return Memory.UNDEFINED
    }

    var jvmMinMem: String? = null
}
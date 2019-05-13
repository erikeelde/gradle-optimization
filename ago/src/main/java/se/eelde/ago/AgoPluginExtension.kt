package se.eelde.ago

open class AgoPluginExtension {

    fun shouldSkipOptimizations(extraProperties: Map<String, String>): Boolean {
        skipOptimizationsEnvVar?.let { envVar ->
            if (extraProperties.containsKey(envVar)) {
                return when (extraProperties[envVar]) {
                    is String -> (extraProperties[envVar] as String).toBoolean()
                    else -> false
                }
            }
        }
        return false
    }

    var jvmMinMem: String? = null
    var skipOptimizationsEnvVar: String? = null
}
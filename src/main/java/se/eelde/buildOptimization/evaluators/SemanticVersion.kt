package se.eelde.buildOptimization.evaluators

/***
 * This is not proper semantic version checking - it also allows omitting the patch version
 */
data class SemanticVersion(
    val major: Int = 0,
    val minor: Int = 0,
    val patch: Int = 0,
    val preRelease: String? = null,
    val buildMetadata: String? = null
) {
    fun compatibleWith(version: SemanticVersion): Boolean {
        return major == version.major && minor <= version.minor
    }

    companion object {
        val versionRegexp =
            """^(?<major>0|[1-9]\d*)\.(?<minor>0|[1-9]\d*)\.?(?<patch>0|[1-9]\d*)?(?:-(?<prerelease>(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\.(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\+(?<buildmetadata>[0-9a-zA-Z-]+(?:\.[0-9a-zA-Z-]+)*))?${'$'}""".toRegex()

        fun parse(version: String): SemanticVersion {
            val result = versionRegexp.find(version)
            val major: Int? = result?.groups?.get("major")?.value?.toIntOrNull()
            val minor: Int? = result?.groups?.get("minor")?.value?.toIntOrNull()
            val patch: Int? = result?.groups?.get("patch")?.value?.toIntOrNull()
            val prerelease: String? = result?.groups?.get("prerelease")?.value
            val buildMetadata: String? = result?.groups?.get("buildmetadata")?.value
            return SemanticVersion(major ?: 0, minor ?: 0, patch ?: 0, prerelease, buildMetadata)
        }
    }
}

package se.eelde.buildOptimization

class CiChecker(private val environmentVariables: Map<String, String>) {

    sealed class DetektedCi {
        object Generic : DetektedCi()
        object GithubActions : DetektedCi()
        object Travis : DetektedCi()
        object Circle : DetektedCi()
        object Jenkins : DetektedCi()
        object TeamCity : DetektedCi()
    }

    fun detectedCi(): DetektedCi? {
        return when {
            isTeamCity() -> DetektedCi.TeamCity
            isJenkis() -> DetektedCi.Jenkins
            isCircleCi() -> DetektedCi.Circle
            isTravis() -> DetektedCi.Travis
            isGithubAction() -> DetektedCi.GithubActions
            isGenericCi() -> DetektedCi.Generic
            else -> null
        }
    }

    private fun isGenericCi(): Boolean {
        /**
         * CI=true
         */
        return environmentVariables["CI"]?.toBoolean() ?: false
    }

    private fun isGithubAction(): Boolean {
        /**
         * https://docs.github.com/en/free-pro-team@latest/actions/reference/environment-variables#default-environment-variables
         * CI=true
         */

        val ci: Boolean = environmentVariables["CI"]?.toBoolean() ?: false
        val githubAction: Boolean = environmentVariables["GITHUB_ACTION"] != null

        return ci && githubAction
    }

    private fun isTravis(): Boolean {
        /**
         * https://docs.travis-ci.com/user/environment-variables/
         * CI=true
         * TRAVIS=true
         */
        val ci: Boolean = environmentVariables["CI"]?.toBoolean() ?: false
        val travis: Boolean = environmentVariables["TRAVIS"]?.toBoolean() ?: false

        return ci && travis
    }

    private fun isCircleCi(): Boolean {
        /**
         * https://circleci.com/docs/2.0/env-vars/#example-configuration-of-environment-variables
         * CI=true
         * CIRCLECI=true
         */
        val ci: Boolean = environmentVariables["CI"]?.toBoolean() ?: false
        val circleci: Boolean = environmentVariables["CIRCLECI"]?.toBoolean() ?: false

        return ci && circleci
    }

    private fun isJenkis(): Boolean {
        /**
         * https://wiki.jenkins.io/display/JENKINS/Building+a+software+project
         */
        val buildNumber = environmentVariables["BUILD_NUMBER"]
        val buildId = environmentVariables["BUILD_ID"]
        val buildUrl = environmentVariables["BUILD_URL"]
        return buildNumber != null && buildId != null && buildUrl != null
    }

    private fun isTeamCity(): Boolean {
        /**
         * https://confluence.jetbrains.com/display/TCD9/Predefined+Build+Parameters
         */
        val teamcityversion = environmentVariables["TEAMCITY_VERSION"]
        return teamcityversion != null
    }
}

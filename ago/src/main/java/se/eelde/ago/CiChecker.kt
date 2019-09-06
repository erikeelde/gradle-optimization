package se.eelde.ago

class CiChecker(private val environmentVariables: Map<String, String>) {

    fun isCi(): Boolean {
        return isCircleCi() || isTravis() || isJenkis() || isTeamCity()
    }

    fun isTravis(): Boolean {
        /**
         * https://docs.travis-ci.com/user/environment-variables/
         * CI=true
         * TRAVIS=true
         */
        val ci: Boolean = environmentVariables["CI"]?.toBoolean() ?: false
        val travis: Boolean = environmentVariables["TRAVIS"]?.toBoolean() ?: false

        return ci && travis
    }

    fun isCircleCi(): Boolean {
        /**
         * https://circleci.com/docs/2.0/env-vars/#example-configuration-of-environment-variables
         * CI=true
         * CIRCLECI=true
         */
        val ci: Boolean = environmentVariables["CI"]?.toBoolean() ?: false
        val circleci: Boolean = environmentVariables["CIRCLECI"]?.toBoolean() ?: false

        return ci && circleci
    }

    fun isJenkis(): Boolean {
        /**
         * https://wiki.jenkins.io/display/JENKINS/Building+a+software+project
         */
        val buildNumber = environmentVariables["BUILD_NUMBER"]
        val buildId = environmentVariables["BUILD_ID"]
        val buildUrl = environmentVariables["BUILD_URL"]
        return buildNumber != null && buildId != null && buildUrl != null
    }

    fun isTeamCity(): Boolean {
        /**
         * https://confluence.jetbrains.com/display/TCD9/Predefined+Build+Parameters
         */
        val teamcityversion = environmentVariables["TEAMCITY_VERSION"]
        return teamcityversion != null
    }
}
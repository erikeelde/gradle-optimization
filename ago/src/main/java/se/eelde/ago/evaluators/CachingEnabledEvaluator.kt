package se.eelde.ago.evaluators

import org.gradle.StartParameter
import org.gradle.api.internal.project.DefaultProject

class CachingEnabledEvaluator(private val project: DefaultProject) {
    val startParameter: StartParameter
        get() {
            return project.services.get(StartParameter::class.java)
        }

    val isCachingEnabled: Boolean
        get() {
            return startParameter.isBuildCacheEnabled
        }
}
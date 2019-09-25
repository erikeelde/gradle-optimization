package se.eelde.ago.evaluators

import org.gradle.StartParameter
import org.gradle.api.internal.project.DefaultProject

class StartParameterEvaluator(private val project: DefaultProject) {

    val startParameter: StartParameter
        get() {
            return project.services.get(StartParameter::class.java)
        }

    val isConfigureOnDemand: Boolean
        get() {
            @Suppress("UnstableApiUsage")
            return startParameter.isConfigureOnDemand
        }

    val isParalellExecutionEnabled: Boolean
        get() {
            return startParameter.isParallelProjectExecutionEnabled
        }

    val isCachingEnabled: Boolean
        get() {
            return startParameter.isBuildCacheEnabled
        }
}

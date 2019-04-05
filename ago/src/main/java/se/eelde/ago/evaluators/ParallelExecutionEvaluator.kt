package se.eelde.ago.evaluators

import org.gradle.StartParameter
import org.gradle.api.internal.project.DefaultProject

class ParallelExecutionEvaluator(private val project: DefaultProject) {
    val startParameter: StartParameter
        get() {
            return project.services.get(StartParameter::class.java)
        }

    val isParalellExecutionEnabled: Boolean
        get() {
            return startParameter.isParallelProjectExecutionEnabled
        }
}
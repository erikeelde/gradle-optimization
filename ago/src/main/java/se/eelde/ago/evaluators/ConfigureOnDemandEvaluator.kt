package se.eelde.ago.evaluators

import org.gradle.StartParameter
import org.gradle.api.internal.project.DefaultProject

class ConfigureOnDemandEvaluator(private val project: DefaultProject) {

    val startParameter: StartParameter
        get() {
            return project.services.get(StartParameter::class.java)
        }

    val isConfigureOnDemand: Boolean
        get() {
            return startParameter.isConfigureOnDemand
        }
}

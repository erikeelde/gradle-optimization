package se.eelde.ago.evaluators

import org.gradle.api.internal.project.DefaultProject

class MemoryEvaluator(private val project: DefaultProject) {
    val getMaxMemory: Long
        get() {
            return Runtime.getRuntime().maxMemory()
        }
}
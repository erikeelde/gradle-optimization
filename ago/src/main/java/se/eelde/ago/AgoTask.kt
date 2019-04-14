package se.eelde.ago

import org.gradle.api.DefaultTask
import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.tasks.TaskAction
import se.eelde.ago.evaluators.*
import javax.inject.Inject


open class AgoTask @Inject constructor(private var agoOutputter: AgoOutputter) : DefaultTask() {
    @TaskAction
    fun moduleTask() {

        agoOutputter.greatInfo()

        val daemonExecutionEvaluator = DaemonExecutionEvaluator(project as DefaultProject)

        if (daemonExecutionEvaluator.isSingleUse) {
            agoOutputter.useDaemon()
        } else {
            agoOutputter.daemonUsed()
        }

        val parallelExecutionEvaluator = ParallelExecutionEvaluator(project as DefaultProject)

        if (!parallelExecutionEvaluator.isParalellExecutionEnabled) {
            agoOutputter.useParallel()
        } else {
            agoOutputter.parallelUsed()
        }

        val cachingEnabledEvaluator = CachingEnabledEvaluator(project as DefaultProject)

        if (!cachingEnabledEvaluator.isCachingEnabled) {
            agoOutputter.useCaching()
        } else {
            agoOutputter.cachingUsed()
        }

        val configureOnDemandEvaluator = ConfigureOnDemandEvaluator(project as DefaultProject)

        if (!configureOnDemandEvaluator.isConfigureOnDemand) {
            agoOutputter.useConfigureOnDemand()
        } else {
            agoOutputter.configureOnDemandUsed()
        }

        val memoryEvaluator = MemoryEvaluator(project as DefaultProject)

        val jvmMemory = memoryEvaluator.getMaxMemory / 1000000
        agoOutputter.output("Defined Jvm Memory: $jvmMemory")

        // project.extensions.extraProperties["org.gradle.jvmargs"]
    }
}

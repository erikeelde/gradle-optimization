package se.eelde.ago

import org.gradle.api.DefaultTask
import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.tasks.TaskAction
import se.eelde.ago.evaluators.CachingEnabledEvaluator
import se.eelde.ago.evaluators.ConfigureOnDemandEvaluator
import se.eelde.ago.evaluators.DaemonExecutionEvaluator
import se.eelde.ago.evaluators.ParallelExecutionEvaluator
import javax.inject.Inject


open class AgoTask @Inject constructor(private var agoOutputter: AgoOutputter) : DefaultTask() {
    @TaskAction
    fun moduleTask() {

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

        // project.extensions.extraProperties["org.gradle.jvmargs"]
    }
}

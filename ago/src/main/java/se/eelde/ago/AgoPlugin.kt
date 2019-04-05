package se.eelde.ago

import org.gradle.StartParameter
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.project.DefaultProject
import org.gradle.cache.CacheRepository
import org.gradle.launcher.daemon.server.health.DaemonHealthStats
import se.eelde.ago.evaluators.DaemonExecutionEvaluator
import se.eelde.ago.evaluators.MemoryEvaluator

class AgoPlugin : Plugin<Project> {
    var extension: AgoPluginExtension? = null

    lateinit var agoOutputter: AgoOutputter

    override fun apply(project: Project) {

        agoOutputter = AgoOutputter(defaultProject = (project as DefaultProject), logger = project.logger)

        extension = project.extensions.create("ago", AgoPluginExtension::class.java)

        project.tasks.create("androidGradleOptimizations", AgoTask::class.java, agoOutputter)

        project.afterEvaluate {

            project.extensions

            agoOutputter.greatInfo()

            val daemonExecutionEvaluator = DaemonExecutionEvaluator(project)

            // agoOutputter.output("Is execution as daemon: ${!daemonExecutionEvaluator.isSingleUse}")

            // agoOutputter.output("memory ${extension!!.mem}")

            val memoryEvaluator = MemoryEvaluator(project)
            val get = project.services.get(DaemonHealthStats::class.java)

            agoOutputter.output("maxmem: ${Runtime.getRuntime().maxMemory()}")
            agoOutputter.output("totalmem: ${Runtime.getRuntime().totalMemory()}")

            project.run {
                val lol1 = services.get(StartParameter::class.java)
                val lol3 = services.get(CacheRepository::class.java)
                // val lol6 = services.get(DaemonCommandExecution::class.java)
                // agoOutputter.output("parallel: ${lol1.isParallelProjectExecutionEnabled}")
            }

            // DefaultServiceRegistry#L984

        }
    }
}

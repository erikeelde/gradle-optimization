package se.eelde.ago

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.project.DefaultProject

class AgoPlugin : Plugin<Project> {
    var extension: AgoPluginExtension? = null

    lateinit var agoOutputter: AgoOutputter

    override fun apply(project: Project) {

        agoOutputter = AgoOutputter(defaultProject = (project as DefaultProject), logger = project.logger)

        val ciChecker = CiChecker(System.getenv())

        extension = project.extensions.create("ago", AgoPluginExtension::class.java)

        if (ciChecker.isCi()) {
            agoOutputter.printRunningOnCi()
            return
        }

        val agoTask = project.tasks.create("androidGradleOptimizations", AgoTask::class.java) { task -> task.agoOutputter = agoOutputter }

        project.rootProject.afterEvaluate {

            for (task in it.tasks) {
                if (task != agoTask) {
                    task.dependsOn(agoTask)
                }
            }
        }
    }
}

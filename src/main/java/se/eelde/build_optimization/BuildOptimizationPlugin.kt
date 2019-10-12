package se.eelde.build_optimization

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.project.DefaultProject

class BuildOptimizationPlugin : Plugin<Project> {
    var buildOptimizationPluginExtension: BuildOptimizationPluginExtension? = null

    lateinit var buildOptimizationOutputter: BuildOptimizationOutputter

    override fun apply(project: Project) {

        buildOptimizationOutputter = BuildOptimizationOutputter(defaultProject = (project as DefaultProject), logger = project.logger)

        val ciChecker = CiChecker(System.getenv())
        buildOptimizationPluginExtension = project.extensions.create("buildOptimization", BuildOptimizationPluginExtension::class.java)

        if (ciChecker.isCi()) {
            buildOptimizationOutputter.printRunningOnCi()
            return
        }

        val buildOptimizationTask = project.tasks.register("checkBuildOptimizations", BuildOptimizationTask::class.java) { task ->
            task.buildOptimizationOutputter = buildOptimizationOutputter
            task.buildOptimizationPluginExtension = buildOptimizationPluginExtension as BuildOptimizationPluginExtension
        }

        project.afterEvaluate {
            for (task in it.tasks) {
                if (task.name != buildOptimizationTask.name) {
                    task.dependsOn(buildOptimizationTask)
                }
            }
        }

//        project.tasks.register("applyBuildOptimizations", BuildOptimizationTask::class.java) { task ->
//            task.buildOptimizationOutputter = buildOptimizationOutputter
//            task.buildOptimizationPluginExtension = buildOptimizationPluginExtension as BuildOptimizationPluginExtension
//        }
    }
}

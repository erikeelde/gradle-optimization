package se.eelde.buildOptimization

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.tasks.TaskAction
import se.eelde.buildOptimization.evaluators.DaemonExecutionEvaluator
import se.eelde.buildOptimization.evaluators.FileWatcherEvaluator
import se.eelde.buildOptimization.evaluators.StartParameterEvaluator

open class BuildOptimizationTask : DefaultTask() {

    internal lateinit var buildOptimizationOutputter: BuildOptimizationOutputter
    internal lateinit var buildOptimizationPluginExtension: BuildOptimizationPluginExtension

    @TaskAction
    @Suppress("LongMethod", "ComplexMethod")
    fun moduleTask() {

        // output this if the task is explicity invoked?
        // buildOptimizationOutputter.greatInfo()

        var optimizationsMissing = false

        val fileWatcherEvaluator = FileWatcherEvaluator(project as DefaultProject)
        val fileSystemWatcherCheck = Check.FileSystemWatcher()
        when (fileWatcherEvaluator.isWatchFileSystem) {
            FileWatcherEvaluator.Result.Watching,
            FileWatcherEvaluator.Result.WatchingUnstable,
            FileWatcherEvaluator.Result.NotApplicable -> {
                // output this if the task is explicity invoked?
                // buildOptimizationOutputter.printPraise(cacheCheck)
            }
            FileWatcherEvaluator.Result.NotWatching -> {
                buildOptimizationOutputter.printCheck(fileSystemWatcherCheck)
                if (fileSystemWatcherCheck.aDefault == CheckSeverity.ENABLED_ENFORCED) {
                    optimizationsMissing = true
                }
            }
        }

        val daemonCheck = Check.Daemon()
        val daemonExecutionEvaluator = DaemonExecutionEvaluator(project as DefaultProject)
        if (daemonExecutionEvaluator.isSingleUse) {
            buildOptimizationOutputter.printCheck(daemonCheck)

            if (daemonCheck.aDefault == CheckSeverity.ENABLED_ENFORCED) {
                optimizationsMissing = true
            }
        } else {
            // output this if the task is explicity invoked?
            // buildOptimizationOutputter.printPraise(daemonCheck)
        }

        val startParameterEvaluator = StartParameterEvaluator(project as DefaultProject)

        val parallelCheck = Check.Parallel()
        if (!startParameterEvaluator.isParallelExecutionEnabled) {
            buildOptimizationOutputter.printCheck(parallelCheck)
            if (parallelCheck.aDefault == CheckSeverity.ENABLED_ENFORCED) {
                optimizationsMissing = true
            }
        } else {
            // output this if the task is explicity invoked?
            // buildOptimizationOutputter.printPraise(parallelCheck)
        }

        val cacheCheck = Check.Cache()
        if (!startParameterEvaluator.isCachingEnabled) {
            buildOptimizationOutputter.printCheck(cacheCheck)
            if (cacheCheck.aDefault == CheckSeverity.ENABLED_ENFORCED) {
                optimizationsMissing = true
            }
        } else {
            // output this if the task is explicity invoked?
            // buildOptimizationOutputter.printPraise(cacheCheck)
        }

        val configureOnDemandCheck = Check.ConfigureOnDemand()
        if (!startParameterEvaluator.isConfigureOnDemand) {
            buildOptimizationOutputter.printCheck(configureOnDemandCheck)
            if (configureOnDemandCheck.aDefault == CheckSeverity.ENABLED_ENFORCED) {
                optimizationsMissing = true
            }
        } else {
            // output this if the task is explicity invoked?
            // buildOptimizationOutputter.printPraise(configureOnDemandCheck)
        }

        if (project.extensions.extraProperties.has("org.gradle.jvmargs")) {
            val jvmArgs = project.extensions.extraProperties["org.gradle.jvmargs"]?.toString() ?: ""
            val jvmArgsParser = JvmArgsParser()

            val parseJmvXmxMem = jvmArgsParser.parseJvmXmxMemory(jvmArgs = jvmArgs)
            val dslProvidedJvmXmx = Check.JvmXmx(buildOptimizationPluginExtension.getJvmXmxMemory())

            if (parseJmvXmxMem.asBytes() < dslProvidedJvmXmx.size.asBytes()) {
                buildOptimizationOutputter.printCheck(dslProvidedJvmXmx)
                if (dslProvidedJvmXmx.aDefault == CheckSeverity.ENABLED_ENFORCED) {
                    optimizationsMissing = true
                }
            } else {
                // output this if the task is explicity invoked?
                // buildOptimizationOutputter.printPraise(dslProvidedJvmXmx)
            }

            val parseJmvXmsMem = jvmArgsParser.parseJvmXmsMemory(jvmArgs = jvmArgs)
            val dslProvidedJvmXms = Check.JvmXms(buildOptimizationPluginExtension.getJvmXmsMemory())

            if (parseJmvXmsMem.asBytes() < dslProvidedJvmXms.size.asBytes()) {
                buildOptimizationOutputter.printCheck(dslProvidedJvmXms)
                if (dslProvidedJvmXms.aDefault == CheckSeverity.ENABLED_ENFORCED) {
                    optimizationsMissing = true
                }
            } else {
                // output this if the task is explicity invoked?
                // buildOptimizationOutputter.printPraise(dslProvidedJvmXms)
            }

            val parseFileEncoding = jvmArgsParser.parseFileEncoding(jvmArgs = jvmArgs)
            val utF8FileEncodingCheck = Check.UTF8FileEncoding(Charsets.UTF_8)
            if (parseFileEncoding != utF8FileEncodingCheck.charset) {
                buildOptimizationOutputter.printCheck(utF8FileEncodingCheck)
                if (utF8FileEncodingCheck.aDefault == CheckSeverity.ENABLED_ENFORCED) {
                    optimizationsMissing = true
                }
            } else {
                // output this if the task is explicity invoked?
                // buildOptimizationOutputter.printPraise(utF8FileEncodingCheck)
            }
        }

        val versionsUpdatePluginCheck = Check.VersionsPlugin()
        @Suppress("SENSELESS_COMPARISON")
        if (!project.plugins.hasPlugin("com.github.ben-manes.versions")) {
            buildOptimizationOutputter.printCheck(versionsUpdatePluginCheck)
        }

        if (optimizationsMissing) {
            throw GradleException("Missing required optimizations - check logs")
        }
    }
}

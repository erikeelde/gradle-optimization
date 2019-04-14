package se.eelde.ago

import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger
import org.gradle.internal.logging.text.StyledTextOutput
import org.gradle.internal.logging.text.StyledTextOutputFactory


class AgoOutputter(var logger: Logger, defaultProject: DefaultProject) {

    private val styledTextOutput: StyledTextOutput

    init {
        val styledTextOutputFactory = defaultProject.services.get(StyledTextOutputFactory::class.java)
        styledTextOutput = styledTextOutputFactory.create("ago")
        styledTextOutput
                .style(StyledTextOutput.Style.ProgressStatus).text("Eventually ")
                .style(StyledTextOutput.Style.Failure).text("we will  ")
                .style(StyledTextOutput.Style.Identifier).println("have teh color")


    }

    fun greatInfo() {
        logger.log(LogLevel.LIFECYCLE,
                """great info: https://proandroiddev.com/gradle-perf-9c11b640f329
                    | https://docs.gradle.org/current/userguide/command_line_interface.html#sec:command_line_performance

                """.trimMargin())
    }

    fun output(string: String) {
        logger.log(LogLevel.LIFECYCLE, string)
    }

    fun parallelUsed() {
        logger.log(LogLevel.LIFECYCLE,
                """
                    Using parallel :+1
                """.trimIndent())
    }

    fun useParallel() {
        logger.log(LogLevel.LIFECYCLE,
                """
                    https://guides.gradle.org/performance/
                     | If possible, enable org.gradle.parallel
                     | Run with --parallel on the command-line
                """.trimIndent())
    }

    fun cachingUsed() {
        logger.log(LogLevel.LIFECYCLE,
                """
                    Using cache :+1
                """.trimIndent())
    }

    fun useCaching() {
        logger.log(LogLevel.LIFECYCLE,
                """
                    https://docs.gradle.org/current/userguide/build_cache.html
                     | Make sure org.gradle.caching=true in your gradle.properties file
                     | Run with --build-cache or --no-build-cache on the command-line
                """.trimIndent())
    }

    fun daemonUsed() {
        logger.log(LogLevel.LIFECYCLE,
                """
                   Using daemon :+1
                """.trimIndent())
    }

    fun useDaemon() {
        logger.log(LogLevel.LIFECYCLE,
                """
                   https://docs.gradle.org/current/userguide/gradle_daemon.html
                    | Don't disable the daemon: org.gradle.daemon=false
                    | Run with --daemon and --no-daemon on the command-line
                """.trimIndent())
    }

    fun useConfigureOnDemand() {
        logger.log(LogLevel.LIFECYCLE,
                """
                   https://docs.gradle.org/current/userguide/multi_project_builds.html#sec:configuration_on_demand
                    | org.gradle.configureondemand=true
                    | Run with --configure-on-demand, --no-configure-on-demand on the command-line
                """.trimIndent())
    }

    fun configureOnDemandUsed() {
        logger.log(LogLevel.LIFECYCLE,
                """
                    Using configure on demand :+1
                """.trimIndent())
    }
}
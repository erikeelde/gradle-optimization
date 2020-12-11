package se.eelde.buildOptimization

import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger
import org.gradle.internal.logging.text.StyledTextOutput
import org.gradle.internal.logging.text.StyledTextOutputFactory

class BuildOptimizationOutputter(var logger: Logger, defaultProject: DefaultProject) {

    private val styledTextOutput: StyledTextOutput

    init {
        val styledTextOutputFactory = defaultProject.services.get(StyledTextOutputFactory::class.java)
        styledTextOutput = styledTextOutputFactory.create("build-optimization")
    }

    fun greatInfo() {
        logger.log(
            LogLevel.LIFECYCLE,
            """great info: https://proandroiddev.com/gradle-perf-9c11b640f329
                    | https://docs.gradle.org/current/userguide/command_line_interface.html#sec:command_line_performance

                """.trimMargin()
        )
    }

    internal fun printCheck(check: Check) {
        val sb = StringBuilder()
        sb.append("${check.link} \n")
        check.hints.forEach { hint -> sb.append(" | $hint \n") }

        when (check.aDefault) {
            CheckSeverity.ENABLED_ENFORCED ->
                styledTextOutput
                    .style(StyledTextOutput.Style.Failure)
                    .text(sb.toString())
            CheckSeverity.ENABLED_RECOMMENDED ->
                logger.log(LogLevel.LIFECYCLE, sb.toString())
        }
    }

    internal fun printPraise(check: Check) {
        logger.log(LogLevel.LIFECYCLE, check.praise)
    }

    fun printRunningOnCi(detektedCi: CiChecker.DetektedCi) {
        when (detektedCi) {
            CiChecker.DetektedCi.Generic ->
                logger.log(LogLevel.LIFECYCLE, "Build running on CI - ignoring gradle optimization-checks.")
            CiChecker.DetektedCi.GithubActions ->
                logger.log(LogLevel.LIFECYCLE, "Build running on Github actions - ignoring gradle optimization-checks.")
            CiChecker.DetektedCi.Travis ->
                logger.log(LogLevel.LIFECYCLE, "Build running on Travis - ignoring gradle optimization-checks.")
            CiChecker.DetektedCi.Circle ->
                logger.log(LogLevel.LIFECYCLE, "Build running on Circle Ci - ignoring gradle optimization-checks.")
            CiChecker.DetektedCi.Jenkins ->
                logger.log(LogLevel.LIFECYCLE, "Build running on Jenkins - ignoring gradle optimization-checks.")
            CiChecker.DetektedCi.TeamCity ->
                logger.log(LogLevel.LIFECYCLE, "Build running on TeamCity - ignoring gradle optimization-checks.")
        }

    }
}

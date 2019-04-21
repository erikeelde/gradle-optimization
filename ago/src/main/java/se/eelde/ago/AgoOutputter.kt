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

    internal fun printCheck(check: Check) {
        val sb = StringBuilder()
        sb.append("${check.link} \n")
        check.hints.forEach { hint -> sb.append(" | $hint \n") }

        logger.log(LogLevel.LIFECYCLE, sb.toString())
    }

    internal fun printPraise(check: Check) {
        logger.log(LogLevel.LIFECYCLE, check.praise)
    }

}
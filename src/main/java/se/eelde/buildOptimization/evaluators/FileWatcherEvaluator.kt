package se.eelde.buildOptimization.evaluators

import org.gradle.StartParameter
import org.gradle.api.internal.StartParameterInternal
import org.gradle.api.internal.project.DefaultProject

class FileWatcherEvaluator(private val project: DefaultProject) {

    sealed class Result {
        object Watching : Result()
        object WatchingUnstable : Result()
        object NotApplicable : Result()
        object NotWatching : Result()
    }

    private val startParameter: StartParameter
        get() {
            return project.services.get(StartParameter::class.java)
        }

    val isWatchFileSystem: Result
        get() {
            val gradleVersion = SemanticVersion.parse(project.gradle.gradleVersion)

            val compatible = gradleVersion.compatibleWith(SemanticVersion.parse("6.7"))
            // throw GradleException("oh noes: comparing $gradleVersion $compatible")

            return if (SemanticVersion.parse("6.7").compatibleWith(gradleVersion)) {
                // 6.7
                if ((startParameter as StartParameterInternal).isWatchFileSystem) {
                    Result.Watching
                } else {
                    Result.NotWatching
                }
            } else if (SemanticVersion.parse("6.5").compatibleWith(gradleVersion)) {
                // 6.5 -> 6.6
                if ((startParameter as StartParameterInternal).isWatchFileSystem) {
                    Result.WatchingUnstable
                } else {
                    Result.NotWatching
                }
            } else {
                Result.NotApplicable
            }
        }
}

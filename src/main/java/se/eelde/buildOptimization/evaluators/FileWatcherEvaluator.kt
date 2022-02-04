package se.eelde.buildOptimization.evaluators

import org.gradle.StartParameter
import org.gradle.api.internal.StartParameterInternal
import org.gradle.api.internal.project.DefaultProject
import org.gradle.internal.watch.vfs.WatchMode

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

            return if (SemanticVersion.parse("6.7").compatibleWith(gradleVersion)) {
                when ((startParameter as StartParameterInternal).watchFileSystemMode) {
                    WatchMode.ENABLED -> Result.Watching
                    WatchMode.DEFAULT -> Result.Watching
                    WatchMode.DISABLED -> Result.NotWatching
                    null -> Result.NotApplicable
                }
            } else  {
                Result.NotApplicable
            }
        }
}

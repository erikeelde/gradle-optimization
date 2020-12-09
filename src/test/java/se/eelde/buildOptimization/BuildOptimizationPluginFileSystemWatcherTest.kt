package se.eelde.buildOptimization

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class BuildOptimizationPluginFileSystemWatcherTest {

    @TempDir
    lateinit var testProjectDir: File

    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        buildFile = File(testProjectDir, "build.gradle").also {
            it.writeText(
                """
            plugins {
                id 'se.eelde.build_optimization'
            }
        """
            )
        }
    }

    @Test
    fun `test passing result with file system checker disabled on gradle 6_7_1`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
org.gradle.vfs.watch=true
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withGradleVersion("6.7.1")
            .withEnvironment(mapOf())
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .build()

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.SUCCESS)
        }
    }

    @Test
    fun `test failing result with file system checker disabled on gradle 6_7_1`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
org.gradle.vfs.watch=false
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withGradleVersion("6.7.1")
            .withEnvironment(mapOf())
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .buildAndFail()

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.FAILED)
        }
    }

    @Test
    fun `test passing result with file system checker disabled on gradle 6_5_1`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
org.gradle.unsafe.watch-fs=true
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withGradleVersion("6.5.1")
            .withEnvironment(mapOf())
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .build()

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.SUCCESS)
        }
    }

    @Test
    fun `test failing result with file system checker disabled on gradle 6_5_1`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
org.gradle.unsafe.watch-fs=false
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withGradleVersion("6.5.1")
            .withEnvironment(mapOf())
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .buildAndFail()

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.FAILED)
        }
    }

    @Test
    fun `test successful result with gradle 6_4`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
org.gradle.vfs.watch=false
org.gradle.unsafe.watch-fs=false
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withGradleVersion("6.4.1")
            .withEnvironment(mapOf())
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .build()

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.SUCCESS)
        }
    }
}

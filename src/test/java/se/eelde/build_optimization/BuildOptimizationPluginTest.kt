package se.eelde.build_optimization

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class BuildOptimizationPluginTest {

    @TempDir
    lateinit var testProjectDir: File

    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        buildFile = File(testProjectDir, "build.gradle").also {
            it.writeText("""
            plugins {
                id 'se.eelde.build_optimization'
            }
        """)
        }
    }

    @Test
    fun `test jvmstuf2f result with all optimizations enabled`() {

        buildFile.writeText("""
            plugins {
                id 'se.eelde.build_optimization'
            }
            
            buildOptimization {
                jvmMinMem '4GB'
            }
        """)

        File(testProjectDir, "gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true

org.gradle.jvmargs=-Xmx1g
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
                .withEnvironment(mapOf())
                .withProjectDir(testProjectDir)
                .withArguments("checkBuildOptimizations")
                .withPluginClasspath()
                .buildAndFail()

        assertTrue(build.output.contains("Expecting more jvm memory to be defined "), build.output)

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.FAILED)
        }
    }

    @Test
    fun `test jvmstuff result with all optimizations enabled`() {

        buildFile.writeText("""
            plugins {
                id 'se.eelde.build_optimization'
            }
            
            buildOptimization {
                jvmMinMem '4GB'
            }
        """)

        File(testProjectDir, "gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true

org.gradle.jvmargs=-Xmx4g
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
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
    fun `test successful result with all optimizations enabled`() {

        File(testProjectDir, "gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
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
    fun `test failing result with parallel buils disabled`() {

        File(testProjectDir, "gradle.properties").writeText("""
org.gradle.parallel=false
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
                .withEnvironment(mapOf())
                .withProjectDir(testProjectDir)
                .withArguments("checkBuildOptimizations")
                .withPluginClasspath()
                .buildAndFail()

        assertTrue(build.output.contains("Run with --parallel on the command-line"), build.output)

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.FAILED)
        }
    }

    @Test
    fun `test failing result with daemon disabled`() {

        File(testProjectDir, "gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=false
org.gradle.configureondemand=true
org.gradle.caching=true
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
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
    fun `test failing result with caching disabled`() {

        File(testProjectDir, "gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=false
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
                .withEnvironment(mapOf())
                .withProjectDir(testProjectDir)
                .withArguments("checkBuildOptimizations")
                .withPluginClasspath()
                .buildAndFail()

        assertTrue(build.output.contains("gradle.properties >> org.gradle.caching=true"), build.output)

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.FAILED)
        }
    }

    @Test
    fun `test task not added on ci`() {

        File(testProjectDir, "gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
                .withEnvironment(mapOf("CI" to "true", "CIRCLECI" to "true"))
                .withProjectDir(testProjectDir)
                .withArguments("checkBuildOptimizations")
                .withPluginClasspath()
                .buildAndFail()

        assertTrue(build.output.contains("Build running on CI - ignoring gradle optimization-checks."), build.output)

        assertThat(build.tasks.size).isEqualTo(0)
    }
}
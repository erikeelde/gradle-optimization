package se.eelde.build_optimization

import com.google.common.truth.Truth.assertThat
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class ParseDslTest {

    @TempDir
    lateinit var testProjectDir: File

    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        buildFile = File(testProjectDir, "build.gradle")
    }

    @Test
    fun `verify that dsl can be properly parsed`() {

        buildFile.writeText("""
            apply plugin: "se.eelde.ago"

            buildOptimization {
                jvmMinMem '4GB'
            }
        """)

        val project = ProjectBuilder.builder().withProjectDir(testProjectDir).build()
        (project as DefaultProject).evaluate()

        val buildOptimizationPlugin = project.plugins.getPlugin(BuildOptimizationPlugin::class.java) as BuildOptimizationPlugin
        val buildOptimizationPluginExtension = buildOptimizationPlugin.buildOptimizationPluginExtension!!

        assertThat(buildOptimizationPluginExtension.jvmMinMem).isEqualTo("4GB")
    }

    @Test
    fun `test successful result with all optimizations enabled`() {
        buildFile.writeText("""
            plugins {
                id 'se.eelde.ago'
            }
        """)

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
                .withArguments("androidGradleOptimizations")
                .withPluginClasspath()
                .build()

        assertThat(build.tasks[0].outcome).isEqualTo(TaskOutcome.SUCCESS)
    }
}
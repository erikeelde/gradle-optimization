package se.eelde.ago

import com.google.common.truth.Truth.assertThat
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

internal class ParseDslTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()

    private lateinit var buildFile: File

    @Before
    fun setup() {
        buildFile = testProjectDir.newFile("build.gradle")
    }

    @Test
    fun `verify that dsl can be properly parsed`() {

        buildFile.writeText("""
            apply plugin: "se.eelde.ago"

            ago {
                jvmMinMem '4GB'
                skipOptimizationsEnvVar 'SKIP_IT'
            }
        """)

        val project = ProjectBuilder.builder().withProjectDir(testProjectDir.root).build()
        (project as DefaultProject).evaluate()

        val agoPlugin = project.plugins.getPlugin(AgoPlugin::class.java) as AgoPlugin
        val agoPluginExtension = agoPlugin.agoPluginExtension!!

        assertThat(agoPluginExtension.jvmMinMem).isEqualTo("4GB")
        assertThat(agoPluginExtension.skipOptimizationsEnvVar).isEqualTo("SKIP_IT")
    }

    @Test
    fun `verify that optimizations can be skipped`() {
        buildFile.writeText("""
            apply plugin: "se.eelde.ago"

            ago {
                skipOptimizationsEnvVar 'SKIP_IT'
            }
        """)

        val project = ProjectBuilder.builder().withProjectDir(testProjectDir.root).build()
        (project as DefaultProject).evaluate()

        val agoPlugin = project.plugins.getPlugin(AgoPlugin::class.java) as AgoPlugin
        val agoPluginExtension = agoPlugin.agoPluginExtension!!

        assertThat(agoPluginExtension.skipOptimizationsEnvVar).isEqualTo("SKIP_IT")
    }

    @Test
    fun `test successful result with all optimizations enabled`() {
        buildFile.writeText("""
            plugins {
                id 'se.eelde.ago'
            }

            ago {
                skipOptimizationsEnvVar 'SKIP_IT'
            }
        """)

        testProjectDir.newFile("gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
                .withEnvironment(mapOf())
                .withProjectDir(testProjectDir.root)
                .withArguments("androidGradleOptimizations")
                .withPluginClasspath()
                .build()

        assertThat(build.tasks[0].outcome).isEqualTo(TaskOutcome.SUCCESS)
    }
}
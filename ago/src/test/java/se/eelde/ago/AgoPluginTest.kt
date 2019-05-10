package se.eelde.ago

import junit.framework.Assert.assertEquals
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.IOException

internal class AgoPluginTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()

    private lateinit var buildFile: File

    @Before
    @Throws(IOException::class)
    fun setup() {
        buildFile = testProjectDir.newFile("build.gradle")
    }

    @Test
    fun lol() {
    }

    @Test
    @Throws(IOException::class)
    fun testHelloWorldTask() {

        buildFile.writeText("""
            apply plugin: "se.eelde.ago"

            ago {
                mem '4GB'
            }
        """)

        val project = ProjectBuilder.builder().withProjectDir(testProjectDir.root).build()
        (project as DefaultProject).evaluate()

        val agoPlugin = project.plugins.getPlugin(AgoPlugin::class.java) as AgoPlugin

        assertEquals("4GB", agoPlugin.extension!!.mem)
    }
}
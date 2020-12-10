package se.eelde.buildOptimization

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BuildOptimizationPluginExtensionTest {

    @Test
    fun getJvmXmx() {
        val buildOptimizationPluginExtension = BuildOptimizationPluginExtension()
        buildOptimizationPluginExtension.jvmXmx = "5GB"
        assertEquals(Memory.Gigabyte(5), buildOptimizationPluginExtension.getJvmXmxMemory())

        buildOptimizationPluginExtension.jvmXmx = "4MB"
        assertEquals(Memory.Megabyte(4), buildOptimizationPluginExtension.getJvmXmxMemory())

        buildOptimizationPluginExtension.jvmXmx = "2kB"
        assertEquals(Memory.Kilobyte(2), buildOptimizationPluginExtension.getJvmXmxMemory())

        buildOptimizationPluginExtension.jvmXmx = "8GB"
        assertEquals(Memory.Gigabyte(8), buildOptimizationPluginExtension.getJvmXmxMemory())
    }

    @Test
    fun getJvmXms() {
        val buildOptimizationPluginExtension = BuildOptimizationPluginExtension()
        buildOptimizationPluginExtension.jvmXms = "5GB"
        assertEquals(Memory.Gigabyte(5), buildOptimizationPluginExtension.getJvmXmsMemory())

        buildOptimizationPluginExtension.jvmXms = "4MB"
        assertEquals(Memory.Megabyte(4), buildOptimizationPluginExtension.getJvmXmsMemory())

        buildOptimizationPluginExtension.jvmXms = "2kB"
        assertEquals(Memory.Kilobyte(2), buildOptimizationPluginExtension.getJvmXmsMemory())

        buildOptimizationPluginExtension.jvmXms = "8GB"
        assertEquals(Memory.Gigabyte(8), buildOptimizationPluginExtension.getJvmXmsMemory())
    }
}

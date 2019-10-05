package se.eelde.build_optimization

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BuildOptimizationPluginExtensionTest {

    @Test
    fun getMaxJvmMem() {
        val buildOptimizationPluginExtension = BuildOptimizationPluginExtension()
        buildOptimizationPluginExtension.jvmMinMem = "5GB"
        assertEquals(Memory.Gigabyte(5), buildOptimizationPluginExtension.getMaxJvmMem())

        buildOptimizationPluginExtension.jvmMinMem = "4MB"
        assertEquals(Memory.Megabyte(4), buildOptimizationPluginExtension.getMaxJvmMem())

        buildOptimizationPluginExtension.jvmMinMem = "2kB"
        assertEquals(Memory.Kilobyte(2), buildOptimizationPluginExtension.getMaxJvmMem())

        buildOptimizationPluginExtension.jvmMinMem = "8GB"
        assertEquals(Memory.Gigabyte(8), buildOptimizationPluginExtension.getMaxJvmMem())
    }
}
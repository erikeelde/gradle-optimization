package se.eelde.ago

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test


class JvmArgsParserTest {

    @Test
    fun testNoJvmMaxMem() {
        val jvmArgParser = JvmArgsParser()
        assertEquals(Memory.UNDEFINED, jvmArgParser.parseMaxJmvMem(""))
        assertEquals(Memory.UNDEFINED, jvmArgParser.parseMaxJmvMem("-XX:MaxPermSize=2048m -XX:+HeapDumpOnOutOfMemoryError"))
    }

    @Test
    fun testMaxJvmMaxMem() {
        val jvmArgParser = JvmArgsParser()
        assertEquals(Memory.Megabyte(2000), jvmArgParser.parseMaxJmvMem("-Xmx2000m"))
        assertEquals(Memory.Megabyte(2000), jvmArgParser.parseMaxJmvMem("a -Xmx2000m a"))
        assertEquals(Memory.Megabyte(2000), jvmArgParser.parseMaxJmvMem("-Xmx2000m -XX:MaxPermSize=2048m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=CRLF"))
    }

    @Test
    fun testMaxJvmMaxMem2() {
        val jvmArgParser = JvmArgsParser()
        assertEquals(Memory.Kilobyte(1), jvmArgParser.parseMaxJmvMem("-Xmx1k"))
        assertEquals(Memory.Kilobyte(2), jvmArgParser.parseMaxJmvMem("-Xmx2K"))
        assertEquals(Memory.Megabyte(3), jvmArgParser.parseMaxJmvMem("-Xmx3m"))
        assertEquals(Memory.Megabyte(4), jvmArgParser.parseMaxJmvMem("-Xmx4M"))
        assertEquals(Memory.Gigabyte(5), jvmArgParser.parseMaxJmvMem("-Xmx5g"))
        assertEquals(Memory.Gigabyte(6), jvmArgParser.parseMaxJmvMem("-Xmx6G"))
    }

    @Test
    fun testNoFileEncoding() {
        val jvmArgParser = JvmArgsParser()
        assertNull(jvmArgParser.parseFileEncoding(""))
        assertNull(jvmArgParser.parseFileEncoding("-XX:MaxPermSize=2048m -XX:+HeapDumpOnOutOfMemoryError"))
    }

    @Test
    fun testFileEncoding() {
        val jvmArgParser = JvmArgsParser()
        assertEquals(Charsets.UTF_8, jvmArgParser.parseFileEncoding("-Dfile.encoding=UTF-8"))
        assertEquals(Charsets.UTF_8, jvmArgParser.parseFileEncoding("a -Dfile.encoding=UTF-8 a"))
        assertEquals(Charsets.UTF_8, jvmArgParser.parseFileEncoding("-Xmx2000m -XX:MaxPermSize=2048m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8"))
    }


}
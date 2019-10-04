package se.eelde.ago

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class JvmArgsParserTest {

    @Test
    fun testNoJvmMaxMem() {
        val jvmArgParser = JvmArgsParser()
        assertThat(jvmArgParser.parseMaxJmvMem("")).isEqualTo(Memory.UNDEFINED)
        assertThat(jvmArgParser.parseMaxJmvMem("-XX:MaxPermSize=2048m -XX:+HeapDumpOnOutOfMemoryError")).isEqualTo(Memory.UNDEFINED)
    }

    @Test
    fun testMaxJvmMaxMem() {
        val jvmArgParser = JvmArgsParser()
        assertThat(jvmArgParser.parseMaxJmvMem("-Xmx2000m")).isEqualTo(Memory.Megabyte(2000))
        assertThat(jvmArgParser.parseMaxJmvMem("a -Xmx2000m a")).isEqualTo(Memory.Megabyte(2000))
        assertThat(jvmArgParser.parseMaxJmvMem("-Xmx2000m -XX:MaxPermSize=2048m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=CRLF")).isEqualTo(Memory.Megabyte(2000))
    }

    @Test
    fun testMaxJvmMaxMem2() {
        val jvmArgParser = JvmArgsParser()
        assertThat(jvmArgParser.parseMaxJmvMem("-Xmx1k")).isEqualTo(Memory.Kilobyte(1))
        assertThat(jvmArgParser.parseMaxJmvMem("-Xmx2K")).isEqualTo(Memory.Kilobyte(2))
        assertThat(jvmArgParser.parseMaxJmvMem("-Xmx3m")).isEqualTo(Memory.Megabyte(3))
        assertThat(jvmArgParser.parseMaxJmvMem("-Xmx4M")).isEqualTo(Memory.Megabyte(4))
        assertThat(jvmArgParser.parseMaxJmvMem("-Xmx5g")).isEqualTo(Memory.Gigabyte(5))
        assertThat(jvmArgParser.parseMaxJmvMem("-Xmx6G")).isEqualTo(Memory.Gigabyte(6))
    }

    @Test
    fun testNoFileEncoding() {
        val jvmArgParser = JvmArgsParser()
        assertThat(jvmArgParser.parseFileEncoding("")).isNull()
        assertThat(jvmArgParser.parseFileEncoding("-XX:MaxPermSize=2048m -XX:+HeapDumpOnOutOfMemoryError")).isNull()
    }

    @Test
    fun testFileEncoding() {
        val jvmArgParser = JvmArgsParser()
        assertThat(jvmArgParser.parseFileEncoding("-Dfile.encoding=UTF-8")).isEqualTo(Charsets.UTF_8)
        assertThat(jvmArgParser.parseFileEncoding("a -Dfile.encoding=UTF-8 a")).isEqualTo(Charsets.UTF_8)
        assertThat(jvmArgParser.parseFileEncoding("-Xmx2000m -XX:MaxPermSize=2048m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8")).isEqualTo(Charsets.UTF_8)
    }
}
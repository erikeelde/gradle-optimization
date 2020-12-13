package se.eelde.buildOptimization

import java.nio.charset.Charset

enum class CheckSeverity {
    ENABLED_ENFORCED,
    ENABLED_RECOMMENDED
}

sealed class Check(val aDefault: CheckSeverity, val link: String, val hints: List<String>, val praise: String) {

    class Parallel : Check(
        aDefault = CheckSeverity.ENABLED_ENFORCED,
        link = "https://guides.gradle.org/performance/",
        hints = listOf(
            "Builds are not being paralleled",
            "To enable: add 'org.gradle.parallel=true' to your gradle.properties",
            "Run with --parallel on the command-line"
        ),
        praise = "Using parallel :+1"
    )

    class Daemon : Check(
        aDefault = CheckSeverity.ENABLED_ENFORCED,
        link = "https://docs.gradle.org/current/userguide/gradle_daemon.html",
        hints = listOf(
            "Builds are not run using the gradle daemon",
            "To enable: add 'org.gradle.daemon=true' to your gradle.properties",
            "Run with --daemon on the command-line"
        ),
        praise = "Using daemon :+1"
    )

    class Cache : Check(
        aDefault = CheckSeverity.ENABLED_ENFORCED,
        link = "https://docs.gradle.org/current/userguide/build_cache.html",
        hints = listOf(
            "Builds are not making use of the gradle cache",
            "To enable: add 'org.gradle.caching=true' to your gradle.properties",
            "Run with --build-cache on the command-line"
        ),
        praise = "Using cache :+1"
    )

    class ConfigureOnDemand : Check(
        aDefault = CheckSeverity.ENABLED_RECOMMENDED,
        link = "https://docs.gradle.org/current/userguide/multi_project_builds.html#sec:configuration_on_demand",
        hints = listOf(
            "Builds are not using on demand configuration",
            "To enable: add 'org.gradle.configureondemand=true' to your gradle.properties",
            "Run with --configure-on-demand on the command-line"
        ),
        praise = "Using configure on demand :+1"
    )

    class FileSystemWatcher : Check(
        aDefault = CheckSeverity.ENABLED_ENFORCED,
        link = "https://blog.gradle.org/introducing-file-system-watching",
        hints = listOf(
            "Builds are not using file system watching",
            "To enable: add 'org.gradle.vfs.watch=true' to your gradle.properties",
            "Run with --watch-fs on the command-line"
        ),
        praise = "File system watcher enabled :+1"
    )

    class JvmXmx(val size: Memory) : Check(
        aDefault = CheckSeverity.ENABLED_ENFORCED,
        link = "https://docs.gradle.org/current/userguide/build_environment.html#sec:configuring_jvm_memory",
        hints = listOf(
            "Expecting more jvm xmx memory to be defined",
            "gradle.properties >> org.gradle.jvmargs=-Xmx2g"
        ),
        praise = "You have ample jvm xmx memory assigned :+1"
    )

    class JvmXms(val size: Memory) : Check(
        aDefault = CheckSeverity.ENABLED_ENFORCED,
        link = "https://docs.gradle.org/current/userguide/build_environment.html#sec:configuring_jvm_memory",
        hints = listOf(
            "Expecting more jvm xms memory to be defined",
            "gradle.properties >> org.gradle.jvmargs=-Xms500m"
        ),
        praise = "You have ample jvm xms memory assigned :+1"
    )

    class UTF8FileEncoding(val charset: Charset) : Check(
        aDefault = CheckSeverity.ENABLED_RECOMMENDED,
        link = "https://github.com/gradle/gradle/issues/2270",
        hints = listOf(
            "Default file encoding should be defined as ${charset.name()}",
            "gradle.properties >> org.gradle.jvmargs=-Dfile.encoding=UTF-8"
        ),
        praise = "File encoding defined :+1"
    )

    class VersionsPlugin : Check(
        aDefault = CheckSeverity.ENABLED_RECOMMENDED,
        link = "https://github.com/ben-manes/gradle-versions-plugin",
        hints = listOf("A way to monitor dependency updates is highly recommended"),
        praise = "Version update method detected :+1"
    )
}

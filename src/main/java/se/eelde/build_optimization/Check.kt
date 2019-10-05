package se.eelde.build_optimization

import java.nio.charset.Charset

enum class CheckSeverity {
    ENABLED_ENFORCED,
    ENABLED_RECOMMENDED
}

sealed class Check(val aDefault: CheckSeverity, val link: String, val hints: List<String>, val praise: String) {

    class Parallel : Check(aDefault = CheckSeverity.ENABLED_ENFORCED,
            link = "https://guides.gradle.org/performance/",
            hints = listOf("If possible, enable org.gradle.parallel",
                    "Run with --parallel on the command-line"),
            praise = "Using parallel :+1")

    class Daemon : Check(aDefault = CheckSeverity.ENABLED_ENFORCED,
            link = "https://docs.gradle.org/current/userguide/gradle_daemon.html",
            hints = listOf("Don't disable the daemon: org.gradle.daemon=false",
                    "Run with --daemon and --no-daemon on the command-line"),
            praise = "Using daemon :+1")

    class Cache : Check(aDefault = CheckSeverity.ENABLED_ENFORCED,
            link = "https://docs.gradle.org/current/userguide/build_cache.html",
            hints = listOf("gradle.properties >> org.gradle.caching=true",
                    "Run with --build-cache or --no-build-cache on the command-line"),
            praise = "Using cache :+1")

    class ConfigureOnDemand : Check(aDefault = CheckSeverity.ENABLED_RECOMMENDED,
            link = "https://docs.gradle.org/current/userguide/multi_project_builds.html#sec:configuration_on_demand",
            hints = listOf("gradle.properties >> org.gradle.configureondemand=true",
                    "Run with --configure-on-demand, --no-configure-on-demand on the command-line"),
            praise = "Using configure on demand :+1")

    class Memory(val size: se.eelde.build_optimization.Memory) : Check(aDefault = CheckSeverity.ENABLED_ENFORCED,
            link = "https://docs.gradle.org/current/userguide/build_environment.html#sec:configuring_jvm_memory",
            hints = listOf("Expecting more jvm memory to be defined",
                    "gradle.properties >> org.gradle.jvmargs=-Xmx2g"),
            praise = "You have ample jvm memory assigned :+1")

    class UTF8FileEncoding(val charset: Charset) : Check(aDefault = CheckSeverity.ENABLED_RECOMMENDED,
            link = "https://github.com/gradle/gradle/issues/2270",
            hints = listOf("Default file encoding should be defined as ${charset.name()}",
                    "gradle.properties >> org.gradle.jvmargs=-Dfile.encoding=UTF-8"),
            praise = "File encoding defined :+1")

    class VersionsPlugin : Check(aDefault = CheckSeverity.ENABLED_RECOMMENDED,
            link = "https://github.com/ben-manes/gradle-versions-plugin",
            hints = listOf("A way to monitor dependency updates is highly recommended"),
            praise = "Version update method detected :+1")
}
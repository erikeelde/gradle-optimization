# Gradle build optimizations

A gradle plugin that will check to make sure some gradle performance optimizations are in place.
It will automatically attach itself as a task to all gradle invocations and verify that no time is wasted waiting for builds without all optimizations enabled.
Default configuration will crash if not all safe optimisations are in place.

Check for output in the gradle build logs for helpful tips and additional reading.

Plugin published [here](https://plugins.gradle.org/plugin/se.eelde.build-optimizations)

##### Use it
```kotlin
plugins {
  id("se.eelde.build-optimizations") version "0.2.0"
}
```

##### Test it (locally):
```shell script
./gradlew :publishMavenLocal
```

```kotlin
buildscript {
    dependencies {
        classpath("se.eelde.build-optimizations:se.eelde.build-optimizations.gradle.plugin:0.2.0")
    }
}

apply(plugin= "se.eelde.build-optimizations")
```

### Specifics: 

Dsl is [here](src/test/java/se/eelde/build_optimization/ParseDslTest.kt)
```kotlin
buildOptimization {
    jvmXmx = "2GB"
    jvmXms = "500MB"
}
```

Checks are [here](src/main/java/se/eelde/build_optimization/Check.kt)

* Require that daemon execution is enabled
* Require that parallel execution is enabled
* Require that builds are using gradle caches
* Hint to enable configuration on demand
* Default File encoding
* Require a specific setting of jvmXmx (default -Xmx=2g)
* Require a specific setting of jvmXms (default -Xms=500m)
* Require file system watcher to be enabled after gradle 6.5

# Gradle build optimizations

A gradle plugin that will check to make sure some gradle performance optimizations are in place.
Default configuration will crash if not all safe optimisations are in place.

Check for output in the gradle build logs for helpful tips.

Plugin published [here](https://plugins.gradle.org/plugin/se.eelde.build-optimizations)

##### Deploy:
```
./gradlew :publishMavenLocal
```

##### Test it:
```
plugins {
  id("se.eelde.build-optimizations") version "0.1"
}
```


### Specifics: 

Dsl is [here](src/test/java/se/eelde/build_optimization/ParseDslTest.kt)

Checks are [here](src/main/java/se/eelde/build_optimization/Check.kt)

* Daemon execution
* Paralell Execution
* Gradle caches
* Configure on demand  
* JvmMemory 
* Default File encoding
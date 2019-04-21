# android-gradle-optimization

A gradle plugin that will check to make sure some gradle performance optimizations are in place.
Default configuration will crash if not all safe optimisations are in place.

Check for output in the gradle build logs for helpful tips.

##### Deploy:
```
./gradlew :ago:publishMavenLocal
```

##### Test it:
```
buildscript {
    dependencies {
        classpath 'se.eelde:ago:1-SNAPSHOT'
    }
}

apply plugin: 'se.eelde.ago'
```

### Specifics: 

Checks are [here](ago/src/main/java/se/eelde/ago/Check.kt)

* Daemon execution
* Paralell Execution
* Gradle caches
* Configure on demand  
* JvmMemory
* Default File encoding
# android-gradle-optimization

A gradle plugin that will check to make sure some gradle performance optimizations are in place.
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

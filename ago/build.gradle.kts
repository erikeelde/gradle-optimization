plugins {
    id("java")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.10.1"
    id("org.jetbrains.kotlin.jvm") version "1.3.50"
    id("maven-publish")
    id("signing")
    // id("org.jetbrains.dokka") version "0.9.18"
}

apply { from("../ktlint.gradle") }

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    target.compilations.all {
        compileKotlinTask.kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(gradleApi())
    testImplementation("junit:junit:4.12")
    testImplementation(gradleTestKit())
    testImplementation("com.google.truth:truth:1.0")
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
}

// Use java-gradle-plugin to generate plugin descriptors and specify plugin ids
gradlePlugin {
    plugins {
        create("agoPlugin") {
            id = "se.eelde.ago"
            displayName = "Make sure gradle is optimized"
            description = "Gradle plugin where you can configure optimizations that should be in place so that you will not run unoptimized gradle builds."
            implementationClass = "se.eelde.ago.AgoPlugin"
        }
    }
}

// The configuration example below shows the minimum required properties
// configured to publish your plugin to the plugin portal
pluginBundle {
    website = "http://www.eelde.se/"
    vcsUrl = "https://github.com/erikeelde/android-gradle-optimization"
    description = "A plugin that verifies that optimizations are enabled on development machines."
    tags = mutableListOf("optimization")

    (plugins) {
        "agoPlugin" {
            // id is captured from java-gradle-plugin configuration
            displayName = "Gradle optimizations plugin"
            version = "0.1"
        }
    }
}

group = "se.eelde"
version = "0.1-SNAPSHOT"

publishing {
    publications {
        register("mavenJava", MavenPublication::class.java) {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])

            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }
}


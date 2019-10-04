plugins {
    id("com.github.ben-manes.versions") version "0.24.0"
    id("java")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.10.1"
    id("org.jetbrains.kotlin.jvm") version "1.3.50"
    id("maven-publish")
    id("signing")
}

buildscript {
    dependencies {
    }

    repositories {
        jcenter()
    }
}

allprojects {
    repositories {
        jcenter()
    }
}


apply { from("ktlint.gradle") }

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
        create("gradleOptimizationsPlugin") {
            id = "se.eelde.build-optimizations"
            group = "se.eelde"
            implementationClass = "se.eelde.ago.AgoPlugin"
        }
    }
}

version = "0.1"

pluginBundle {
    website = "http://www.eelde.se/"
    vcsUrl = "https://github.com/erikeelde/gradle-optimization"
    description = "A plugin that verifies that optimizations are enabled on development machines."
    tags = mutableListOf("optimization")

    (plugins) {
        "gradleOptimizationsPlugin" {
            // id is captured from java-gradle-plugin configuration
            displayName = "Assert build optimizations are in place to assure you are running optimized builds."
            description = """Verify that builds on developer machines (plugin will sidestep checks on CI) are running with expected optimizations enabled.
                Daemon builds, parallel, xmx etc.

                Useful for new developers, new machines, if you accidentally delete your .gradle folder etc.

                A good way to get new optimizations from gradle to be adopted across users of your repository / your organization.
            """.trimMargin()
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])


            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }
}

/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.databind)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "issue-engine"
        }
    }
    repositories {
        mavenLocal()
    }
}
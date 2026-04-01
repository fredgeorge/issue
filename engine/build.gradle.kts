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
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.databind)
    implementation(libs.jackson.kotlin)
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
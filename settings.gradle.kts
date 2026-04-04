/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version providers.gradleProperty("kotlinPluginVersion").get()
    }
}

rootProject.name = "issue"
include("engine", "test-support", "tests", "persistence")

/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

plugins {
    // Using aliases from the .toml
    // IMPORTANT: Use all aliases or the false plugins below, not both!
//    kotlin("jvm") version "2.3.20" apply false
//    kotlin("plugin.serialization") version "2.3.20" apply false
}

allprojects {
    group = "com.nrkei.project"
    version = "1.1.1"
}

val javaVersion = providers.gradleProperty("javaVersion").map(String::toInt).get()

subprojects {
    plugins.withId("java") {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(javaVersion))
            }
        }
    }
}

/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

allprojects {
    group = "com.nrkei.project"
    version = "1.1.8"
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

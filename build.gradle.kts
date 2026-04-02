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
    version = "1.0.3"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}
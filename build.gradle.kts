/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

plugins {
    kotlin("jvm") version "2.3.20" apply false
}

allprojects {
    group = "com.nrkei.project"
    version = "1.0.2"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}
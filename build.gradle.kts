import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    // Kotlin plugin will be applied in subprojects where needed
    id("org.jetbrains.kotlin.jvm") version "2.3.0" apply false
}

repositories {
    mavenCentral()
}

subprojects {
    // 1 – make sure the Kotlin-JVM plugin is applied
    apply(plugin = "org.jetbrains.kotlin.jvm")

    // Kotlin → compile *to* 21  ← FIXED
    extensions.configure<KotlinJvmProjectExtension> {
        jvmToolchain(21)
    }

    // Kotlin byte-code level (kept from before)
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }

    repositories { mavenCentral() }
}

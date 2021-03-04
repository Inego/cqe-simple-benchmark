import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.googlecode.cqengine:cqengine:3.6.0")
}

val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions.useIR = true

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
    kotlin("plugin.jpa") version "1.4.32"
    id("com.github.node-gradle.node") version "3.1.1"
}

group = "fr.dalae"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

tasks.bootRun {
    main = "fr.dalae.fileman.FilemanApplicationKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("commons-io:commons-io:2.8.0")
    implementation("com.h2database:h2")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}

val tempDir = file("$buildDir/test")
tasks.test {
    doFirst {
        delete(tempDir)
    }
}

node {
    nodeProjectDir.set(file("$projectDir/src/main/webapp"))
}

tasks.create("npmBuildCustom", com.github.gradle.node.npm.task.NpmTask::class.java) {
    args.addAll("run", "build")
    dependsOn(tasks.npmInstall)
}

tasks.create("npmDeployToBack", Copy::class.java) {
    from("src/main/webapp/build")
    into("build/resources/main/static/.")
    dependsOn("npmBuildCustom")
}

tasks.compileJava {
    //dependsOn("npmDeployToBack")
}
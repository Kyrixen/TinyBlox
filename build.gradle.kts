// Using Gradle 4.10.3

plugins {
    java
    application
}

// Java 8 compatibility
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "org.kyrixen.Main" // Main Class
}

// Repositories for dependencies
repositories {
    mavenCentral()
}

dependencies {
    // None for now
}

// For better Garbage Collector (GC)
tasks.named<JavaExec>("run") {
    jvmArgs(
        "-XX:+UseG1GC",
        "-XX:MaxGCPauseMillis=20",
        "-XX:InitiatingHeapOccupancyPercent=35",
        "-verbose:gc"
        //"-XX:+PrintGCDetails",
        //"-XX:+PrintGCTimeStamps"
    )
}

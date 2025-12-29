plugins {
    java
    application
}

group = "org.kyrixen"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

val lwjglVersion = "3.3.6" // latest stable
val lwjglNatives = when {
    System.getProperty("os.name").lowercase().contains("windows") -> "natives-windows"
    System.getProperty("os.name").lowercase().contains("linux") -> "natives-linux"
    System.getProperty("os.name").lowercase().contains("mac") -> "natives-macos"
    else -> throw GradleException("Unsupported OS")
}

dependencies {
    // Core and bindings
    implementation("org.lwjgl:lwjgl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-opengl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-stb:$lwjglVersion") // For PNG loading

    // Platform-specific natives
    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
}

application {
    mainClass.set("org.kyrixen.Main")
}

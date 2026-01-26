plugins {
    id("com.gradleup.shadow") version "9.3.0"
}

extra.set("targetJava", 11)

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(project(":bukkit"))
    implementation(project(":core"))
    implementation(project(":velocity"))
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    manifest {
        attributes(
            "Implementation-Title" to rootProject.name,
            "Implementation-Version" to rootProject.version
        )
    }

    val prefix = "${rootProject.group}.libs"

    relocate("okhttp3", "$prefix.okhttp3")
    relocate("okio", "$prefix.okio")
    relocate("com.squareup.moshi", "$prefix.moshi")
    relocate("org.bstats", "$prefix.bstats")

    archiveBaseName.set(rootProject.name.toString())
    archiveClassifier.set("")
    archiveVersion.set(rootProject.version.toString())
}

tasks.test {
    useJUnitPlatform()
}
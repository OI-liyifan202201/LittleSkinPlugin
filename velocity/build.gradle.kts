extra.set("targetJava", 11)

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.0.0")
    // annotationProcessor("com.velocitypowered:velocity-api:3.0.0")

    implementation("org.bstats:bstats-velocity:3.1.0")
    implementation(project(":core"))
}

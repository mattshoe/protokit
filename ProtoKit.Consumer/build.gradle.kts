plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp)
}

dependencies {
    ksp(project(":ProtoKit.Processor"))
    compileOnly(project(":ProtoKit.Annotations"))
    implementation(project(":ProtoKit.Runtime"))
    testImplementation(kotlin("test"))
}
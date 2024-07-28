plugins {
    kotlin("jvm")
    id("maven-publish")
    signing
}

ext {
    set("ARTIFACT_ID", "ProtoKit.Processor")
    set("PUBLICATION_NAME", "protokitProcessor")
}

dependencies {
    implementation(libs.ksp.api)
    implementation(libs.mustache)
    implementation(libs.stratify)
    implementation(project(":ProtoKit.Annotations"))
}
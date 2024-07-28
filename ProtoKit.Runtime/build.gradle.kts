plugins {
    kotlin("jvm")
    id("maven-publish")
    signing
}

ext {
    set("ARTIFACT_ID", "ProtoKit.Runtime")
    set("PUBLICATION_NAME", "protokitRuntime")
}

dependencies {
    testImplementation(kotlin("test"))
}
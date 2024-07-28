plugins {
    kotlin("jvm")
    id("maven-publish")
    signing
}

ext {
    set("ARTIFACT_ID", "ProtoKit.Annotations")
    set("PUBLICATION_NAME", "protokitAnnotations")
}

dependencies {
    testImplementation(kotlin("test"))
}
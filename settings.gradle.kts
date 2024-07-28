plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "ProtoKit.Root"
include("ProtoKit.Annotations")
include("ProtoKit.Processor")
include("ProtoKit.Runtime")
include("ProtoKit.Consumer")

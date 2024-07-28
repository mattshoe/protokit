plugins {
    kotlin("jvm") version "2.0.0"
    id("maven-publish")
    signing
}

val GROUP_ID: String = project.properties["group.id"].toString()
val VERSION: String = project.properties["version"].toString()

dependencies {
    implementation(libs.kotlinx.coroutines)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }

    group = GROUP_ID
    version = VERSION

    plugins.withId("org.jetbrains.kotlin.jvm") {
        kotlin {
            jvmToolchain(11)
        }
    }

    tasks.withType<Test> {
        useJUnit()
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        reports {
            html.required = true
            junitXml.required = true
        }
    }
}

subprojects {
    plugins.withId("java") {
        java {
            withJavadocJar()
            withSourcesJar()
        }
    }

    afterEvaluate {
        (findProperty("PUBLICATION_NAME") as? String)?.let { publicationName ->
            val subArtifactId = findProperty("ARTIFACT_ID") as String

            plugins.withId("maven-publish") {
                publishing {
                    publications {
                        repositories {
                            mavenLocal()
                        }

                        create<MavenPublication>(publicationName) {
                            from(components["java"])
                            groupId = rootProject.group.toString()
                            artifactId = subArtifactId
                            version = rootProject.version.toString()
                            pom {
                                name = "ProtoKit"
                                description = """
                                    ProtoKit is a Kotlin Symbol Processing (KSP) plugin designed to streamline the 
                                    generation of Protocol Buffers (.proto) files from annotated Kotlin classes. 
                                    ProtoKit automates the creation and maintenance of protobuf schemas, ensuring 
                                    consistency and reducing manual effort.
                                """.trimIndent()
                                url = "https://github.com/mattshoe/protokit"
                                properties = mapOf(
                                    "myProp" to "value"
                                )
                                packaging = "jar"
                                inceptionYear = "2024"
                                licenses {
                                    license {
                                        name = "The Apache License, Version 2.0"
                                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                                    }
                                }
                                developers {
                                    developer {
                                        id = "mattshoe"
                                        name = "Matthew Shoemaker"
                                        email = "mattshoe81@gmail.com"
                                    }
                                }
                                scm {
                                    connection = "scm:git:git@github.com:mattshoe/protokit.git"
                                    developerConnection = "scm:git:git@github.com:mattshoe/protokit.git"
                                    url = "https://github.com/mattshoe/protokit"
                                }
                            }
                        }


                        signing {
                            val signingKey = providers.environmentVariable("GPG_SIGNING_KEY")
                            val signingPassphrase = providers.environmentVariable("GPG_SIGNING_PASSPHRASE")
                            if (signingKey.isPresent && signingPassphrase.isPresent) {
                                useInMemoryPgpKeys(signingKey.get(), signingPassphrase.get())
                                sign(publishing.publications[publicationName])
                            }
                        }
                    }
                }
            }

            tasks.register<Zip>("generateZip") {
                val publishTask = tasks.named(
                    "publish${publicationName.replaceFirstChar { it.uppercaseChar() }}PublicationToMavenLocalRepository",
                    PublishToMavenRepository::class.java
                )
                from(publishTask.map { it.repository.url })
                archiveFileName.set("${publicationName}_${VERSION}.zip")
            }
        }
    }
}
plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
}

group = "io.github.lory1990"
version = project.findProperty("version")?.toString() ?: "0.0.1"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = "java-datocms"
            version = project.version.toString()

            pom {
                name.set("DatoCMS Java Client")
                description.set("A lightweight Java client library for interacting with the DatoCMS GraphQL API")
                url.set("https://github.com/Lory1990/java-datocms")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("Lory1990")
                        name.set("Lorenzo De Francesco")
                        email.set("Lory1990@users.noreply.github.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/Lory1990/java-datocms.git")
                    developerConnection.set("scm:git:ssh://github.com/Lory1990/java-datocms.git")
                    url.set("https://github.com/Lory1990/java-datocms")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            credentials {
                username = project.findProperty("sonatypeUsername")?.toString() ?: System.getenv("SONATYPE_USERNAME")
                password = project.findProperty("sonatypePassword")?.toString() ?: System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
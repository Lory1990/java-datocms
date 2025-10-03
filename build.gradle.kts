plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
    id("com.vanniktech.maven.publish") version "0.28.0"
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

mavenPublishing {
    coordinates("io.github.lory1990", "java-datocms", version.toString())

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

    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}
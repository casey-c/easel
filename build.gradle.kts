plugins {
    java
    `maven-publish`
    signing
}

group = "io.github.casey-c"
version = "0.0.2"

/*
Setup environment variables
* stsInstallLocation should point to the steam install directory
* compileOnlyLibs should point to a directory containing any JARs you reference
    - e.g. this directory should have desktop-1.0.jar, ModTheSpire.jar, BaseMod.jar
    - NOTE: these compileOnlyLibs are not included in the JAR, so you will get runtime
      errors if you try and call code that won't exist on the client.
 */
var stsInstallLocation: String = System.getenv("STS_INSTALL")
var compileOnlyLibs: String = System.getenv("STS_MODDING_LIB")

// Uses the value written in settings.gradle
var modName: String = rootProject.name

dependencies {
    compileOnly(fileTree(compileOnlyLibs))
}

// --------------------------------------------------------------------------------

tasks.register<Jar>("buildJAR") {
    group = "Slay the Spire"
    description = "Builds a fat (includes runtime dependencies) JAR in the build/libs folder"

    // Main code
    from(sourceSets.main.get().output)

    // Any runtime dependencies (e.g. from mavenCentral(), local JARs, etc.)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter {
            it.name.endsWith("jar")
        }.map {
            zipTree(it)
        }
    })
}

tasks.register<Copy>("buildAndCopyJAR") {
    group = "Slay the Spire"
    description = "Copies the JAR from your build/libs folder into your \$STS_INSTALL location"

    dependsOn("buildJAR")

    from("build/libs/$modName-$version.jar")
    into("$stsInstallLocation\\mods")
}

// --------------------------------------------------------------------------------

java {
    withSourcesJar()
    withJavadocJar()
}

artifacts {
    add("archives", tasks["sourcesJar"])
    add("archives", tasks["javadocJar"])
}

publishing {
    publications {
        create<MavenPublication>("easel") {
            from(components["java"])

            pom {
                name.set("easel")
                description.set("A UI library for Slay the Spire mods")
                url.set("https://github.com/casey-c/easel")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/mit-license.php")
                    }
                }

                developers {
                    developer {
                        name.set("Casey Conway")
                        email.set("16923456+casey-c@users.noreplay.github.com")
                        organization.set("None")
                        organizationUrl.set("https://github.com/casey-c")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/casey-c/easel.git")
                    developerConnection.set("scm:git:ssh://github.com/casey-c/easel.git")
                    url.set("https://github.com/casey-c/easel")
                }

            }
        }
    }

    repositories {
        maven {
            name = "easel"
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}

signing {
    sign(publishing.publications["easel"])
}

// --------------------------------------------------------------------------------
// Note to self (steps for publishing to maven central):
// See: https://central.sonatype.org/publish/publish-manual/
// 1. Run the publishToMavenLocal task to build up the repo in ~/.m2/repositories/io/github/casey-c/easel
//     - the local repo should contain the javadoc, source, and main library jars as well as automatic gpg signed version
// 2. Build a "bundled" jar containing everything in this folder
//     - $ jar -cvf bundle.jar *
// 3. Upload to https://s01.oss.sonatype.org/ (Staging upload -> Artifact bundle)
// 4. Verify that it closes correctly / click release button to sync with maven central
// 5. ???
// 6. profit?
plugins {
    java
    `maven-publish`
}

group = "com.github.casey-c"
version = "0.0.1"

/*
Setup environment variables
* stsInstallLocation should point to the steam install directory
* compileOnlyLibs should point to a directory containing any JARs you reference
    - e.g. this directory should have desktop-1.0.jar, ModTheSpire.jar, BaseMod.jar
    - NOTE: these compileOnlyLibs are not included in the JAR, so you will get runtime
      errors if you try and call code that won't exist on the client.
 */
//var stsInstallLocation: String = System.getenv("STS_INSTALL")
//var compileOnlyLibs: String = System.getenv("STS_MODDING_LIB")

// Uses the value written in settings.gradle
var modName: String = rootProject.name

//dependencies {
//    compileOnly(fileTree(compileOnlyLibs))
//}

// --------------------------------------------------------------------------------

//tasks.register<Jar>("buildJavadocJAR") {
//    group = "Slay the Spire"
//    description = "Builds the javadoc jar"
//
//    dependsOn("javadoc")
//    archiveClassifier.set("javadoc")
//}
//
//tasks.register<Jar>("buildJAR") {
//    group = "Slay the Spire"
//    description = "Builds a fat (includes runtime dependencies) JAR in the build/libs folder"
//
//    // Main code
//    from(sourceSets.main.get().output)
//
//    // Any runtime dependencies (e.g. from mavenCentral(), local JARs, etc.)
//    dependsOn(configurations.runtimeClasspath)
//    from({
//        configurations.runtimeClasspath.get().filter {
//            it.name.endsWith("jar")
//        }.map {
//            zipTree(it)
//        }
//    })
//}
//
//tasks.register<Copy>("buildAndCopyJAR") {
//    group = "Slay the Spire"
//    description = "Copies the JAR from your build/libs folder into your \$STS_INSTALL location"
//
//    dependsOn("buildJAR")
//
//    from("build/libs/$modName.jar")
//    into("$stsInstallLocation\\mods")
//}

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
        }
    }

    repositories {
        maven {
            name = "easel"
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}

// --------------------------------------------------------------------------------

// --------------------------------------------------------------------------------

// New source/javadoc jars for maven publishing

//tasks.register<Jar>("sourcesJar") {
//    dependsOn("classes")
//    archiveClassifier.set("sources")
//    from(sourceSets.main.get().allSource)
//}
//
//tasks.register<Jar>("javadocJar") {
//    dependsOn("javadoc")
//    archiveClassifier.set("javadoc")
//    from(tasks["javadoc"])
//}
//
//artifacts {
//    add("archives", tasks["sourcesJar"])
//    add("archives", tasks["javadocJar"])
//}

// --------------------------------------------------------------------------------

//publishing {
//    publications {
//        create<MavenPublication>("easel") {
//            from(components["java"])
//        }
//    }
//}

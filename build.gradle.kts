import java.net.URI

val githubVersion: String by project

plugins {
  `java-library`
  `maven-publish`
  id("org.jreleaser") version "1.14.0"
}

group = "xyz.jordanplayz158.mumble-voip.mumble"
version = githubVersion

java {
  withJavadocJar()
  withSourcesJar()
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = group.toString()
      artifactId = "server"

      from(components["java"])

      pom {
        name = "server"
        description = "Mumble Server ICE communication interface"
        url = "https://github.com/JordanPlayz158/mumble-slice"
        inceptionYear = "2024"
        licenses {
          license {
            name = "AGPL-3.0-or-later"
            url = "https://spdx.org/licenses/AGPL-3.0-or-later.html"
          }
        }
        // This list is for maintainers of the mumble-slice
        //   not the list of developers for the mumble project
        developers {
          developer {
            id = "jordanplayz158"
            name = "Jordan Adams"
          }
        }
        scm {
          connection = "scm:git:https://github.com/JordanPlayz158/mumble-slice.git"
          developerConnection = "scm:git:ssh://github.com/JordanPlayz158/mumble-slice.git"
          url = "https://github.com/JordanPlayz158/mumble-slice"
        }
      }
    }
  }

  repositories {
    maven {
      url = URI(layout.buildDirectory.dir("staging-deploy").get().toString())
    }
  }
}

jreleaser {
  signing {
    setActive("ALWAYS")
    armored = true
  }
  deploy {
    maven {
      nexus2 {
        create("maven-central") {
          setActive("ALWAYS")
          uri(URI("https://s01.oss.sonatype.org/service/local"))
          snapshotUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
          closeRepository = true
          releaseRepository = false
          stagingRepository("target/staging-deploy")
        }
      }
    }
  }
}

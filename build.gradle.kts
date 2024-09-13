plugins {
  `java-library`
  `maven-publish`
  id("org.jreleaser") version "1.14.0"
}

java {
  withJavadocJar()
  withSourcesJar()
}

publishing {
  publications {
    maven(MavenPublication) {
      groupId = "xyz.jordanplayz158.mumble-voip.mumble"
      artifactId = "server"

      from components.java

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
      url = layout.buildDirectory.dir("staging-deploy")
    }
  }
}

jreleaser {
  signing {
    active = 'ALWAYS'
    armored = true
  }
  deploy {
    maven {
      nexus2 {
        'maven-central' {
          active = 'ALWAYS'
          url = 'https://s01.oss.sonatype.org/service/local'
          snapshotUrl = 'https://s01.oss.sonatype.org/content/repositories/snapshots/'
          closeRepository = true
          releaseRepository = false
          stagingRepository('target/staging-deploy')
        }
      }
    }
  }
}

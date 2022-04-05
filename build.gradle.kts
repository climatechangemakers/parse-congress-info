plugins {
  alias(libs.plugins.kotlin.multiplatform)
}

group = "org.climatechangemakers"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

kotlin {
  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")
  val nativeTarget = when {
    hostOs == "Mac OS X" -> macosX64("native")
    hostOs == "Linux" -> linuxX64("native")
    isMingwX64 -> mingwX64("native")
    else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
  }

  nativeTarget.apply {
    binaries {
      executable {
        entryPoint = "org.climatechangemakers.parsecongress.main"
        this.binaryOptions
      }
    }
  }

  sourceSets {
    val nativeMain by getting {
      dependencies {
        implementation(libs.clikt)
        implementation(libs.okio)
      }
    }
    val nativeTest by getting
  }
}

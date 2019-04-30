
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    // Kotlin JVM plugin to target the JVM
    kotlin("jvm")                       version "1.3.31"

    // Code Coverage plugin
    jacoco

    // Kotlin linter
    id("org.jlleitschuh.gradle.ktlint") version "7.1.0"

    // dependencyUpdates - a task to determine which dependencies have updates
    id("com.github.ben-manes.versions") version "0.21.0"
}

allprojects {
    apply {
        plugin("maven")
    }
}

configure<JavaPluginConvention> {
    group = "com.stefanzh"
    version = "1.0.0"
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// Null safety for Kotlin projects through the Spring Initializr:
// https://github.com/spring-io/initializr/issues/591
tasks.withType<KotlinCompile<KotlinJvmOptions>> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    // Kotlin libraries
    implementation(platform(kotlin("bom", version = "1.3.31")))
    implementation(kotlin("stdlib-jdk8"))

    // JUnit 5
    testImplementation(enforcedPlatform("org.junit:junit-bom:5.3.2"))   // use JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api")           // JUnit 5 public API for writing tests and extensions
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")           // JUnit 5 engine to run tests
    testImplementation("org.junit.jupiter:junit-jupiter-params")        // JUnit 5 parameterized tests
}

// Test settings
val test by tasks.getting(Test::class) {
    useJUnitPlatform()  // enable Gradle to run JUnit 5 tests

    // log skipped and failed tests
    testLogging {
        events = setOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
    }
}

// Jacoco Plugin
jacoco {
    toolVersion = "0.8.3"
    reportsDir = file("$buildDir/reports/jacoco")
}

// Jacoco Coverage Report
val jacocoTestReport by tasks.getting(JacocoReport::class) {
    reports {
        html.isEnabled = true // human readable
        xml.isEnabled = true  // required by coveralls
    }
}

// Jacoco Enforce Code Coverage
val jacocoTestCoverageVerification by tasks.getting(JacocoCoverageVerification::class) {
    violationRules {
        // Every class should be tested
        rule {
            enabled = true
            element = "CLASS"

            limit {
                counter = "CLASS"
                value = "COVEREDRATIO"
                minimum = BigDecimal.valueOf(1.00)
            }
        }

        // Coverage on lines of code
        rule {
            enabled = true
            element = "CLASS"

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal.valueOf(1.00)
            }
        }
    }
}

// the success of the build depends on Jacoco
test.finalizedBy(jacocoTestCoverageVerification, jacocoTestReport)

// always run tests before code coverage is collected
jacocoTestReport.dependsOn(test)
jacocoTestCoverageVerification.dependsOn(test)

// execute linters on check
val check: DefaultTask by tasks
val ktlintCheck: DefaultTask by tasks
check.dependsOn(ktlintCheck)

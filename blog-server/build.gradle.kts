import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    java
    application
    id("org.springframework.boot") version "2.4.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.spring") version "1.6.20"
    kotlin("kapt") version "1.6.20"
    id("io.freefair.lombok") version "5.3.0"
    id("org.jetbrains.kotlin.plugin.lombok") version "1.6.20"
    `maven-publish`
}

group = "com.minzheng"
version = "0.0.1"
description = "blog"
java.sourceCompatibility = JavaVersion.VERSION_1_8

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}



configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

kapt {
    keepJavacAnnotationProcessors = true
}
kotlinLombok {
    lombokConfigurationFile(file("lombok.config"))
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    implementation("com.github.xiaoymin:knife4j-spring-boot-starter:2.0.7")
    implementation("com.aliyun.oss:aliyun-sdk-oss:3.8.0")
    implementation("com.alibaba:fastjson:1.2.76")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.5.1")

    implementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")
    implementation("cn.hutool:hutool-all:5.7.5")
    implementation("org.apache.commons:commons-lang3:3.8.1")

    implementation("eu.bitwalker:UserAgentUtils:1.20")
    runtimeOnly("mysql:mysql-connector-java:8.0.22")


    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
    implementation("io.ktor:ktor-client-core:2.1.0")
    implementation("io.ktor:ktor-client-cio:2.1.0")
}



publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java", "src/main/kotlin")
        }
    }
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootBuildImage> {
    builder = "paketobuildpacks/builder:tiny"
    environment = mapOf("BP_NATIVE_IMAGE" to "true")
}



repositories {
    mavenLocal()
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/public")
    }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/central")
    }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/google")
    }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/gradle-plugin")
    }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/spring")
    }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/spring-plugin")
    }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/apache-snapshots")
    }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/grails-core")
    }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/nexus/content/repositories/jcenter")
    }
}


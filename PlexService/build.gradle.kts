plugins {
    id("com.android.library")
}

android {
    namespace = "com.pragmaticaudio.plexservice"
    compileSdk = 33

    defaultConfig {

        minSdk = 27
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildToolsVersion = "33.0.1"
    ndkVersion = "21.1.6352462"
}


dependencies {
    implementation("javax.xml.stream:stax-api:1.0-2")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation(project(mapOf("path" to ":restserver")))
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.yaml:snakeyaml:1.28")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
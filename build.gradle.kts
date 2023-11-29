buildscript {
//    repositories {
//        google()
//    }
    dependencies {

        classpath("com.google.gms:google-services:4.4.0")
        //밑에는 언선이가 추가한거
        classpath("com.android.tools.build:gradle:7.1.1")
    }
}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false

    //언선이가 추가한 거
    id ("org.jetbrains.kotlin.multiplatform") version "1.6.0" apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
}
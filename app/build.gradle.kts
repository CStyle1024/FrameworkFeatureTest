import groovy.namespace.QName
import groovy.util.Node
import groovy.xml.XmlParser
import groovy.xml.XmlUtil
import java.io.FileOutputStream

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.frameworkfeaturetest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.frameworkfeaturetest"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("arm64-v8a"))
        }
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }

    signingConfigs {
        create("platform-e05") {
            storeFile = file("./signature/platform-e05.jks")
            storePassword = "123456"
            keyAlias = "mzplatform"
            keyPassword = "123456"
        }
    }

    signingConfigs {
        create("platform-phone") {
            storeFile = file("./signature/platform.jks")
            storePassword = "123456"
            keyAlias = "mzplatform"
            keyPassword = "123456"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
//            signingConfig = signingConfigs.getByName("debug")
            signingConfig = signingConfigs.getByName("platform-e05")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

//project.tasks.preBuild.get().doLast {
//    fun changeSdkOrder(path: String) {
//        runCatching {
//            val imlFile = File(path)
//            val xmlParser = XmlParser().parse(imlFile)
//            println("> Task :${project.name}:preBuild:doLast:changedSdkOrder xmlParser=" + xmlParser)
//            with(XmlParser().parse(imlFile)) {
//                println("> Task :${project.name}:preBuild:doLast:changedSdkOrder jdkEntry")
//                val rootManagerComponent = getAt(QName.valueOf("component"))
//                    .map { it as Node }
//                    .first { it.attribute("name") == "NewModuleRootManager" }
//                val jdkEntry = rootManagerComponent.getAt(QName.valueOf("orderEntry"))
//                    .map { it as Node }
//                    .first { it.attribute("type") == "jdk" }
//                val jdkName = jdkEntry.attribute("jdkName")
//                val jdkType = jdkEntry.attribute("jdkType")
//                println("> Task :${project.name}:preBuild:doLast:changedSdkOrder jdkEntry = $jdkEntry")
//                rootManagerComponent.remove(jdkEntry)
//                rootManagerComponent.appendNode(
//                    "orderEntry", mapOf(
//                        "type" to "jdk",
//                        "jdkName" to jdkName,
//                        "jdkType" to jdkType
//                    )
//                )
//                XmlUtil.serialize(this, FileOutputStream(imlFile))
//            }
//        }
//    }
//
//    println("> Task :${project.name}:preBuild:doLast:changedSdkOrder:" + rootDir.absolutePath + "/.idea/modules/app/FrameworkFeatureTest.iml")
//    changeSdkOrder(rootDir.absolutePath + "/.idea/modules/app/FrameworkFeatureTest.iml")
//}

dependencies {

    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    compileOnly(files("libs/framework.jar"))
}
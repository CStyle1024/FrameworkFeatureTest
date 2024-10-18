// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
}

allprojects {
    beforeEvaluate {
        val path = rootDir.absolutePath + "/app/libs/framework.jar"
        tasks.withType<JavaCompile> {
            options.compilerArgs.add("-Xbootclasspath/p:$path")
//            val newFileList = mutableListOf<File>()
//            newFileList.add(File(path))
//            options.bootstrapClasspath?.files?.let { oldFileList ->
//                newFileList.addAll(oldFileList)
//            }
//            options.bootstrapClasspath = files(*newFileList.toTypedArray())
        }
    }
}
package com.github.tkameyama.intellijdbtokotlinclass.services

import com.intellij.openapi.project.Project
import com.github.tkameyama.intellijdbtokotlinclass.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}

package com.github.t_kameyama.dbtokotlin

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class SettingsConfigurable : Configurable {
    private var component: SettingsComponent? = null

    override fun getDisplayName() = "Database To Kotlin Class"

    override fun getPreferredFocusedComponent()= component!!.preferredFocusedComponent

    override fun createComponent(): JComponent {
        val component = SettingsComponent()
        this.component = component
        return component.panel
    }

    override fun isModified(): Boolean {
        val component = this.component!!
        val state = SettingsState.getInstance()
        return (component.classNamePrefix != state.classNamePrefix) || (component.classNamePostfix != state.classNamePostfix)
    }

    override fun apply() {
        val component = this.component!!
        val state = SettingsState.getInstance()
        state.classNamePrefix = component.classNamePrefix
        state.classNamePostfix = component.classNamePostfix
    }

    override fun reset() {
        val component = this.component!!
        val state = SettingsState.getInstance()
        component.classNamePrefix = state.classNamePrefix
        component.classNamePostfix = state.classNamePostfix
    }

    override fun disposeUIResources() {
        component = null
    }
}

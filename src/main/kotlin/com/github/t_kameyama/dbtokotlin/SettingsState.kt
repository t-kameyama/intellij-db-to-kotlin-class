package com.github.t_kameyama.dbtokotlin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "DatabaseToKotlinClassState",
    storages = [Storage("DatabaseToKotlinClassState.xml")]
)
class SettingsState: PersistentStateComponent<SettingsState> {
    @Suppress("unused")
    var classNamePrefix: String = ""

    @Suppress("unused")
    var classNamePostfix: String = ""

    @Suppress("unused")
    var classAnnotations: String = ""

    override fun getState(): SettingsState {
        return this
    }

    override fun loadState(p0: SettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): SettingsState {
            return ApplicationManager.getApplication().getService(SettingsState::class.java)
        }
    }
}

package com.github.t_kameyama.dbtokotlin

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

class SettingsComponent {
    val panel: JPanel
    private val classNamePrefixField = JBTextField()
    private val classNamePostfixField = JBTextField()
    private val classAnnotationsField = JBTextField()
    val preferredFocusedComponent = classNamePrefixField

    init {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Class name prefix: "), classNamePrefixField, 1, false)
            .addLabeledComponent(JBLabel("Class name postfix: "), classNamePostfixField, 1, false)
            .addLabeledComponent(JBLabel("Class annotations (Comma-Separated): "), classAnnotationsField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    var classNamePrefix: String
        get() = classNamePrefixField.text
        set(newText) {
            classNamePrefixField.text = newText
        }

    var classNamePostfix: String
        get() = classNamePostfixField.text
        set(newText) {
            classNamePostfixField.text = newText
        }

    var classAnnotations: String
        get() = classAnnotationsField.text
        set(newText) {
            classAnnotationsField.text = newText
        }
}

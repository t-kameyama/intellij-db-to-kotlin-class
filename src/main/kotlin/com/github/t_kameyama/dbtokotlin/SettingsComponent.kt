package com.github.t_kameyama.dbtokotlin

import com.intellij.openapi.ui.Messages
import com.intellij.ui.TableUtil
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.ListTableModel
import javax.swing.JPanel

class SettingsComponent {
    val panel: JPanel
    private val classNamePrefixField = JBTextField()
    private val classNamePostfixField = JBTextField()

    private val classAnnotationTableModel = ClassAnnotationTableModel()
    private val classAnnotationTable = JBTable(classAnnotationTableModel)
    private val classAnnotationTableDecorator = ToolbarDecorator.createDecorator(classAnnotationTable)
        .setAddAction {
            val newValue = Messages.showInputDialog(null, "Add Class Annotation", null, "", null)
            if (newValue.isNullOrBlank()) return@setAddAction
            classAnnotationTableModel.addRow(newValue)
        }
        .setEditAction {
            val rowIndex = classAnnotationTable.selectedRow
            val items = classAnnotationTableModel.items
            val currentValue = items.getOrNull(rowIndex) ?: return@setEditAction
            val newValue = Messages.showInputDialog(null, "Edit Class Annotation", null, currentValue, null)
            if (newValue.isNullOrBlank()) return@setEditAction
            classAnnotationTableModel.setItem(rowIndex, newValue)
        }
        .setRemoveAction {
            TableUtil.removeSelectedItems(classAnnotationTable)
        }
        .createPanel()

    val preferredFocusedComponent = classNamePrefixField

    init {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Class name prefix: "), classNamePrefixField, 1, false)
            .addLabeledComponent(JBLabel("Class name postfix: "), classNamePostfixField, 1, false)
            .addComponent(classAnnotationTableDecorator, 10)
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

    var classAnnotationList: List<String>
        get() = classAnnotationTableModel.items
        set(newList) {
            (classAnnotationTableModel.items.size - 1).downTo(0).forEach { classAnnotationTableModel.removeRow(it) }
            newList.forEach { classAnnotationTableModel.addRow(it) }
        }
}

class ClassAnnotationTableModel : ListTableModel<String>() {
    init {
        columnInfos = arrayOf(
            object : ColumnInfo<String, String>("Class Annotations") {
                override fun valueOf(item: String?) = item
            }
        )
    }
}
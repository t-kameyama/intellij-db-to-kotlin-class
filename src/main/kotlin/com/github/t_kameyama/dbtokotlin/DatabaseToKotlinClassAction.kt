package com.github.t_kameyama.dbtokotlin

import com.intellij.database.dialects.DatabaseDialect
import com.intellij.database.model.DasColumn
import com.intellij.database.psi.DbTable
import com.intellij.database.util.DasUtil
import com.intellij.database.util.DbSqlUtil
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import org.javalite.common.Inflector
import java.awt.datatransfer.StringSelection
import java.math.BigDecimal
import java.sql.JDBCType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@ExperimentalStdlibApi
class DatabaseToKotlinClassAction : AnAction() {
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val element = e.getData(LangDataKeys.PSI_ELEMENT)
        e.presentation.isEnabledAndVisible = element is DbTable
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val tables = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY)
            .orEmpty()
            .filterIsInstance<DbTable>()
            .sortedBy { it.name }
            .ifEmpty { return }

        val dialect = DbSqlUtil.getSqlDialect(tables.first()).databaseDialect
        val state = SettingsState.getInstance()
        val dataClasses = tables.joinToString(separator = "\n\n") {
            it.createDataClass(
                dialect = dialect,
                classNamePrefix = state.classNamePrefix,
                classNamePostfix = state.classNamePostfix,
                classAnnotationsRowText = state.classAnnotations
            )
        }

        CopyPasteManager.getInstance().setContents(StringSelection(dataClasses))
    }

    private fun DbTable.createDataClass(
        dialect: DatabaseDialect,
        classNamePrefix: String,
        classNamePostfix: String,
        classAnnotationsRowText: String,
    ): String {
        val className = "$classNamePrefix${name.camelize(true)}$classNamePostfix"

        val indent = " ".repeat(4)
        val properties = DasUtil.getColumns(this).joinToString(
            prefix = "\n",
            postfix = "\n",
            separator = ",\n",
            transform = { "$indent${it.createProperty(dialect)}" }
        )

        val classAnnotations = if (classAnnotationsRowText.isNotBlank()) {
            classAnnotationsRowText
                .split(",")
                .map { it.trim() }
                .joinToString(separator = "\n", postfix = "\n") { if (it.startsWith("@")) it else "@$it" }
        } else {
            ""
        }

        return "${classAnnotations}data class $className($properties)"
    }

    private fun DasColumn.createProperty(dialect: DatabaseDialect): String {
        val jdbcType = dialect.getJavaTypeForNativeType(dasType.toDataType().typeName)
        val type = when (JDBCType.valueOf(jdbcType)) {
            JDBCType.CHAR,
            JDBCType.VARCHAR,
            JDBCType.LONGVARCHAR,
            JDBCType.NCHAR,
            JDBCType.NVARCHAR,
            JDBCType.LONGNVARCHAR,
            JDBCType.CLOB,
            JDBCType.NCLOB -> String::class
            JDBCType.TINYINT,
            JDBCType.SMALLINT,
            JDBCType.INTEGER -> Int::class
            JDBCType.BIGINT -> Long::class
            JDBCType.REAL,
            JDBCType.FLOAT,
            JDBCType.DOUBLE,
            JDBCType.NUMERIC,
            JDBCType.DECIMAL -> BigDecimal::class
            JDBCType.BIT,
            JDBCType.BOOLEAN -> Boolean::class
            JDBCType.DATE -> LocalDate::class
            JDBCType.TIME -> LocalTime::class
            JDBCType.TIMESTAMP -> LocalDateTime::class
            JDBCType.BINARY,
            JDBCType.VARBINARY,
            JDBCType.LONGVARBINARY,
            JDBCType.BLOB -> ByteArray::class
            else -> Any::class
        }.simpleName.let { if (this.isNotNull) it else "$it?" }

        val propertyName = name.camelize(false)
        return "val $propertyName: $type"
    }

    private fun String.camelize(isClassName: Boolean): String {
        ifEmpty { return this }

        val tokens = if ("_" in this) {
            split("_")
        } else {
            toCharArray()
                .joinToString(separator = "") { if (it.isUpperCase()) "_$it" else "$it" }
                .removePrefix("_")
                .split("_")
        }
        val lastIndex = tokens.size - 1
        val camel = buildString {
            tokens.forEachIndexed { index, token ->
                if (isClassName && index == lastIndex) {
                    append(token.singularize().capitalize())
                } else {
                    append(token.capitalize())
                }
            }
        }

        return if (isClassName) {
            camel
        } else {
            camel.substring(0, 1).lowercase() + camel.substring(1)
        }
    }

    private fun String.capitalize(): String {
        ifEmpty { return this }
        return substring(0, 1).uppercase() + substring(1).lowercase()
    }

    private fun String.singularize(): String {
        val lowercase = lowercase()
        val singular = Inflector.singularize(lowercase)
        return if (lowercase == Inflector.pluralize(singular)) singular else lowercase
    }
}

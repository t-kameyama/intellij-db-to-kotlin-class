package com.github.t_kameyama.dbtokotlin

import com.intellij.database.dialects.DatabaseDialect
import com.intellij.database.model.DasColumn
import com.intellij.database.psi.DbTable
import com.intellij.database.util.DasUtil
import com.intellij.database.util.DbSqlUtil
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
    override fun update(e: AnActionEvent) {
        val element = e.getData(LangDataKeys.PSI_ELEMENT)
        e.presentation.isEnabledAndVisible = element is DbTable
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val tables = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY).orEmpty()
            .filterIsInstance<DbTable>().sortedBy { it.name }
        if (tables.isEmpty()) return
        val dialect = DbSqlUtil.getSqlDialect(tables.first()).databaseDialect
        val state = SettingsState.getInstance()
        val dataClasses = tables.joinToString(separator = "\n\n") {
            it.createDataClass(dialect, state.classNamePrefix, state.classNamePostfix)
        }
        write(dataClasses)
    }

    private fun write(dataClasses: String) {
        CopyPasteManager.getInstance().setContents(StringSelection(dataClasses))
    }

    private fun DbTable.createDataClass(
        dialect: DatabaseDialect,
        classNamePrefix: String,
        classNamePostfix: String
    ): String {
        val split = name.lowercase().split("_")
        val last = split.last()
        val className = split
            .joinToString("_") { if (it == last) Inflector.singularize(it) else it }
            .let { Inflector.camelize(it, true) }
            .let { "$classNamePrefix$it$classNamePostfix" }
        val indent = "    "
        val properties = DasUtil.getColumns(this)
            .joinToString(prefix = "\n", postfix = "\n", separator = ",\n") { "$indent${it.createProperty(dialect)}" }
        return "data class $className($properties)"
    }

    private fun DasColumn.createProperty(dialect: DatabaseDialect): String {
        val jdbcType = dialect.getJavaTypeForNativeType(dataType.typeName)
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

        val propertyName = Inflector.camelize(name.lowercase(), false)
        return "val $propertyName: $type"
    }
}

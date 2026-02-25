package org.jragent.agent.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DataBaseTool implements Tool{

    private final JdbcTemplate jdbcTemplate;

    public DataBaseTool(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public String getName() {
        return "DataBaseTool";
    }

    @Override
    public String getDescription() {
        return "一个用于执行数据库查询操作的工具，从PostgreSQL中读取数据。";
    }

    @Override
    public ToolType getType() {
        return ToolType.OPTIONAL;
    }

    @org.springframework.ai.tool.annotation.Tool(name = "databaseQuery", description = "用于在PostgreSQL中执行只读查询（SELECT）。接收由模型生成的查询语句，并返回结构化数据结果。该工具仅用于检索数据，严禁任何写入或修改数据库的语句。")
    public String databaseQuery(String sql) {
        // 防注入
        try {
            String trimmedSql = sql.trim().toUpperCase();
            if (!trimmedSql.startsWith("SELECT")) {
                log.warn("拒绝执行非 SELECT 查询: {}", sql);
                return "错误：仅支持 SELECT 查询语句。提供的 SQL: " + sql;
            }

            // 执行查询，并将结果映射为文字表格
            List<String> rows = jdbcTemplate.query(sql, (ResultSet rs) -> {
                List<String> resultRows = new ArrayList<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                if (columnCount == 0) {
                    resultRows.add("查询结果为空（无列）");
                    return resultRows;
                }

                // 获取列名与默认列宽
                List<String> columnNames = new ArrayList<>();
                List<Integer> columnWidths = new ArrayList<>();
                // sql列编号从1开始
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    int defaultColumnLength = columnName.length();
                    columnNames.add(columnName);
                    columnWidths.add(defaultColumnLength);
                }

                // 先完整读取所有数据行，并在读取过程中动态更新每列宽度
                List<List<String>> rowsData = new ArrayList<>();
                // 遍历每行
                while (rs.next()) {
                    List<String> rowData = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        // 遍历每列
                        Object value = rs.getObject(i);
                        // 值非空则加入，动态更新列宽
                        String valueStr = value == null ? "null" : value.toString();
                        rowData.add(valueStr);
                        int currentWidth = columnWidths.get(i - 1);
                        if (valueStr.length() > currentWidth) {
                            columnWidths.set(i - 1, valueStr.length());
                        }
                    }
                    rowsData.add(rowData);
                }

                // 构建表头
                StringBuilder header = new StringBuilder();
                header.append("| ");
                for (int i = 0; i < columnCount; i++) {
                    String columnName = columnNames.get(i);
                    int width = columnWidths.get(i);
                    // 左对齐
                    header.append(String.format("%-" + width + "s", columnName)).append(" | ");
                }
                resultRows.add(header.toString());

                // 构建分隔线
                StringBuilder separator = new StringBuilder();
                separator.append("| ");
                for (int i = 0; i < columnCount; i++) {
                    int width = columnWidths.get(i);
                    separator.append("-".repeat(width + 2)).append("|");
                }
                resultRows.add(separator.toString());

                // 构建数据行
                if (rowsData.isEmpty()) {
                    StringBuilder emptyRow = new StringBuilder();
                    emptyRow.append("| ");
                    int totalWidth = columnWidths.stream().mapToInt(w -> w + 3).sum() - 1;
                    emptyRow.append(String.format("%-" + (totalWidth - 2) + "s", "(无数据)"));
                    emptyRow.append(" |");
                    resultRows.add(emptyRow.toString());
                } else {
                    for (List<String> rowData : rowsData) {
                        StringBuilder row = new StringBuilder();
                        row.append("| ");
                        for (int i = 0; i < columnCount; i++) {
                            String value = rowData.get(i);
                            int width = columnWidths.get(i);
                            row.append(String.format("%-" + width + "s", value)).append(" | ");
                        }
                        resultRows.add(row.toString());
                    }
                }

                return resultRows;
            });

            // 计算并记录返回去掉表头和分割线后的行数
            int dataRowCount = rows.size() - 2;
            if (rows.size() > 2 && rows.get(rows.size() - 1).contains("(无数据)")) {
                dataRowCount = 0;
            }

            log.info("成功执行 SQL 查询，返回 {} 行数据", dataRowCount);
            return "查询结果:\n" + String.join("\n", rows);
        } catch (Exception e) {
            log.error("未知错误: {}", e.getMessage(), e);
            return "错误：操作失败 - " + e.getMessage() + "\nSQL: " + sql;
        }
    }
}

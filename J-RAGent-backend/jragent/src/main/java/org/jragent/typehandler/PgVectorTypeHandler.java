package org.jragent.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(float[].class)
public class PgVectorTypeHandler extends BaseTypeHandler<float[]> {

    /*入库：float[] -> StringBuilder -> String -> Types.OTHER -> Postgres vector*/
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, float[] parameter, JdbcType jdbcType) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int j = 0; j < parameter.length; j++) {
            sb.append(parameter[j]);
            if (j < parameter.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        ps.setObject(i, sb.toString(), Types.OTHER);
    }

    /*按列名获取结果*/
    @Override
    public float[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    /*按列索引获取结果*/
    @Override
    public float[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    /*调用存储过程或数据库函数*/
    @Override
    public float[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    /*出库：Postgres vector -> String -> float[]*/
    private float[] parse(String vectorText) {
        if (vectorText == null) return null;
        vectorText = vectorText.replace("[", "").replace("]", "");
        if (vectorText.isBlank()) return new float[0];
        String[] parts = vectorText.split(",");
        float[] arr = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Float.parseFloat(parts[i]);
        }
        return arr;
    }
}

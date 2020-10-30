package cn.sunwei1991.rabbit.common.mybatis.handler;

import cn.sunwei1991.rabbit.api.Message;
import cn.sunwei1991.rabbit.common.util.FastJsonConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Title MessageJsonTypeHandler
 * @Description 在Mybatis插入数据前进行Message对象JSON转换
 * @Author SunWei
 * @Create 2020/10/21 3:42 下午
 */
public class MessageJsonTypeHandler extends BaseTypeHandler<Message> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i,
                                    Message message, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, FastJsonConvertUtil.convertObjectToJSON(message));
    }

    @Override
    public Message getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String value = resultSet.getString(s);
        if(null != value && !StringUtils.isBlank(value)) {
            return FastJsonConvertUtil.convertJSONToObject(resultSet.getString(s), Message.class);
        }
        return null;
    }

    @Override
    public Message getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);
        if(null != value && !StringUtils.isBlank(value)) {
            return FastJsonConvertUtil.convertJSONToObject(resultSet.getString(i), Message.class);
        }
        return null;
    }

    @Override
    public Message getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);
        if(null != value && !StringUtils.isBlank(value)) {
            return FastJsonConvertUtil.convertJSONToObject(callableStatement.getString(i), Message.class);
        }
        return null;
    }
}

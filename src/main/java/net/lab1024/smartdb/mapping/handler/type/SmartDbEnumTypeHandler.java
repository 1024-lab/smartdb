/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.lab1024.smartdb.mapping.handler.type;

import net.lab1024.smartdb.SmartDbEnum;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SmartDbEnumTypeHandler<T extends SmartDbEnum> implements TypeHandler<T> {

    private final Class<T> type;

    public SmartDbEnumTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }


    @Override
    public T getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        int result = rs.getInt(columnIndex);
        if (result == 0 && rs.wasNull()) {
            return null;
        } else {
            T[] enums = type.getEnumConstants();
            for (T e : enums) {
                if (e.getValue() != null && e.getValue().equals(result)) {
                    return e;
                }
            }
            return null;
        }
    }

    @Override
    public T getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        int result = rs.getInt(columnName);
        if (result == 0 && rs.wasNull()) {
            return null;
        } else {
            T[] enums = type.getEnumConstants();
            for (T e : enums) {
                if (e.getValue() != null && e.getValue().equals(result)) {
                    return e;
                }
            }
            return null;
        }
    }
}
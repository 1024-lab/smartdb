/**
 * Copyright 2009-2019 the original author or authors.
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

public class YearTypeHandler implements TypeHandler<Year> {

    @Override
    public Year getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        int year = rs.getInt(columnIndex);
        return year == 0 && rs.wasNull() ? null : Year.of(year);
    }

    @Override
    public Year getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        int year = rs.getInt(columnName);
        return year == 0 && rs.wasNull() ? null : Year.of(year);
    }
}

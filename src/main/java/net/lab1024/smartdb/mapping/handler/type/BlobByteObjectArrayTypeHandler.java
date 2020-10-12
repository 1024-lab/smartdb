/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.lab1024.smartdb.mapping.handler.type;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobByteObjectArrayTypeHandler implements TypeHandler<Byte[]> {

  private Byte[] getBytes(Blob blob) throws SQLException {
    Byte[] returnValue = null;
    if (blob != null) {
      returnValue = ByteArrayUtils.convertToObjectArray(blob.getBytes(1, (int) blob.length()));
    }
    return returnValue;
  }

  @Override
  public Byte[] getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
    Blob blob = rs.getBlob(columnIndex);
    return getBytes(blob);
  }

  @Override
  public Byte[] getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
    Blob blob = rs.getBlob(columnName);
    return getBytes(blob);
  }
}

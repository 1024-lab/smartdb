package net.lab1024.smartdb.sqlbuilder.impl.mysql;


import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.AbstractSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractInsertSqlBuilder;

import java.util.Map;

public class MysqlInsertSqlBuilder extends AbstractInsertSqlBuilder {

    public MysqlInsertSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.MYSQL;
    }

    @Override
    public String generateSql(boolean isPretty) {
        StringBuilder insertClause = new StringBuilder();
        insertClause.append("insert into ");
        insertClause.append(this.table);
        insertClause.append(" ( ");

        StringBuilder valuesClause = new StringBuilder();
        valuesClause.append(" values (");

        if (columnAndParam.size() > 0) {
            int count = 1;
            boolean isNotLast = false;
            for (Map.Entry<String, InsertParamObject> entry : columnAndParam.entrySet()) {
                //insert clause
                insertClause.append(MysqlConst.RESERVED_WORD_CHAR).append(entry.getKey()).append(MysqlConst.RESERVED_WORD_CHAR);

                //value clause
                InsertParamObject paramObject = entry.getValue();
                if (paramObject.isSqlFunction()) {
                    valuesClause.append(AbstractSqlBuilder.BLANK).append(paramObject.getParam());
                } else {
                    valuesClause.append(AbstractSqlBuilder.BLANK).append(AbstractSqlBuilder.PARAM_PLACEHOLDER);
                }

                isNotLast = count < columnAndParam.size();
                if (isNotLast) {
                    insertClause.append(",");
                    valuesClause.append(",");
                }
                count++;
            }
        }

        insertClause.append(" )");
        valuesClause.append(" )");
        insertClause.append(isPretty ? AbstractSqlBuilder.LINE_SEPARATOR : AbstractSqlBuilder.BLANK);
        insertClause.append(valuesClause);
        insertClause.append(appendSqlBuilder);
        return insertClause.toString();

    }
}

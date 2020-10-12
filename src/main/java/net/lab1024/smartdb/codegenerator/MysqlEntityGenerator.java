package net.lab1024.smartdb.codegenerator;

import net.lab1024.smartdb.annotation.PrimaryKey;
import net.lab1024.smartdb.annotation.TableAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * isNullable(int column)：指示指定列中的值是否可以为 null。
 */
public class MysqlEntityGenerator extends SmartDbEntityGenerator {

    protected static final Logger LOG = LoggerFactory.getLogger(MysqlEntityGenerator.class);

    public MysqlEntityGenerator(SmartDbEntityGeneratorBuilder smartDbEntityGeneratorBuilder) {
        super(smartDbEntityGeneratorBuilder);
    }

    @Override
    public void generate() {
        // 创建连接
        Connection con = null;
        // 查要生成实体类的表

        PreparedStatement pStemt = null;
        ResultSet rs = null;
        try {
            con = smartDbEntityGeneratorBuilder.getConnection();
            //获取主键
            ResultSet primaryColumnKeys = con.getMetaData().getPrimaryKeys(null, null, smartDbEntityGeneratorBuilder.getTableName());
            while (primaryColumnKeys.next()) {
                primaryKeyColumnNames.add(primaryColumnKeys.getString(4));
            }
            //获取表注释
            rs = con.getMetaData().getColumns(null, "%", smartDbEntityGeneratorBuilder.getTableName(), "%");
            while (rs.next()) {
                colRemarkList.add(rs.getString("REMARKS"));
            }
            //获取列信息
            LOG.debug("============开始 解析列============");
            pStemt = con.prepareStatement(String.format("select * from %s ", smartDbEntityGeneratorBuilder.getTableName()));
            ResultSetMetaData rsmd = pStemt.getMetaData();
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                String columnName = rsmd.getColumnName(i + 1);
                String fieldName = smartDbEntityGeneratorBuilder.getTableColumnCaseFormat().to(smartDbEntityGeneratorBuilder.getEntityFieldCaseFormat(), columnName);
                //命名格式转换
                colNamesList.add(fieldName);
                LOG.debug(String.format("列明转换: %s ==> %s ", columnName, fieldName));

                String colType = rsmd.getColumnTypeName(i + 1);
                colTypesList.add(colType);


                if (colType.equalsIgnoreCase("datetime") || colType.equalsIgnoreCase("date") || colType.equalsIgnoreCase("time")) {
                    importUtilDate = true;
                }
                int columnDisplaySize = rsmd.getColumnDisplaySize(i + 1);
                colSizesList.add(columnDisplaySize);
            }
            LOG.debug("============结束 解析列============");
            //生产类内容
            String classContent = generateClassContent();
            write2File(classContent);

        } catch (Exception e) {
            LOG.error("", e);
        } finally {
            smartDbEntityGeneratorBuilder.releaseResources(null, pStemt, con);
        }
    }

    protected void parseTableInfo(Connection conn, PreparedStatement pStemt) {

    }

    protected void genEntityClassRemark(StringBuilder sb) {
        sb.append("package " + smartDbEntityGeneratorBuilder.getPackageName() + ";\r\n\r\n");
        // 判断是否导入工具包
        if (importUtilDate) {
            sb.append("import java.util.Date;\r\n");
        }

        if (!primaryKeyColumnNames.isEmpty()) {
            sb.append("import " + PrimaryKey.class.getName() + ";\r\n");
        }

        sb.append("import " + TableAlias.class.getName() + ";\r\n");

        // 注释部分
        sb.append("\r\n/**\r\n");
        sb.append("* " + smartDbEntityGeneratorBuilder.getTableName() + "  实体类\r\n");
        sb.append("* 表备注：" + smartDbEntityGeneratorBuilder.getTableName() + "  实体类\r\n");
        sb.append("*/ \r\n");
    }

    protected void write2File(String content) {
        LOG.debug("============开始写入文件============");
        String dir = null;
        if (smartDbEntityGeneratorBuilder.getEntityClassFileDir() != null && smartDbEntityGeneratorBuilder.getEntityClassFileDir().length() > 0) {
            dir = smartDbEntityGeneratorBuilder.getEntityClassFileDir();
        } else {
            dir = FileSystemView.getFileSystemView().getHomeDirectory().getAbsoluteFile().getAbsolutePath();
        }

        File classFile = new File(dir + File.separator + smartDbEntityGeneratorBuilder.getClassName() + ".java");
        try {
            if (classFile.exists()) {
                classFile.delete();
            }
            classFile.createNewFile();
            FileWriter fw = new FileWriter(classFile);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(content);
            pw.flush();
            pw.close();
            LOG.debug("============结束写入文件============");
        } catch (IOException e) {
            LOG.error("", e);
        }
        LOG.info(String.format("表:%s, 类：%s, 路径:%s", smartDbEntityGeneratorBuilder.getTableName(), smartDbEntityGeneratorBuilder.getClassName(), classFile.getAbsolutePath()));
    }

    protected String generateClassContent() {
        LOG.debug("============开始生成 类============");
        StringBuilder sb = new StringBuilder();
        //生成  类注释
        genEntityClassRemark(sb);
        //生成  类名
        genEntityClassName(sb);
        //生成  字段
        genEntityClassFields(sb);

        //生成  getter/setter方法
        if (super.smartDbEntityGeneratorBuilder.isGenerateGetterSetter()) {
            genGetterSetterMethod(sb);
        }

        //类结束花括号
        sb.append("}\r\n");
        LOG.debug("============结束生成 类============");
        return sb.toString();
    }

    protected void genEntityClassName(StringBuilder sb) {
        // 实体部分
        sb.append("\r\n");
        sb.append("@" + TableAlias.class.getSimpleName() + "(\"" + smartDbEntityGeneratorBuilder.getTableName() + "\")" + "\r\n");
        sb.append("public class " + smartDbEntityGeneratorBuilder.getClassName() + "{\r\n");
    }

    protected void genEntityClassFields(StringBuilder sb) {
        for (int i = 0; i < colNamesList.size(); i++) {
            sb.append("\t/** " + colRemarkList.get(i) + " (最大长度:" + String.valueOf(colSizesList.get(i)) + ",类型:" + colTypesList.get(i) + ")  */\r\n");
            if (primaryKeyColumnNames.contains(colNamesList.get(i))) {
                sb.append("\t@" + PrimaryKey.class.getSimpleName() + "\r\n");
            }
            if (smartDbEntityGeneratorBuilder.isFieldBasicType()) {
                sb.append("\tprivate " + sqlType2JavaTypeForBasic(colTypesList.get(i)) + " " + colNamesList.get(i) + ";\r\n");
            } else {
                sb.append("\tprivate " + sqlType2JavaTypeForRefer(colTypesList.get(i)) + " " + colNamesList.get(i) + ";\r\n");
            }
        }
    }

    protected void genGetterSetterMethod(StringBuilder sb) {
        for (int i = 0; i < colNamesList.size(); i++) {
            //字段注释使用列的的注释
            sb.append("\t/** " + colRemarkList.get(i) + " (最大长度:" + colSizesList.add(i) + ",类型:" + colTypesList.get(i) + ")  */\r\n");
            if (smartDbEntityGeneratorBuilder.isFieldBasicType()) {
                sb.append("\tpublic void set" + initcap(colNamesList.get(i)) + "(" + sqlType2JavaTypeForBasic(colTypesList.get(i)) + " " + colNamesList.get(i) + "){\r\n");
            } else {
                sb.append("\tpublic void set" + initcap(colNamesList.get(i)) + "(" + sqlType2JavaTypeForRefer(colTypesList.get(i)) + " " + colNamesList.get(i) + "){\r\n");
            }

            sb.append("\tthis." + colNamesList.get(i) + "=" + colNamesList.get(i) + ";\r\n");
            sb.append("\t}\r\n");
            sb.append("\t/** " + colRemarkList.get(i) + " (最大长度:" + colSizesList.add(i) + ",类型:" + colTypesList.get(i) + ")  */\r\n");
            if (smartDbEntityGeneratorBuilder.isFieldBasicType()) {
                sb.append("\tpublic " + sqlType2JavaTypeForBasic(colTypesList.get(i)) + " get" + initcap(colNamesList.get(i)) + "(){\r\n");
            } else {
                sb.append("\tpublic " + sqlType2JavaTypeForRefer(colTypesList.get(i)) + " get" + initcap(colNamesList.get(i)) + "(){\r\n");
            }
            sb.append("\t\treturn " + colNamesList.get(i) + ";\r\n");
            sb.append("\t}\r\n");
        }
    }

    /**
     * java 引用类型
     */
    protected String sqlType2JavaTypeForRefer(String sqlType) {
        if (sqlType.equalsIgnoreCase("bit")) {
            return "Boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "Byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "Short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "Float";
        } else if (sqlType.equalsIgnoreCase("numeric") || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money") || sqlType.equalsIgnoreCase("smallmoney")) {
            return "Double";
        } else if (sqlType.equalsIgnoreCase("decimal")) {
            return "BigDecimal";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar") || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        } else if (sqlType.equalsIgnoreCase("TIMESTAMP")) {
            return "Timestamp";
        }
        return null;
    }

    /**
     * java 基本数据类型
     */
    protected String sqlType2JavaTypeForBasic(String sqlType) {
        if (sqlType.equalsIgnoreCase("bit")) {
            return "boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "int";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "float";
        } else if (sqlType.equalsIgnoreCase("numeric") || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money") || sqlType.equalsIgnoreCase("smallmoney")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("decimal")) {
            return "BigDecimal";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar") || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        } else if (sqlType.equalsIgnoreCase("TIMESTAMP")) {
            return "Timestamp";
        }
        return null;
    }

    protected String initcap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

}

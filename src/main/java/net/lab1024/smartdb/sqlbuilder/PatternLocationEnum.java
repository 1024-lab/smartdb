package net.lab1024.smartdb.sqlbuilder;

public enum PatternLocationEnum {
    /**
     * 前后都有<br>
     * eg: like '%abs%'
     */
    AROUND,
    /**
     * 后缀<br>
     * eg: like 'abs%'
     */
    SUFFIX,

    /**
     * 前缀<br>
     * eg: like '%abs'
     */
    PREFIX;

}

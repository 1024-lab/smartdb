package net.lab1024.smartdb.codegenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class SmartDbEntityGenerator {

    protected SmartDbEntityGeneratorBuilder smartDbEntityGeneratorBuilder;
    protected List<String> primaryKeyColumnNames = new ArrayList<String>();
    protected List<String> colRemarkList = new ArrayList<String>();
    protected List<String> colNamesList = new ArrayList<String>();
    protected List<String> colTypesList = new ArrayList<String>();
    protected List<Integer> colSizesList = new ArrayList<Integer>();
    protected boolean importUtilDate = false;

    public SmartDbEntityGenerator(SmartDbEntityGeneratorBuilder smartDbEntityGeneratorBuilder) {
        this.smartDbEntityGeneratorBuilder = smartDbEntityGeneratorBuilder;
    }

    abstract void generate();

}

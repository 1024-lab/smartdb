package net.lab1024.smartdb.xenum;

import net.lab1024.smartdb.SmartDbEnum;

public enum Level implements SmartDbEnum {
    HIGH(1), LOW(2);

    private Integer type;

    Level(Integer type) {
        this.type = type;
    }

    @Override
    public Integer getValue() {
        return type;
    }
}

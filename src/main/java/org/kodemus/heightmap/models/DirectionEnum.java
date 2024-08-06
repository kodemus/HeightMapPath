package org.kodemus.heightmap.models;

import lombok.Getter;

@Getter
public enum DirectionEnum {
    HORIZONTAL, VERTICAL;

    private DirectionEnum other;

    static {
        HORIZONTAL.other = VERTICAL;
        VERTICAL.other = HORIZONTAL;
    }
}

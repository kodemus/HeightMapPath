package org.kodemus.heightmap.models;

import lombok.Data;

@Data
public class HeightMap {

    public int width;
    public int height;
    public int[][] data;
    public int min;
    public int max;
}

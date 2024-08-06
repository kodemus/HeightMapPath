package org.kodemus.heightmap.models;

import lombok.Data;
import lombok.ToString;

@Data
public class ToolPixelMatrix {

    public int radius;
    public int[] minXForY;
    public int[] maxXForY;
    @ToString.Exclude
    public int[][] zForXY;
}

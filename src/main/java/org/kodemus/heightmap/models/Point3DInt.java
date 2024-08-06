package org.kodemus.heightmap.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point3DInt {

    public int x;
    public int y;
    public int z;

    public Point3DInt(Point3DInt point) {
        x = point.x;
        y = point.y;
        z = point.z;
    }
}

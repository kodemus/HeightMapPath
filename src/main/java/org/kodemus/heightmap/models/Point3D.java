package org.kodemus.heightmap.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point3D {

    public double x;
    public double y;
    public double z;

    public Point3D(Point3D point) {
        x = point.x;
        y = point.y;
        z = point.z;
    }
}

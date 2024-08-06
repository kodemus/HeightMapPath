package org.kodemus.heightmap.models;

import lombok.Data;

@Data
public class ToolActionInt {

    public ToolActionTypeEnum type;
    public Point3DInt pos;

    public ToolActionInt(ToolActionTypeEnum type, Point3DInt pos) {
        this.type = type;
        this.pos = new Point3DInt(pos);
    }
}

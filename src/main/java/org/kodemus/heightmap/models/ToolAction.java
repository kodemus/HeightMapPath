package org.kodemus.heightmap.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ToolAction {

    public final ToolActionTypeEnum type;
    public final Point3D pos;
    public int feedRate;
}

package org.kodemus.heightmap.models;

import java.io.File;

public class HeightMapPathOptions {

    // Tool options:

    /**
     * Set the shape of the end mill.
     */
    public ToolShapeEnum toolShape = ToolShapeEnum.FLAT;

    /**
     * Set the diameter of the end mill in mm.
     */
    public double toolDiameter = 6;

    // Tool control options:

    /**
     * Set the maximum step-down in mm. Where the natural toolpath would exceed a cut of this depth, multiple passes are taken instead.
     */
    public double stepDown = 100;

    /**
     * Set the percentage of step over between two path in %.
     */
    public int stepOverPct = 0;

    /**
     * Set the distance to step forward for each point in the path.
     */
    public double stepForward = 0.5;

    /**
     * Set the maximum feed rate in X/Y plane in mm/min.
     */
    public int xyFeedRate = 200;

    /**
     * Set the maximum feed rate in Z axis in mm/min.
     */
    public int zFeedRate = 50;

    /**
     * Set the maximum feed rate for rapid travel moves in mm/min.
     */
    public int rapidFeedRate = 500;

    /**
     * Set the spindle speed in RPM.
     */
    public int speed = 1000;

    // Path generation options:

//            --roughing-only
//    Only do the roughing pass (based on --step-down) and do not do the finish pass. This is useful if you
//    want to use different parameters, or a different tool, for the roughing pass compared to the finish pass.
//    Default: do the finish pass as well as the roughing pass

    /**
     * Set the clearance to leave around the part in mm. Intended so that you can come back again with a finish pass to clean up the part.
     */
    public double clearance = 0;

    /**
     * Set the Z clearance to leave above the part during rapid moves.
     */
    public double rapidClearance = 2;

    /**
     * Set whether the tool will move in horizontal lines
     */
    public boolean horizontalPass = true;

    /**
     * Set whether the tool will move in vertical lines
     */
    public boolean verticalPass = true;

    /**
     * Set whether the tool will do a pass around the surface before the other passes
     */
    public boolean aroundPass = true;

//    --x-offset MM
//    Set the offset of X cordinates.
//    Default: 0
//
//            --y-offset MM
//    Set the offset of Y cordinates.
//    Default: 0
//
//            --z-offset MM
//    Set the offset of Z cordinates.
//    Default: 0
//
//            --ramp-entry
//    Add horizontal movements to plunge cuts where possible, to reduce cutting forces.
//            Default: plunge straight down

//    Heightmap options:

    /**
     * Set the source heightmap file Path
     */
    public File sourceFile;

    /**
     * Set the dest command file Path. If not provided, the commands are sent to the console
     */
    public File destFile;

    /**
     * Set the width of the image in mm. If height is not specified, height will be calculated automatically to maintain aspect ratio. If
     * neither are specified, width=100mm is assumed.
     */
    public int width;

    /**
     * Set the height of the image in mm. If width is not specified, width will be calculated automatically to maintain aspect ratio. If
     * neither are specified, width=100mm is assumed.
     */
    public int height;

    /**
     * Set the total depth of the part in mm.
     */
    public int depth;

//            --x-flip
//    Flip the image in the X axis. This is useful when you want to cut the same shape on the bottom of a part. The origin will still be at top left of the finished toolpath.
//
//            --y-flip
//    Flip the image in the Y axis. This is useful when you want to cut the same shape on the bottom of a part. The origin will still be at top left of the finished toolpath.
//
//            --invert
//    Invert the colours in the image, so that white is the deepest cut and black is the shallowest.
//
//    --deep-black
//    Let the tool cut below the full depth into black (0,0,0) if this would allow better reproduction of the non-black parts of the heightmap.
//    Only really applicable with a ball-nose end mill.
//            Default: treat black (0,0,0) as a hard limit on cut depth

    /**
     * Let the tool cut beyond the edges of the heightmap.
     */
    public boolean beyondEdges = false;

//            --omit-top
//    Don't bother cutting top surfaces that are at the upper limit of the heightmap.
//    Default: cut them
//
//    --normalise
//    Measure the minimum and maximum brightness in the heightmap and stretch all brightness so that the full range of cut depth is achieved.
//            Default: no normalisation
//
//    --normalise-ignore-black
//    When normalising, ignore black (i.e. stretch all brightnesses apart from black, but leave black alone).
//    Note that normalisation is applied before inversion, so if you need this with --invert, you might have to invert the image externally instead.
//            Default: don't ignore black
//
//            --imperial
//    All units are inches instead of mm, and inches/min instead of mm/min. G-code output has G20 instead of G21.
//            Default: not imperial.
//
//            --rgb
//    Use R,G,B channels independently to get 24 bits of height data instead of 8.
//    Default: greyscale

    /**
     * Read stock heightmap from PNGFILE, to save cutting air in roughing passes.
     */
    public File readStockFile;

    /**
     * Write output heightmap to PNGFILE, to use with --read-stock.
     */
    public File writeStockFile;

//    Cycle time options:

//            --max-vel MM/MIN
//    Max. velocity in mm/min for cycle time calculation.
//            Default: 4000.
//
//            --max-accel MM/SEC^2
//    Max. acceleration in mm/sec^2 for cycle time calculation.
//            Default: 50.

//    Output options:

//            --quiet
//    Suppress output of dimensions, resolutions, and progress.

}

package org.kodemus.heightmap;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import javax.imageio.ImageIO;
import org.kodemus.heightmap.models.DirectionEnum;
import org.kodemus.heightmap.models.HeightMap;
import org.kodemus.heightmap.models.HeightMapPathOptions;
import org.kodemus.heightmap.models.Point3D;
import org.kodemus.heightmap.models.Point3DInt;
import org.kodemus.heightmap.models.ToolAction;
import org.kodemus.heightmap.models.ToolActionInt;
import org.kodemus.heightmap.models.ToolActionTypeEnum;
import org.kodemus.heightmap.models.ToolPath;
import org.kodemus.heightmap.models.ToolPixelMatrix;
import org.kodemus.heightmap.models.ToolShapeEnum;

/**
 * Hello world!
 */
public class HeightMapPath {

    String name;
    HeightMapPathOptions options;
    HeightMap heightMap;
    HeightMap stock;
    HeightMap stepDownLimit;
    ToolPixelMatrix toolPixelMatrix;
    ToolPath path;

    public HeightMapPath(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws IOException {
        var processes = new ArrayList<HeightMapPath>();
        HeightMapPath process;

        process = new HeightMapPath("D6");
        process.setupD6();
        processes.add(process);

        process = new HeightMapPath("D6-1");
        process.setupD6_1();
        processes.add(process);

//        process = new HeightMapPath("B3");
//        process.setupB3();
//        processes.add(process);

        process = new HeightMapPath("B3-1");
        process.setupB3_1();
        processes.add(process);

        for (int i = 1; i < processes.size(); i++) {
            processes.get(i).options.readStockFile = processes.get(i - 1).options.writeStockFile;
        }

        for (var heightMapPath : processes) {
            System.out.println(heightMapPath.name);
            heightMapPath.process();
        }
    }

    public void setup(String[] args) throws IOException {
        options = new HeightMapPathOptions();
        options.sourceFile = new File("C:/Applications/cnc/heightmaps/fleur/fleur512.png");
        options.width = 100;
        options.height = 100;
        options.depth = 10;
        options.toolDiameter = 6;
        options.toolShape = ToolShapeEnum.FLAT;
        options.stepOverPct = 0;
        options.stepDown = 3;
        options.horizontalPass = true;
        options.verticalPass = false;
        options.writeStockFile = new File("C:/Applications/cnc/heightmaps/fleur3/fleur-stock-D6.png");
        options.destFile = new File("C:/Applications/cnc/heightmaps/fleur3/fleur-D6.nc");

        heightMap = readHeightMapFile(options.sourceFile);
        stock = options.readStockFile != null ? readHeightMapFile(options.readStockFile) : createStock(heightMap);

        initToolPixelMatrix();
    }

    public void setupD6() {
        options = new HeightMapPathOptions();
        options.sourceFile = new File("C:/Applications/cnc/heightmaps/fleur/fleur512.png");
        options.width = 100;
        options.height = 100;
        options.depth = 10;
        options.toolDiameter = 6;
        options.toolShape = ToolShapeEnum.FLAT;
        options.stepOverPct = 0;
        options.stepDown = 3;
        options.clearance = 0.5;
        options.horizontalPass = true;
        options.verticalPass = false;
        options.xyFeedRate = 400;
        options.writeStockFile = new File("C:/Applications/cnc/heightmaps/fleur3/fleur-stock-D6.png");
        options.destFile = new File("C:/Applications/cnc/heightmaps/fleur3/fleur-D6.nc");
    }

    public void setupD6_1() {
        options = new HeightMapPathOptions();
        options.sourceFile = new File("C:/Applications/cnc/heightmaps/fleur/fleur512.png");
        options.width = 100;
        options.height = 100;
        options.depth = 10;
        options.toolDiameter = 6;
        options.toolShape = ToolShapeEnum.FLAT;
        options.stepOverPct = 0;
        options.stepDown = 3;
        options.clearance = 0;
        options.horizontalPass = true;
        options.verticalPass = false;
        options.xyFeedRate = 400;
        options.writeStockFile = new File("C:/Applications/cnc/heightmaps/fleur3/fleur-stock-D6-1.png");
        options.destFile = new File("C:/Applications/cnc/heightmaps/fleur3/fleur-D6-1.nc");
    }

    public void setupB3() {
        options = new HeightMapPathOptions();
        options.sourceFile = new File("C:/Applications/cnc/heightmaps/fleur/fleur512.png");
        options.width = 100;
        options.height = 100;
        options.depth = 10;
        options.toolDiameter = 3.175;
        options.toolShape = ToolShapeEnum.BALL;
        options.stepOverPct = 0;
        options.stepDown = 3;
        options.clearance = 0;
        options.horizontalPass = true;
        options.verticalPass = false;
        options.xyFeedRate = 400;
        options.writeStockFile = new File("C:/Applications/cnc/heightmaps/fleur3/fleur-stock-B3.png");
        options.destFile = new File("C:/Applications/cnc/heightmaps/fleur3/fleur-B3.nc");
    }

    public void setupB3_1() {
        options = new HeightMapPathOptions();
        options.sourceFile = new File("C:/Applications/cnc/heightmaps/fleur/fleur512.png");
        options.width = 100;
        options.height = 100;
        options.depth = 10;
        options.toolDiameter = 3.175;
        options.toolShape = ToolShapeEnum.BALL;
        options.stepOverPct = 30;
        options.stepDown = 3;
        options.clearance = 0.2;
        options.horizontalPass = true;
        options.verticalPass = false;
        options.xyFeedRate = 400;
        options.writeStockFile = new File("C:/Applications/cnc/heightmaps/fleur3/fleur-stock-B3-1.png");
        options.destFile = new File("C:/Applications/cnc/heightmaps/fleur3/fleur-B3-1.nc");
    }

    public void process() throws IOException {
        init();
        createPath();
        translatePath();
        calcPathDuration();
        writePath();
        if (options.writeStockFile != null) {
            exportHeightMapFile(stock, options.writeStockFile);
        }
        System.out.printf("Duration : %s%n", Duration.ofSeconds((long) path.duration));
    }

    public void init() throws IOException {
        heightMap = readHeightMapFile(options.sourceFile);
        stock = options.readStockFile != null ? readHeightMapFile(options.readStockFile) : createStock(heightMap);

        stepDownLimit = new HeightMap();
        stepDownLimit.width = heightMap.width;
        stepDownLimit.height = heightMap.height;
        stepDownLimit.data = new int[heightMap.height][];
        for (int j = 0; j < heightMap.height; j++) {
            stepDownLimit.data[j] = new int[heightMap.width];
        }

        initToolPixelMatrix();
    }

    private void createPath() {
        path = new ToolPath();

        var stepOver = realToPixelXy(options.toolDiameter * (1 - options.stepOverPct / 100.));
        var aroundStepOver = options.stepOverPct >= 50 ? stepOver : realToPixelXy(options.toolDiameter / 2);

        var aroundMinX = realToPixelXy(options.beyondEdges ? 0 : options.toolDiameter / 2 + options.clearance);
        var aroundMaxX = realToPixelXy(options.width - (options.beyondEdges ? 0 : options.toolDiameter / 2 + options.clearance));
        var aroundMinY = realToPixelXy(options.beyondEdges ? 0 : options.toolDiameter / 2 + options.clearance);
        var aroundMaxY = realToPixelXy(options.height - (options.beyondEdges ? 0 : options.toolDiameter / 2 + options.clearance));

        var minX = options.aroundPass ? aroundMinX + aroundStepOver : aroundMinX;
        var maxX = options.aroundPass ? aroundMaxX - aroundStepOver : aroundMaxX;
        var minY = options.aroundPass ? aroundMinY + aroundStepOver : aroundMinY;
        var maxY = options.aroundPass ? aroundMaxY - aroundStepOver : aroundMaxY;

        Point3DInt pos;

        var aroundPassDone = false;

        var straightPasses = new ArrayList<DirectionEnum>();
        if (options.horizontalPass) {
            straightPasses.add(DirectionEnum.HORIZONTAL);
        }
        if (options.verticalPass) {
            straightPasses.add(DirectionEnum.VERTICAL);
        }

        for (var passDirection : straightPasses) {
            boolean bottomNotReached = true;

            while (bottomNotReached) {
                bottomNotReached = false;

                calcStepDownLimit();

                if (options.aroundPass && !aroundPassDone) {
                    pos = new Point3DInt(aroundMinX, aroundMinY, 256 + realToPixelZ(options.rapidClearance));
                    path.actionsInt.add(new ToolActionInt(ToolActionTypeEnum.G0, pos));

                    var target = new Point3DInt(aroundMaxX, pos.y, 0);
                    bottomNotReached = createPathLine(pos, target, DirectionEnum.HORIZONTAL, true);
                    target.y = aroundMaxY;
                    bottomNotReached |= createPathLine(pos, target, DirectionEnum.VERTICAL, true);
                    target.x = aroundMinX;
                    bottomNotReached |= createPathLine(pos, target, DirectionEnum.HORIZONTAL, false);
                    target.y = aroundMinY;
                    bottomNotReached |= createPathLine(pos, target, DirectionEnum.VERTICAL, false);

                    pos = new Point3DInt(aroundMinX, aroundMinY, 256 + realToPixelZ(options.rapidClearance));
                    path.actionsInt.add(new ToolActionInt(ToolActionTypeEnum.G0, pos));
                }

                calcStepDownLimit();

                pos = new Point3DInt(minX, minY, 256 + realToPixelZ(options.rapidClearance));
                path.actionsInt.add(new ToolActionInt(ToolActionTypeEnum.G0, pos));

                boolean moveAsc = true;
                while (true) {
                    var target = new Point3DInt(passDirection == DirectionEnum.HORIZONTAL ? (moveAsc ? maxX : minX) : pos.x,
                            passDirection == DirectionEnum.VERTICAL ? (moveAsc ? maxY : minY) : pos.y,
                            0);
                    bottomNotReached |= createPathLine(pos, target, passDirection, moveAsc);

                    if (passDirection == DirectionEnum.HORIZONTAL) {
                        if (pos.y >= maxY) {
                            break;
                        }

                        target.y = Math.min(target.y + stepOver, maxY);

                    } else {
                        if (pos.x >= maxX) {
                            break;
                        }

                        target.x = Math.min(target.x + stepOver, maxX);
                    }

                    bottomNotReached |= createPathLine(pos, target, passDirection.getOther(), true);

                    moveAsc = !moveAsc;
                }

                pos.z = 256 + realToPixelZ(options.rapidClearance);
                path.actionsInt.add(new ToolActionInt(ToolActionTypeEnum.G0, pos));
            }

            aroundPassDone = options.aroundPass;
        }
    }

    private void calcStepDownLimit() {
        for (int j = 0; j < stock.height; j++) {
            for (int i = 0; i < stock.width; i++) {
                stepDownLimit.data[j][i] = Math.max(stock.data[j][i] - realToPixelZ(options.stepDown), 0);
            }
        }
    }

    private boolean createPathLine(Point3DInt pos, Point3DInt target, DirectionEnum direction, boolean minToMax) {
        var bottomNotReached = false;

        if (path.actionsInt.getLast().type == ToolActionTypeEnum.G0) {
            bottomNotReached |= createPathAction(pos);
        }

        while (pos.x != target.x || pos.y != target.y) {
            if (direction == DirectionEnum.HORIZONTAL) {
                pos.x += minToMax ? 1 : -1;
            } else {
                pos.y += minToMax ? 1 : -1;
            }

            bottomNotReached |= createPathAction(pos);
        }

        return bottomNotReached;
    }

    private boolean createPathAction(Point3DInt pos) {
        var minZStep = calcMapDepthForTool(stepDownLimit, pos);
        var targetHeight = calcMapDepthForTool(heightMap, pos);
        var bottomNotReached = minZStep > targetHeight;
        if (bottomNotReached) {
            pos.z = minZStep;
        } else {
            pos.z = targetHeight;
        }
        path.actionsInt.add(new ToolActionInt(ToolActionTypeEnum.G1, pos));

        applyActionToStock(pos);

        return bottomNotReached;
    }

    private int calcMapDepthForTool(HeightMap heightMap, Point3DInt pos) {
        var minY = Math.max(0, pos.y - toolPixelMatrix.radius);
        var maxY = Math.min(heightMap.width - 1, pos.y + toolPixelMatrix.radius);

        var depth = Integer.MIN_VALUE;
        var j = minY - pos.y + toolPixelMatrix.radius;
        for (var y = minY; y <= maxY; y++) {
            var minX = Math.max(0, pos.x + toolPixelMatrix.minXForY[j]);
            var maxX = Math.min(heightMap.height - 1, pos.x + toolPixelMatrix.maxXForY[j]);

            var i = minX - pos.x + toolPixelMatrix.radius;
            for (var x = minX; x <= maxX; x++) {
                depth = Math.max(depth, heightMap.data[y][x] - toolPixelMatrix.zForXY[j][i]);
                i++;
            }
            j++;
        }

        return depth;
    }

    private void applyActionToStock(Point3DInt pos) {
        var minY = Math.max(0, pos.y - toolPixelMatrix.radius);
        var maxY = Math.min(heightMap.width - 1, pos.y + toolPixelMatrix.radius);

        var j = minY - pos.y + toolPixelMatrix.radius;
        for (var y = minY; y <= maxY; y++) {
            var minX = Math.max(0, pos.x + toolPixelMatrix.minXForY[j]);
            var maxX = Math.min(heightMap.height - 1, pos.x + toolPixelMatrix.maxXForY[j]);

            var i = minX - pos.x + toolPixelMatrix.radius;
            for (var x = minX; x <= maxX; x++) {
                stock.data[y][x] = Math.min(stock.data[y][x], pos.z + toolPixelMatrix.zForXY[j][i]);
                i++;
            }
            j++;
        }
    }

    private void translatePath() {
        var pos = new Point3D(0, 0, 0);
        path.actions = new ArrayList<>();
        for (var actionInt : path.actionsInt) {
            var action = new ToolAction(actionInt.type, pixelToReal(actionInt.pos));
            if (action.type == ToolActionTypeEnum.G0) {
                action.feedRate = options.rapidFeedRate;
            } else {
                var distXy = Math.sqrt((action.pos.x - pos.x) * (action.pos.x - pos.x) + (action.pos.y - pos.y) * (action.pos.y - pos.y));
                var distZ = action.pos.z < pos.z ? pos.z - action.pos.z : 0;
                action.feedRate = distXy == 0 && distZ == 0 ? options.xyFeedRate
                        : (int) ((distXy * options.xyFeedRate + distZ * options.zFeedRate) / (distXy + distZ));
            }
            path.actions.add(action);
            pos = action.pos;
        }
    }

    private void calcPathDuration() {
        var pos = new Point3D(0, 0, 0);
        path.duration = 0;
        for (var action : path.actions) {
            var dist = Math.sqrt((action.pos.x - pos.x) * (action.pos.x - pos.x)
                    + (action.pos.y - pos.y) * (action.pos.y - pos.y)
                    + (action.pos.z - pos.z) * (action.pos.z - pos.z));
            pos = action.pos;
            path.duration += dist / action.feedRate * 60;
        }
    }

    private void writePath() throws IOException {
        try (var out = options.destFile == null ? System.out
                : new PrintStream(new BufferedOutputStream(new FileOutputStream(options.destFile)))) {
            for (var action : path.actions) {
                out.printf(Locale.US, "%s X%.3f Y%.3f Z%.3f F%d%n", action.type, action.pos.x, action.pos.y, action.pos.z, action.feedRate);
            }
        }
    }

    public int realToPixelXy(double dist) {
        return (int) (dist * heightMap.width / options.width);
    }

    public int realToPixelZ(double dist) {
        return (int) (dist * 256 / options.depth);
    }

    public double pixelToRealXy(int dist) {
        return (double) dist * options.width / heightMap.width;
    }

    public double pixelToRealZ(int dist) {
        return (double) dist * options.depth / 256;
    }

    public Point3D pixelToReal(Point3DInt pos) {
        return new Point3D(pixelToRealXy(pos.x), pixelToRealXy(pos.y), pixelToRealZ(pos.z));
    }

    public static HeightMap readHeightMapFile(File file) throws IOException {
        var image = ImageIO.read(file);
        var heightMap = new HeightMap();
        heightMap.width = image.getWidth();
        heightMap.height = image.getHeight();
        heightMap.data = new int[image.getHeight()][];

        byte[] pixelData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        int i = 0;
        heightMap.min = Integer.MAX_VALUE;
        heightMap.max = Integer.MIN_VALUE;
        for (int y = 0; y < image.getHeight(); y++) {
            var line = new int[image.getWidth()];
            heightMap.getData()[y] = line;
            for (int x = 0; x < image.getWidth(); x++) {
                int r = pixelData[i] & 0xFF;
                int g = pixelData[i + 1] & 0xFF;
                int b = pixelData[i + 2] & 0xFF;
                var val = (r + g + b) / 3;
                line[x] = val;
                if (val < heightMap.min) {
                    heightMap.min = val;
                }
                if (val > heightMap.max) {
                    heightMap.max = val;
                }

                i += 3;
            }
        }

        return heightMap;
    }

    private static void exportHeightMapFile(HeightMap heightMap, File file) throws IOException {
        var image = new BufferedImage(heightMap.width, heightMap.height, BufferedImage.TYPE_3BYTE_BGR);
        var imagePixels = new int[heightMap.width * heightMap.height];
        for (int j = 0; j < heightMap.height; j++) {
            for (int i = 0; i < heightMap.width; i++) {
                int depth = heightMap.data[j][i];
                imagePixels[j * heightMap.width + i] = (depth & 0xFF) << 16 | (depth & 0xFF) << 8 | (depth & 0xFF);
            }
        }
        image.setRGB(0, 0, heightMap.width, heightMap.height, imagePixels, 0, heightMap.width);
        ImageIO.write(image, "png", file);
    }

    private static HeightMap createStock(HeightMap heightmap) {
        var stock = new HeightMap();
        stock.width = heightmap.width;
        stock.height = heightmap.height;
        stock.data = new int[heightmap.height][];
        for (int y = 0; y < stock.height; y++) {
            stock.data[y] = new int[stock.width];
            Arrays.fill(stock.data[y], 255);
        }
        return stock;
    }

    private void initToolPixelMatrix() {
        toolPixelMatrix = new ToolPixelMatrix();

        toolPixelMatrix.radius = realToPixelXy(options.toolDiameter / 2);
        var nbLines = toolPixelMatrix.radius * 2 + 1;
        toolPixelMatrix.minXForY = new int[nbLines];
        toolPixelMatrix.maxXForY = new int[nbLines];
        toolPixelMatrix.zForXY = new int[nbLines][];

        var radiusSquare = toolPixelMatrix.radius * toolPixelMatrix.radius;

        for (int y = -toolPixelMatrix.radius; y <= toolPixelMatrix.radius; y++) {
            int j = y + toolPixelMatrix.radius;
            var ySquare = y * y;

            toolPixelMatrix.zForXY[j] = new int[nbLines];

            int x = -toolPixelMatrix.radius;
            while (x * x + ySquare > radiusSquare) {
                x++;
            }
            toolPixelMatrix.minXForY[j] = x;
            while (x * x + ySquare <= radiusSquare) {
                int i = x + toolPixelMatrix.radius;
                if (options.toolShape == ToolShapeEnum.BALL) {
                    toolPixelMatrix.zForXY[j][i] = toolPixelMatrix.radius - (int) Math.sqrt((double) radiusSquare - (x * x + ySquare));
                }
                x++;
            }
            toolPixelMatrix.maxXForY[j] = x - 1;
        }
    }
}

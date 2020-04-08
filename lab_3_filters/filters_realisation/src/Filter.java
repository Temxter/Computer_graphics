public class Filter {

    private int maxX;
    private int maxY;
    private int centralX;
    private int centralY;
    private byte[][] filter;

    public Filter(int maxX, int maxY, int centralX, int centralY, byte[][] filter) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.centralX = centralX;
        this.centralY = centralY;
        this.filter = filter;
    }

    public int getCentralX() {
        return centralX;
    }

    public int getCentralY() {
        return centralY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getValue(int x, int y) {
        return filter[x][y] & 0xFF;
    }

    public byte getByteValue(int x, int y) {
        return filter[x][y];
    }
}

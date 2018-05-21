package util;

public class DBG {

    public static final boolean UI = true;
    public static final boolean CONTROL = true;
    public static final boolean BOARD = false;
    public static final boolean PLAYERS = true;

    public boolean debug;
    private String file;

    public DBG(boolean debugOrNot, String file) {
        this.debug = debugOrNot;
        this.file = file;
    }
    public DBG(boolean debugOrNot) { this(debugOrNot, ""); }
    public DBG() { this(false, ""); }

    public void println(String msg) {
        System.out.println(this.file + ": " + msg);
    }
}

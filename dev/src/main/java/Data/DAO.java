package Data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public abstract class DAO {
    protected SQLDB db;
    static String currentDirectory = System.getProperty("user.dir");
    static File currentDirFile = new File(currentDirectory);
//    static String parentDirectory = currentDirFile.getParent() + "\\dev\\src\\main\\resources\\MYDB.db";
//    protected static final String URL = ("jdbc:sqlite:" + parentDirectory);
    protected static String URL = "jdbc:sqlite:src\\main\\resources\\MYDB.db";
    protected Connection conn;

    public DAO(){
            try {
                this.conn = DriverManager.getConnection(URL);
                this.db = new SQLDB();
            } catch (SQLException e) {
                System.err.println("Error establishing connection: " + e.getMessage());
                e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void ensureConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL);
        }
    }

    public boolean isCurr(int id) throws Exception {
        ensureConnection();
        boolean ans = db.isCurr(conn, id);
        closeConnection();
        return ans;
    }

    public boolean readConfig(List<Integer> confidata) throws Exception {
        ensureConnection();
        boolean ans = db.loadConfig(conn,confidata);
        closeConnection();
        return ans;
    }


}

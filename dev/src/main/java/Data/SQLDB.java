package Data;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLDB {

    static String currentDirectory = System.getProperty("user.dir");
    static File currentDirFile = new File(currentDirectory);
    //static String parentDirectory = currentDirFile.getParent() + "\\dev\\src\\main\\resources\\MYDB.db";
    //protected static String URL = ("jdbc:sqlite:" + parentDirectory);
    protected static String URL = "jdbc:sqlite:src\\main\\resources\\MYDB.db";

    public static void createTables2() throws SQLException {
        try (Connection conn=DriverManager.getConnection(URL)){
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS currentWorkers (" +
                    " id INTEGER PRIMARY KEY," +
                    " firstName TEXT NOT NULL," +
                    " lastName TEXT NOT NULL," +
                    " bankAccount TEXT NOT NULL," +
                    " startDate TEXT NOT NULL," +
                    " endDate TEXT NOT NULL," +
                    " hourlySalary DOUBLE NOT NULL," +
                    " branch TEXT NOT NULL," +
                    " Salary DOUBLE" +
                    ");";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS workerJobs (" +
                    " id INTEGER NOT NULL ," +
                    " Job TEXT NOT NULL," +
                    "PRIMARY KEY (id, Job)" +
                    ");";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS Transport (" +
                    " idS INTEGER," +
                    " date TEXT NOT NULL," +
                    " branch TEXT NOT NULL," +
                    " type INTEGER NOT NULL," +
                    " workerID INTEGER NOT NULL," +
                    " truck TEXT NOT NULL," +
                    "PRIMARY KEY (date, branch,type)" +
                    ");";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS prevWorkers (" +
                    " id INTEGER PRIMARY KEY," +
                    " firstName TEXT NOT NULL," +
                    " lastName TEXT NOT NULL," +
                    " bankAccount TEXT NOT NULL," +
                    " startDate TEXT NOT NULL," +
                    " endDate TEXT NOT NULL," +
                    " hourlySalary DOUBLE NOT NULL," +
                    " Branch TEXT NOT NULL" +
                    ");";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS shifts (" +
                    " id TEXT PRIMARY KEY," +
                    " shiftDate TEXT," +
                    " branch TEXT," +
                    " shiftType INTEGER," +
                    " Cashier INTEGER," +
                    " Delivery_Person INTEGER," +
                    " Manager INTEGER," +
                    " Store_Keeper INTEGER," +
                    " DriverB INTEGER," +
                    " DriverC INTEGER," +
                    " DriverD INTEGER" +
                    ");";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS configurations (" +
                    " countShiftPerDay INTEGER NOT NULL," +
                    " worksDayinWeek INTEGER NOT NULL," +
                    " minimumSalary INTEGER NOT NULL," +
                    " generalCounterGeneration INTEGER NOT NULL," +
                    " morningShiftHours INTEGER NOT NULL," +
                    " eveningShiftHours INTEGER NOT NULL," +
                    " systemPassword INTEGER NOT NULL," +
                    " daysOff INTEGER NOT NULL" +
                    ");";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS availability (" +
                    " id INTEGER NOT NULL," +
                    " M1 INTEGER NOT NULL," +
                    " M2 INTEGER NOT NULL," +
                    " M3 INTEGER NOT NULL," +
                    " M4 INTEGER NOT NULL," +
                    " M5 INTEGER NOT NULL," +
                    " M6 INTEGER NOT NULL," +
                    " M7 INTEGER NOT NULL," +
                    " E1 INTEGER NOT NULL," +
                    " E2 INTEGER NOT NULL," +
                    " E3 INTEGER NOT NULL," +
                    " E4 INTEGER NOT NULL," +
                    " E5 INTEGER NOT NULL," +
                    " E6 INTEGER NOT NULL," +
                    " E7 INTEGER NOT NULL," +
                    " startDate TEXT NOT NULL," +
                    " endDate TEXT NOT NULL," +
                    " PRIMARY KEY (id, startDate)" +
                    ");";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS participatesWorkersinShift (" +
                    " date TEXT NOT NULL," +
                    " type INTEGER NOT NULL," +
                    " idworker INTEGER," +
                    " branch TEXT," +
                    " job TEXT NOT NULL," +
                    " PRIMARY KEY (date, type, branch,idworker)" +
                    ");";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS assignPres (" +
                    " idAssign INTEGER NOT NULL," +
                    " startDate TEXT NOT NULL," +
                    " endDate TEXT NOT NULL," +
                    " branch TEXT NOT NULL" +
                    ");";
            stmt.execute(sql);
            conn.commit();

        } catch (SQLException e) {
            throw e;
        }
    }




    public boolean workerExists(Connection conn, int id) throws SQLException {
        conn.setAutoCommit(false);
        String sql = "SELECT COUNT(*) FROM currentWorkers WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<String> readWorkerfromDBRoles(Connection conn, int id) throws Exception {
        conn.setAutoCommit(false);
        List<String> jobs = new ArrayList<>();
        String sqlJobs = "SELECT * FROM workerJobs WHERE id = ?";
        try (PreparedStatement pstmtJobs = conn.prepareStatement(sqlJobs)) {
            pstmtJobs.setInt(1, id);
            try (ResultSet rs = pstmtJobs.executeQuery()) {
                while (rs.next()) {
                    jobs.add(rs.getString("Job"));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error reading jobs for worker ID " + id + ": " + e.getMessage(), e);
        }
        return jobs;
    }

    public List<String> readWorkerfromDBWorker(Connection conn, int id) throws Exception {
        conn.setAutoCommit(false);
        List<String> worker =null;
        String sqlWorker = "SELECT * FROM currentWorkers WHERE id = ?";

        try {
            PreparedStatement pstmtWorker = conn.prepareStatement(sqlWorker);
            pstmtWorker.setInt(1, id);
            try (ResultSet rs = pstmtWorker.executeQuery()) {
                if (rs.next()) {
                    worker = new ArrayList<>();
                    worker.add(String.valueOf(rs.getInt("id")));
                    worker.add(rs.getString("firstName"));
                    worker.add(rs.getString("lastName"));
                    worker.add(rs.getString("bankAccount"));
                    worker.add(rs.getString("startDate"));
                    worker.add(rs.getString("endDate"));
                    worker.add(String.valueOf(rs.getDouble("hourlySalary")));
                    worker.add(rs.getString("branch"));
                    worker.add(String.valueOf(rs.getDouble("Salary")));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error reading worker from DB: " + e.getMessage());
        }

        return worker;
    }

    public List<String> readShiftbyDTB(Connection conn, String date, int type, String branch) throws Exception {
        List<String> shiftDetails = new ArrayList<>();
        String sqlShift = "SELECT * FROM shifts WHERE shiftDate = ? and branch = ? and shiftType = ?";
        conn.setAutoCommit(false);
        try (PreparedStatement pstmtShift = conn.prepareStatement(sqlShift)) {
            pstmtShift.setString(1, date);
            pstmtShift.setString(2, branch);
            pstmtShift.setInt(3, type);
            try (ResultSet rs = pstmtShift.executeQuery()) {
                if (rs.next()) {
                    shiftDetails.add(String.valueOf(rs.getInt("id")));
                    shiftDetails.add(rs.getString("shiftDate"));
                    shiftDetails.add(String.valueOf(rs.getInt("shiftType")));
                    shiftDetails.add(rs.getString("branch"));
                    shiftDetails.add(String.valueOf(rs.getInt("Cashier")));
                    shiftDetails.add(String.valueOf(rs.getInt("Delivery_Person")));
                    shiftDetails.add(String.valueOf(rs.getInt("Manager")));
                    shiftDetails.add(String.valueOf(rs.getInt("Store_Keeper")));
                    shiftDetails.add(String.valueOf(rs.getInt("DriverB")));
                    shiftDetails.add(String.valueOf(rs.getInt("DriverC")));
                    shiftDetails.add(String.valueOf(rs.getInt("DriverD")));
                }
            conn.commit();
            }
            
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error reading shift from DB: " + e.getMessage());
        }

        return shiftDetails;
    }

    public static void dropAllTables2() throws Exception {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(URL);
           conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = OFF;");
            try (ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table';")) {
                while (rs.next()) {
                    String tableName = rs.getString("name");
                    stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName);
                }
            }
            stmt.execute("PRAGMA foreign_keys = ON;");
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error dropping tables: " + e.getMessage());
        }
    }


    public boolean updateWorkerDetails(Connection conn, int id, String firstName, String lastName, String bankAccount, String startDate, String endDate, double hourlySalary, String branch) throws Exception {
        conn.setAutoCommit(false);
        String sql = "UPDATE currentWorkers SET firstName = ?, lastName = ?, bankAccount = ?, startDate = ?, endDate = ?, hourlySalary = ?, branch = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, bankAccount);
            pstmt.setString(4, startDate);
            pstmt.setString(5, endDate);
            pstmt.setDouble(6, hourlySalary);
            pstmt.setString(7, branch);
            pstmt.setInt(8, id);
            pstmt.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error updating worker details: " + e.getMessage());
        }
    }

    public boolean updateContractDetails(Connection conn, int id, String startDate, String endDate, double salary) throws Exception {
        conn.setAutoCommit(false);
        String sql = "UPDATE currentWorkers SET startDate = ?, endDate = ?, hourlySalary = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            pstmt.setDouble(3, salary);
            pstmt.setInt(4, id);
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error updating worker details for worker ID " + id + ": " + e.getMessage());
        }
    }

    public void deleteWorkerById(Connection conn, int id) throws Exception {
        conn.setAutoCommit(false);
        String sql = "DELETE FROM currentWorkers WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            if (affectedRows > 0) {
            } else {
                conn.rollback();
                throw new Exception("No worker found with ID " + id + ".");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error deleting worker: " + e.getMessage());
        }
    }

    public static void deleteAllData2() throws Exception {
        String[] tables = {
                "currentWorkers",
                "workerJobs",
                "prevWorkers",
                "shifts",
                "configurations",
                "availability",
                "participatesWorkersinShift",
                "Transport",
                "assignPres"
        };
        Connection conn=null;
        try{conn  = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            conn.setAutoCommit(false);
            stmt.execute("PRAGMA foreign_keys = OFF;");
            for (String table : tables) {
                stmt.executeUpdate("DELETE FROM " + table);
            }
            stmt.execute("PRAGMA foreign_keys = ON;");
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error deleting all data: " + e.getMessage());
        }
    }

    public boolean loadConfig(Connection conn, List<Integer> confidata) throws Exception {
        conn.setAutoCommit(false);
        String sql = "INSERT INTO configurations(countShiftPerDay, worksDayinWeek, minimumSalary,generalCounterGeneration, morningShiftHours, eveningShiftHours, systemPassword,daysOff) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, confidata.get(0));
            pstmt.setInt(2, confidata.get(1));
            pstmt.setInt(3, confidata.get(2));
            pstmt.setInt(4, confidata.get(3));
            pstmt.setInt(5, confidata.get(4));
            pstmt.setInt(6, confidata.get(5));
            pstmt.setInt(7, confidata.get(6));
            pstmt.setInt(8, confidata.get(7));
            pstmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Error adding configurations : " + e.getMessage());
        }
        return true;
    }
    public boolean insertWorker(Connection conn, int id, String fn, String ln, String ba, String b, String startDate, String endDate, double hourlySalary, String r) throws Exception {
        conn.setAutoCommit(false);
        if (workerExists(conn, id)) {
            conn.rollback();
            throw new Exception("Worker with ID " + id + " already exists.");
        }
        String sql = "INSERT INTO currentWorkers(id, firstName, lastName, bankAccount, startDate, endDate, hourlySalary, branch,Salary) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, id);
            pstmt.setString(2, fn);
            pstmt.setString(3, ln);
            pstmt.setString(4, ba);
            pstmt.setString(5, startDate);
            pstmt.setString(6, endDate);
            pstmt.setDouble(7, hourlySalary);
            pstmt.setString(8, b);
            pstmt.setDouble(9,0);
            pstmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Error inserting worker: " + e.getMessage());
        }
        return true;
    }

    public boolean insertRoles2Worker(Connection conn, int id, String r) throws Exception {

        conn.setAutoCommit(false);
        String sql = "INSERT INTO workerJobs (id, Job) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, r);
            pstmt.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {

            conn.rollback();
            throw new Exception("Error inserting role for worker ID " + id + ": " + e.getMessage(), e);
        }
    }

    public boolean insertShift(Connection conn, int id, String date, String b, int type, Map<String, Integer> counter) throws Exception {

        if(counter == null|| counter.isEmpty()) {
            counter=new HashMap<>();
            counter.put("Cashier",-1);
            counter.put("Delivery_Person",-1);
            counter.put("Manager",-1);
            counter.put("Store_Keeper",-1);
            counter.put("DriverB",-1);
            counter.put("DriverC",-1);
            counter.put("DriverD",-1);
        }
        conn.setAutoCommit(false);
        String sql = "INSERT INTO shifts(id, shiftDate, branch, shiftType, Cashier, Delivery_Person, Manager, Store_Keeper, DriverB, DriverC, DriverD) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, date);
            pstmt.setString(3, b);
            pstmt.setInt(4, type);
            pstmt.setInt(5, counter.get("Cashier"));
            pstmt.setInt(6, counter.get("Delivery_Person"));
            pstmt.setInt(7, counter.get("Manager"));
            pstmt.setInt(8, counter.get("Store_Keeper"));
            pstmt.setInt(9, counter.get("DriverB"));
            pstmt.setInt(10, counter.get("DriverC"));
            pstmt.setInt(11, counter.get("DriverD"));
            pstmt.executeUpdate();
            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Error inserting shift: " + e.getMessage());
        }
    }

    public boolean insertAvailability(Connection conn, int id, int[] morning, int[] evening, String startDate, String endDate) throws Exception {
        conn.setAutoCommit(false);
        String sql = "INSERT INTO availability(id, M1, M2, M3, M4, M5, M6, M7, E1, E2, E3, E4, E5, E6, E7, startDate, endDate) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            for (int i = 0; i < 7; i++) {
                pstmt.setInt(i + 2, morning[i]);
                pstmt.setInt(i + 9, evening[i]);
            }
            pstmt.setString(16, startDate);
            pstmt.setString(17, endDate);
            pstmt.executeUpdate();
            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Error inserting availability: " + e.getMessage());
        }
    }

    public boolean insertParticipatesWorkersInShift(Connection conn, String date,int type, int workerId, String job,String branch) throws Exception {
        conn.setAutoCommit(false);
        String sql = "INSERT INTO participatesWorkersinShift(date,type, idworker, branch, job) VALUES(?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setInt(2, type);
            pstmt.setInt(3, workerId);
            pstmt.setString(4, branch);
            pstmt.setString(5, job);
            pstmt.executeUpdate();
            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Error inserting participatesWorkersInShift: " + e.getMessage());
        }
    }

    public boolean deleteShift(Connection conn, String date, int type, String branch) throws Exception {
//        conn.setAutoCommit(false);
        String sql = "DELETE FROM shifts WHERE shiftDate = ? and  shiftType = ? and branch = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setInt(2, type);
            pstmt.setString(3, branch);
            int isDeleted = pstmt.executeUpdate();
//            conn.commit();
            if (isDeleted > 0) {
                return true;
            } else {
                conn.rollback();
                throw new Exception("No shift found");
            }
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Error deleting shift: " + e.getMessage());
        }
    }

    public List<Integer> readWorkerByRole(Connection conn, String role) throws Exception {
        conn.setAutoCommit(false);
        List<Integer> workers = new ArrayList<>();
        String sql = "SELECT id FROM workerJobs WHERE Job = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, role);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    workers.add(rs.getInt("id"));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error reading worker by role from DB: " + e.getMessage());
        }
        return workers;
    }

    public boolean deleteRoleFromWorker(Connection conn, int id, String role) throws Exception {
        conn.setAutoCommit(false);
        String sql = "DELETE FROM workerJobs WHERE id = ? AND Job = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, role);
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                return true;
            } else {
                conn.rollback();
                throw new Exception("No role '" + role + "' found for worker ID: " + id);
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error deleting role for worker ID " + id + ": " + e.getMessage());
        }
    }

    public List<Integer> readWorkerByBranch(Connection conn, String branch) throws Exception {
        conn.setAutoCommit(false);
        List<Integer> workers = new ArrayList<>();
        String sql = "SELECT id FROM currentWorkers WHERE branch = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branch);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    workers.add(rs.getInt("id"));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error reading worker by branch from DB: " + e.getMessage());
        }
        return workers;
    }

    public boolean isCurr(Connection conn, int workerID) throws Exception {
        String sql = "SELECT id FROM currentWorkers WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, workerID);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error checking if worker is current: " + e.getMessage());
        }
    }

    public boolean fireWorker(Connection conn, int tempID,List<String> firedWorker) throws Exception {
//        if (!isCurr(conn, tempID)) {
//            return false;
//        }
//        conn = DriverManager.getConnection(URL);
        try {
            conn.setAutoCommit(false);
            deleteWorkerById(conn, tempID);
            insertToPrev(conn, Integer.parseInt(firedWorker.get(0)), firedWorker.get(1), firedWorker.get(2),
                    firedWorker.get(3), firedWorker.get(4), firedWorker.get(5),
                    Double.parseDouble(firedWorker.get(6)), firedWorker.get(7));

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error firing worker: " + e.getMessage());
        }
    }

    public boolean insertToPrev(Connection conn, int id, String fn, String ln, String ba, String startDate, String endDate, double hourlySalary, String branch) throws Exception {

        String sql = "INSERT INTO prevWorkers(id, firstName, lastName, bankAccount, startDate, endDate, hourlySalary, branch) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, id);
            pstmt.setString(2, fn);
            pstmt.setString(3, ln);
            pstmt.setString(4, ba);
            pstmt.setString(5, startDate);
            pstmt.setString(6, endDate);
            pstmt.setDouble(7, hourlySalary);
            pstmt.setString(8, branch);
            pstmt.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error inserting worker to previous workers: " + e.getMessage());
        }
    }

    public boolean deletePrevWorker(Connection conn, int id) throws Exception {
        conn.setAutoCommit(false);
        String sql = "DELETE FROM prevWorkers WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int isDeleted = pstmt.executeUpdate();
            conn.commit();
            if (isDeleted > 0) {
                return true;
            } else {
                conn.rollback();
                throw new Exception("No worker found with ID " + id + ".");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error deleting previous worker with ID " + id + ": " + e.getMessage());
        }
    }

    public boolean updateShiftCounterRoles(Connection conn, int type, String date, String branch, int cashier, int manager, int storeKeeper, int deliveryPerson, int driverB, int driverC, int driverD) throws Exception {
        conn.setAutoCommit(false);
        String sql = "UPDATE shifts SET Cashier = ?, Delivery_Person = ?, Manager = ?, Store_Keeper = ?, DriverB = ?, DriverC = ?, DriverD = ? WHERE shiftType = ? and branch = ? and shiftDate = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cashier);
            pstmt.setInt(2, deliveryPerson);
            pstmt.setInt(3, manager);
            pstmt.setInt(4, storeKeeper);
            pstmt.setInt(5, driverB);
            pstmt.setInt(6, driverC);
            pstmt.setInt(7, driverD);
            pstmt.setInt(8, type);
            pstmt.setString(9, branch);
            pstmt.setString(10, date);
            int rowsUpdated = pstmt.executeUpdate();
            conn.commit();
            if (rowsUpdated > 0) {
                return true;
            } else {
                conn.rollback();
                throw new Exception("No shift found with the specified criteria.");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error updating shift details: " + e.getMessage());
        }
    }

    public List<String> readPrevWorkerFromDB(Connection conn, int id) throws Exception {
        conn.setAutoCommit(false);
        List<String> worker = new ArrayList<>();
        String sql = "SELECT * FROM prevWorkers WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    worker.add(String.valueOf(rs.getInt("id")));
                    worker.add(rs.getString("firstName"));
                    worker.add(rs.getString("lastName"));
                    worker.add(rs.getString("bankAccount"));
                    worker.add(rs.getString("startDate"));
                    worker.add(rs.getString("endDate"));
                    worker.add(String.valueOf(rs.getDouble("hourlySalary")));
                    worker.add(rs.getString("branch"));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error reading previous worker from DB: " + e.getMessage());
        }
        return worker;
    }

    public List<String> readAvailability(Connection conn, int workerId, String date) throws Exception {
        conn.setAutoCommit(false);
        List<String> availability = new ArrayList<>();
        String sql = "SELECT * FROM availability WHERE id = ? and startDate = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, workerId);
            pstmt.setString(2, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    availability.add(String.valueOf(rs.getInt("id")));
                    availability.add(String.valueOf(rs.getInt("M1")));
                    availability.add(String.valueOf(rs.getInt("M2")));
                    availability.add(String.valueOf(rs.getInt("M3")));
                    availability.add(String.valueOf(rs.getInt("M4")));
                    availability.add(String.valueOf(rs.getInt("M5")));
                    availability.add(String.valueOf(rs.getInt("M6")));
                    availability.add(String.valueOf(rs.getInt("M7")));
                    availability.add(String.valueOf(rs.getInt("E1")));
                    availability.add(String.valueOf(rs.getInt("E2")));
                    availability.add(String.valueOf(rs.getInt("E3")));
                    availability.add(String.valueOf(rs.getInt("E4")));
                    availability.add(String.valueOf(rs.getInt("E5")));
                    availability.add(String.valueOf(rs.getInt("E6")));
                    availability.add(String.valueOf(rs.getInt("E7")));
                    availability.add(rs.getString("startDate"));
                    availability.add(rs.getString("endDate"));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error reading availability from DB: " + e.getMessage());
        }
        return availability;
    }

    public boolean updateWorkerDetailsManager(Connection conn, int id, String firstName, String lastName, String bankAccount, String startDate, String endDate, double hourlySalary, String branch) throws Exception {
        try {
            conn.setAutoCommit(false);
            updateWorkerDetails(conn, id, firstName, lastName, bankAccount, startDate, endDate, hourlySalary, branch);
            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<List<String>> readassignWorkersperShift(Connection conn, String date, int type, String b) throws Exception {
        List<List<String>> answer = new ArrayList<>();
        List<String> shiftDetails = new ArrayList<>();
        String sqlShift = "SELECT * FROM participatesWorkersinShift WHERE date = ? and type = ? and branch = ?";
        try (PreparedStatement pstmtShift = conn.prepareStatement(sqlShift)) {
            pstmtShift.setString(1, date);
            pstmtShift.setInt(2, type);
            pstmtShift.setString(3, b);
            try (ResultSet rs = pstmtShift.executeQuery()) {
                while (rs.next()) {
                    shiftDetails.add(String.valueOf(rs.getInt("idworker")));
                    shiftDetails.add(String.valueOf(rs.getString("job")));
                    answer.add(shiftDetails);
                    shiftDetails = new ArrayList<>();
                }
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error reading workers per shift from DB: " + e.getMessage());
        }
        return answer;
    }

    public boolean updateSalary(Connection conn, int workerID, double salary) throws Exception {
        conn.setAutoCommit(false);
        String sql = "UPDATE currentWorkers SET Salary = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, salary);
            pstmt.setInt(2, workerID);
            pstmt.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error updating worker details: " + e.getMessage());
        }
    }

    public String readSalary(Connection conn, int tmpID) throws Exception {
        conn.setAutoCommit(false);
        List<String> worker =null;
        String sqlWorker = "SELECT * FROM currentWorkers WHERE id = ?";

        try {
            PreparedStatement pstmtWorker = conn.prepareStatement(sqlWorker);
            pstmtWorker.setInt(1, tmpID);
            try (ResultSet rs = pstmtWorker.executeQuery()) {
                if (rs.next()) {
                    worker = new ArrayList<>();
                    worker.add(String.valueOf(rs.getDouble("Salary")));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error reading worker from DB: " + e.getMessage());
        }
        return worker.get(0);
    }

    public boolean insertTransport(Connection conn,int idS ,String date, String branch, int type, int workerID, String truck) throws Exception {
        String sql = "INSERT INTO Transport(idS, date,branch,type, workerID, truck) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, idS);
            pstmt.setString(2, date);
            pstmt.setString(3, branch);
            pstmt.setInt(4, type);
            pstmt.setInt(5, workerID);
            pstmt.setString(6, truck);
            pstmt.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error inserting transport: " + e.getMessage());
        }
    }

    public List<String> readTransport(Connection conn, int type,String date,String branch) throws Exception {
        conn.setAutoCommit(false);
        List<String> transport =null;
        String sqlWorker = "SELECT * FROM Transport WHERE type = ? and date =? and branch =?";
        try {
            PreparedStatement pstmtWorker = conn.prepareStatement(sqlWorker);
            pstmtWorker.setInt(1, type);
            pstmtWorker.setString(2, branch);
            pstmtWorker.setString(3, date);
            try (ResultSet rs = pstmtWorker.executeQuery()) {
                if (rs.next()) {
                    transport = new ArrayList<>();
                    transport.add(String.valueOf(rs.getInt("idS")));
                    transport.add(rs.getString("date"));
                    transport.add(rs.getString("branch"));
                    transport.add(rs.getString("type"));
                    transport.add(String.valueOf(rs.getInt("workerID")));
                    transport.add(rs.getString("truck"));
                }
            }
            catch (Exception e){
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error reading transport from DB: " + e.getMessage());
        }

        return transport;
    }

    public boolean deleteParticipating(Connection conn, String date, int type, String b) throws Exception {
        conn.setAutoCommit(false);
        String sql = "DELETE FROM participatesWorkersinShift WHERE date = ? and type = ? and branch = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setInt(2, type);
            pstmt.setString(3, b);
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            if (affectedRows > 0) {
                return true;
            } else {
                conn.rollback();
                throw new Exception("No worker partctipating found within shift" );
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error deleting worker in shift: " + e.getMessage());
        }
    }

    public boolean deleteTransport(Connection conn, int type,String date, String branch)throws Exception{

        conn.setAutoCommit(false);
        String sql = "DELETE FROM Transport WHERE date = ? and branch = ? and type = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, type);
            pstmt.setString(2,branch);
            pstmt.setString(3, date);
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            if (affectedRows > 0) {
                return true;
            } else {
                conn.rollback();
                throw new Exception("No transport found");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error deleting transport: " + e.getMessage());
        }
    }

    public boolean deleteAvail(Connection conn, int workerID, String date) throws Exception {
        conn.setAutoCommit(false);
        String sql = "DELETE FROM availability WHERE id = ? and startDate = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, workerID);
            pstmt.setString(2,date);
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            if (affectedRows > 0) {
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Error deleting availability: " + e.getMessage());
        }
    }

    public int IDcounter(Connection conn) throws Exception {
        conn.setAutoCommit(false);
        String data = null;
        String columnName = "generalCounterGeneration";
        String sql = "SELECT " + columnName + " FROM configurations";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                data = resultSet.getString(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
            throw e;
        }
        conn.commit();
        return Integer.valueOf(data);
    }

    public boolean saveCounter(Connection conn, int counter) throws Exception {
        conn.setAutoCommit(false);
        String sql = "UPDATE configurations SET generalCounterGeneration= ? ";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, counter);
            pstmt.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error updating Counter details: " + e.getMessage());
        }
    }
}


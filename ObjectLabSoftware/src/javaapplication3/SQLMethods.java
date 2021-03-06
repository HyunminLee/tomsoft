package javaapplication3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Database
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!
public class SQLMethods {

    String url;
    String dbName = "jobsdb";
    String driver = "com.mysql.jdbc.Driver";

    //String url = "jdbc:mysql://10.37.8.172/";
    //String url = "jdbc:mysql://cosc412.ddns.net:3306/";
    String userName = "admin";
    String password = "password";
    InstanceCall inst;
    Connection conn = null;
    private static Statement st;
    private ResultSet res;
    private PreparedStatement stmt;

    public SQLMethods() { //Only Constructor should open and close connections
        inst = new InstanceCall();
        NsLookup look = new NsLookup();
        File f = new File("C:\\Sync\\computername.txt");
        String ip = "";
        String line = "";
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(f.getAbsolutePath());

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                ip = line;
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file!");
            String temp = JOptionPane.showInputDialog("Please enter the exact name of the computer running Magics:");
            ip = look.resolve(temp);
            //create and write to file
            PrintWriter writer;
            try {
                writer = new PrintWriter("C:\\Sync\\computername.txt", "UTF-8");
                writer.println(temp);
                writer.close();
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (UnsupportedEncodingException ex1) {
                Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            System.out.println("Couldn't read file! IOException!");
        }

        url = "jdbc:mysql://" + ip + ":3306/";

        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
            st = conn.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.print("Driver class not found / created. Exception!");
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

//doesn't work
    public ResultSet selectAllFromTable(String table) {
        res = null;
        try {
            stmt = this.conn.prepareStatement("SELECT * FROM ?");
            stmt.setString(1, table);
            res = stmt.executeQuery();
            System.out.println(stmt);
        } catch (SQLException e) {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet selectAllFromPending() {
        res = null;
        try {
            stmt = this.conn.prepareStatement("SELECT * FROM pendingJobs");
            res = stmt.executeQuery();
            System.out.println(stmt);
        } catch (SQLException e) {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet selectIncompleteFromZCorp() {
        res = null;
        try {
            stmt = this.conn.prepareStatement("SELECT * FROM zcorp WHERE status = 'incomplete'");
            res = stmt.executeQuery();
            System.out.println(stmt);
        } catch (SQLException e) {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet selectIncompleteFromObjet() {
        res = null;
        try {
            stmt = this.conn.prepareStatement("SELECT * FROM objet WHERE status = 'incomplete'");
            res = stmt.executeQuery();
            System.out.println(stmt);
        } catch (SQLException e) {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet selectIncompleteFromSolidscape() {
        res = null;
        try {
            stmt = this.conn.prepareStatement("SELECT * FROM solidscape WHERE status = 'incomplete'");
            res = stmt.executeQuery();
            System.out.println(stmt);
        } catch (SQLException e) {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet searchPrinterSettings(String printer) {
        res = null;
        try {
            stmt = this.conn.prepareStatement("SELECT * FROM printers WHERE printer = ?");
            stmt.setString(1, printer);
            res = stmt.executeQuery();
            System.out.println(stmt);
        } catch (SQLException e) {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public void updatePrinterSettings(String printer, String Type1, String Unit1, double Cost1, String Type2, String Unit2, double Cost2,
            String Type3, String Unit3, double Cost3, String Type4, String Unit4, double Cost4, String Type5, String Unit5, double Cost5) {
        try {
            stmt = this.conn.prepareStatement(
                    "UPDATE printers "
                    + "SET "
                    + "materialType = ?, "
                    + "materialUnit = ?, "
                    + "materialCostPerUnit = ?, "
                    + "materialType2 = ?, "
                    + "materialUnit2 = ?, "
                    + "materialCostPerUnit2 = ?, "
                    + "materialType3 = ?, "
                    + "materialUnit3 = ?, "
                    + "materialCostPerUnit3 = ?, "
                    + "materialType4 = ?, "
                    + "materialUnit4 = ?, "
                    + "materialCostPerUnit4 = ?,"
                    + "materialType5 = ?, "
                    + "materialUnit5= ?, "
                    + "materialCostPerUnit5 = ?"
                    + "WHERE printer = ?");

            stmt.setString(1, Type1);
            stmt.setString(2, Unit1);
            stmt.setDouble(3, Cost1);
            stmt.setString(4, Type2);
            stmt.setString(5, Unit2);
            stmt.setDouble(6, Cost2);
            stmt.setString(7, Type3);
            stmt.setString(8, Unit3);
            stmt.setDouble(9, Cost3);
            stmt.setString(10, Type4);
            stmt.setString(11, Unit4);
            stmt.setDouble(12, Cost4);
            stmt.setString(13, Type5);
            stmt.setString(14, Unit5);
            stmt.setDouble(15, Cost5);
            stmt.setString(16, printer);
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertPrinterSettings(String printer, String Type1, String Unit1, float Cost1, String Type2, String Unit2, float Cost2,
            String Type3, String Unit3, float Cost3, String Type4, String Unit4, float Cost4, String Type5, String Unit5, float Cost5) {
        try {
            stmt = this.conn.prepareStatement(
                    "INSERT INTO printers "
                    + "(printer, "
                    + "materialType, "
                    + "materialUnit, "
                    + "materialCostPerUnit, "
                    + "materialType2, "
                    + "materialUnit2, "
                    + "materialCostPerUnit2, "
                    + "materialType3, "
                    + "materialUnit3, "
                    + "materialCostPerUnit3, "
                    + "materialType4, "
                    + "materialUnit4, "
                    + "materialCostPerUnit4)"
                    + "VALUES ('" + printer + "', '" + Type1 + "', '" + Unit1 + "', '" + Cost1 + "', '" + Type2 + "', '" + Unit2 + "', "
                    + "'" + Cost2 + "', '" + Type3 + "', '" + Unit3 + "', '" + Cost3 + "', '" + Type4 + "', '" + Unit4 + "', '" + Cost4 + "')");

            stmt.executeUpdate();
            System.out.println("Successfully inserted value");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet searchPending() {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM pendingjobs "
                    + "WHERE "
                    + "status = 'pending'");
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchSolidscapeByBuildName(String buildName) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT idSolidscape "
                    + "FROM solidscape "
                    + "WHERE "
                    + "buildName = ?");
            stmt.setString(1, buildName);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchObjetByBuildName(String buildName) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT idObjet "
                    + "FROM objet "
                    + "WHERE "
                    + "buildName = ?");
            stmt.setString(1, buildName);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchZCorpByBuildName(String buildName) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT idZcorp "
                    + "FROM zcorp "
                    + "WHERE "
                    + "buildName = ?");
            stmt.setString(1, buildName);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchCompleted(String query, String column) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM completedJobs "
                    + "WHERE "
                    + column + " LIKE '%" + query + "%'");
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchPrintersByBuildName(String buildName) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM printers "
                    + "WHERE "
                    + "buildName = ?");
            stmt.setString(1, buildName);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchPendingWithID(String ID) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM pendingjobs "
                    + "WHERE "
                    + "status = 'pending' "
                    + "AND idJobs = ?");
            stmt.setString(1, ID);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchIncompletePendingByBuild(String buildName) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM pendingjobs "
                    + "WHERE "
                    + "status = 'incomplete' "
                    + "AND buildName = ?");
            stmt.setString(1, buildName);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchPendingByBuildName(String buildName) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM pendingjobs "
                    + "WHERE "
                    + "buildName = '" + buildName + "'");
            //stmt.setString(1, buildName);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchID(String table, String firstName, String lastName, String fileName, String dateStarted) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT idJobs "
                    + "FROM " + table + " "
                    + "WHERE "
                    + "firstName = ? "
                    + "AND lastName = ? "
                    + "AND fileName = ? "
                    + "AND dateStarted = ?");
            //stmt.setString(, table);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, fileName);
            stmt.setString(4, dateStarted);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchZcorpByID(String id) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM zcorp "
                    + "WHERE "
                    + "AND idJobs = ?");
            //stmt.setString(, table);
            stmt.setString(1, id);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchObjetByID(String id) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM objet "
                    + "WHERE "
                    + "AND idJobs = ?");
            stmt.setString(1, id);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchSolidscapeByID(String id) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM solidscape "
                    + "WHERE "
                    + "AND idJobs = ?");
            stmt.setString(1, id);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchPendingWithoutName(String table, String fileName, String dateStarted) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT idJobs "
                    + "FROM " + table + " "
                    + "WHERE "
                    + "AND fileName = ? "
                    + "AND dateStarted = ?");
            //stmt.setString(, table);
            stmt.setString(3, fileName);
            stmt.setString(4, dateStarted);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchTwo(String table, String column1, String column2, String key1) {
        res = null;
        String query = "SELECT * FROM " + table + " WHERE concat(" + column1 + ", ' ', " + column2 + ")" + "LIKE ?";
        try {
            stmt = this.conn.prepareStatement(query); //fixed
            stmt.setString(1, key1);
            //System.out.println(query);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchOne(String table, String column1, String key) {
        res = null;
        String query = "SELECT * FROM " + table + " WHERE " + column1 + " = ?";
        try {
            stmt = this.conn.prepareStatement(query); //fixed
            stmt.setString(1, key);
            res = stmt.executeQuery();
            System.out.println(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void delete(String table, String id) throws SQLException {
        stmt = this.conn.prepareStatement(
                "DELETE "
                + "FROM " + table + " "
                + "WHERE idJobs = ?");
        // stmt.setString(1, table);
        stmt.setString(1, id);
        System.out.println(stmt);
        stmt.executeUpdate();
    }

    public void deleteByBuildName(String buildName, String table) throws SQLException {
        stmt = this.conn.prepareStatement(
                "DELETE "
                + "FROM " + table
                + " WHERE buildName = ?");
        stmt.setString(1, buildName);
        System.out.println(stmt);
        stmt.executeUpdate();
    }

    public void approve(String id) throws SQLException {
        stmt = this.conn.prepareStatement(
                "UPDATE pendingjobs "
                + "SET status = 'approved', "
                + "dateUpdated = now() "
                + "WHERE idJobs = ?");
        stmt.setString(1, id);
        System.out.println(stmt);
        stmt.executeUpdate();
    }

    public void updatePendingJobFLocation(String idJob, String fLocation) {
        try {
            stmt = this.conn.prepareStatement(
                    "UPDATE pendingjobs "
                    + "SET fileLocation = " + fLocation + " "
                    + "WHERE idJobs = '" + idJob + "'");
            System.out.println(stmt);

        } catch (SQLException ex) {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateToPrint(String idJob) throws SQLException {
        stmt = this.conn.prepareStatement(
                "UPDATE pendingjobs "
                + "SET status = 'to print' "
                + "WHERE idJobs = '?';");
        stmt.setString(1, idJob);
        System.out.println(stmt);
    }

    public ResultSet searchApprovedZcorp() {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM pendingjobs "
                    + "WHERE "
                    + "printer = 'ZCorp' "
                    + "AND buildName is null "
                    + "AND status = 'approved'");
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchApprovedSolidscape() {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM pendingjobs "
                    + "WHERE "
                    + "printer = 'Solidscape' "
                    + "AND status = 'approved'");
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchApprovedObjet() {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM pendingjobs "
                    + "WHERE "
                    + "printer = 'Objet' "
                    + "AND status = 'approved'");
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void updatePendingJobVolume(String idJob, double volume) {
        try {
            stmt = this.conn.prepareStatement(
                    "UPDATE pendingjobs "
                    + "SET volume = " + volume + " "
                    + "WHERE idJobs = '" + idJob + "'");
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePendingJobByCost(String idJob, double cost) {
        try {
            stmt = this.conn.prepareStatement(
                    "UPDATE pendingjobs "
                    + "SET cost = " + cost + " "
                    + "WHERE idJobs = '" + idJob + "'");
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void runQuery(String query) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(query);
            stmt.executeUpdate();
            //stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
       // return res;
    }

    public void updatePendingJobsBuildName(String build, String fileName) {
        try {
            stmt = this.conn.prepareStatement(
                    "UPDATE pendingjobs "
                    + "SET buildName = ? WHERE filename = ?");
            stmt.setString(1, build);
            stmt.setString(2, fileName);
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertIntoPendingJobs(String printer, String firstName, String lastName, String Class, String section, String fileName, String filePath, String email) {
        try {
            stmt = this.conn.prepareStatement("INSERT INTO pendingjobs "
                    + "(idJobs, "
                    + "printer, "
                    + "firstName, "
                    + "lastName, "
                    + "course, "
                    + "section, "
                    + "fileName, "
                    + "filePath, "
                    + "status, "
                    + "email) "
                    + "VALUES"
                    + "(DATE_FORMAT(NOW(), '%Y-%m-%d_" + lastName + "_" + firstName + "_" + fileName + "'), '"
                    + printer + "', '"
                    + firstName + "', '"
                    + lastName + "', '"
                    + Class + "', '"
                    + section + "', '"
                    + fileName.replace("'", "\'") + "', '"
                    + filePath.replace("'", "\'") + "', '"
                    + "pending', '"
                    + email + "')");

            stmt.executeUpdate();
            System.out.println(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertIntoSolidscape(String bn, int models, double resolution, String BuildTime, String comment, double cost) {
        String date = "(CURTIME())";
        try {
            stmt = this.conn.prepareStatement(
                    "INSERT "
                    + "INTO solidscape "
                    + "(idSolidscape, "
                    + "buildName, "
                    + "dateRun, "
                    + "noModels, "
                    + "resolution, "
                    + "runTime, "
                    + "comments, "
                    + "costOfBuild) "
                    + "VALUES"
                    + "(DATE_FORMAT(NOW(), '%Y-%m-%d_%H-%i-%s_" + bn + "'), '"
                    + "" + bn + "', " + date + ", '" + models + "', '" + resolution + "', '" + BuildTime + "', '" + comment + "', '" + cost + "')");
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertIntoZcorp(String bn, double mb, double yb, double mab, double cb, double ci, int models, String comment, double cost, String status) {
        String id = "DATE_FORMAT(NOW(), '%Y-%m-%d_%H-%i-%s_" + bn + "')";
        String date = "(CURTIME())";
        try {
            stmt = this.conn.prepareStatement(
                    "INSERT "
                    + "INTO zcorp "
                    + "(idZcorp, "
                    + "buildName, "
                    + "dateRun, "
                    + "monoBinder, "
                    + "yellowBinder, "
                    + "magentaBinder, "
                    + "cyanBinder, "
                    + "cubicInches, "
                    + "noModels, "
                    + "comments, "
                    + "costOfBuild, "
                    + "status) "
                    + "VALUES (" + id + ", '" + bn + "'," + date + ", '" + mb + "', '" + yb + "', '" + mab + "', '" + cb + "', '" + ci + "', '" + models + "', '" + comment + "','" + cost + "','" + status + "')");
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertIntoObjet(String bn, double bc, double sc, int models, String bm, double resolution, String comment, double cost) {
        String date = "(CURTIME())";
        try {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url + dbName, userName, password);
            System.out.println("Success!");
            Statement st = conn.createStatement();
            int val = st.executeUpdate("INSERT INTO objet "
                    + "(idObjet, "
                    + "buildName, "
                    + "dateRun, "
                    + "buildConsumed, "
                    + "supportConsumed, "
                    + "noModels, "
                    + "buildMaterials, "
                    + "resolution, "
                    + "comments, "
                    + "costOfBuild) "
                    + "VALUES(DATE_FORMAT(NOW(), '%Y-%m-%d_%H-%i-%s_" + bn + "'),"
                    + "'" + bn + "'," + date + ",'" + bc + "','" + sc + "','" + models + "','" + bm + "','" + resolution + "','" + comment + "','" + cost + "');");
            if (val == 1) {
                System.out.print("Successfully inserted value");
            }
            System.out.println(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertIntoClasses(String course, String second) {
        try {
            stmt = this.conn.prepareStatement(
                    "INSERT INTO classes (className, classSection, current) VALUES('" + course + "','" + second + "', false)");
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAllClassesInvisible() {
        try {
            stmt = this.conn.prepareStatement("UPDATE classes SET current = false");
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentClasses(String course, String section) {
        try {
            stmt = this.conn.prepareStatement(
                    "UPDATE classes "
                    + "SET current = true WHERE className = ? and classSection = ?");
            stmt.setString(1, course);
            stmt.setString(2, section);
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    //completed
    public void insertIntoCompletedJobs(String id, String printer, String firstName, String lastName, String course, String section, String fileName, String filePath,
            String dateStarted, String status, String email, String comment, String buildName, Double volume, Double cost) {
        try {
            stmt = this.conn.prepareStatement("INSERT INTO completedJobs "
                    + "(idCompJobs, "
                    + "printer, "
                    + "firstName, "
                    + "lastName, "
                    + "course, "
                    + "section, "
                    + "fileName, "
                    + "filePath, "
                    + "dateStarted, "
                    + "dateCompleted, "
                    + "status, "
                    + "email, "
                    + "comment, "
                    + "buildName, "
                    + "volume, "
                    + "cost) "
                    + "VALUES"
                    + "('" + id + "', "
                    + "'" + printer + "', "
                    + "'" + firstName + "', '"
                    + "" + lastName + "', "
                    + "'" + course + "', "
                    + "'" + section + "', "
                    + "'" + fileName + "', "
                    + "'" + filePath + "', "
                    + "'" + dateStarted + "', "
                    + "(CURDATE()), "
                    + "'pending', "
                    + "'" + email + "', "
                    + "'" + comment + "', "
                    + "'" + buildName + "', "
                    + "'" + volume + "', "
                    + "'" + cost + "')");
            stmt.executeUpdate();
            System.out.println(stmt);
            System.out.println("Complete Record Entered!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getClasses() {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM classes "
                    + "WHERE "
                    + "current = false");
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet getCurrentClasses() {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM classes "
                    + "WHERE "
                    + "current = true");
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}

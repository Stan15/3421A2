package com.eecs3421;

/*
 Connection Parameters:
 IP: 127.0.0.1,
 Port: 3306,
 User: root,
 Password: root1234
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class A02MiddleTier {
	//This class will contain your code for interacting with Database, acquire the query result and display it in the GUI text area.

    String userName = "root";
    String password = "Root1234";
    String serverName = "127.0.0.1";
    String portNumber = "3306";

    /*
    Connection to MySQL database
    Source code from https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
     */
    public Connection getConnection() throws SQLException {

        Connection conn;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        conn = DriverManager.getConnection(
                "jdbc:" + "mysql" + "://" +
                        this.serverName +
                        ":" + this.portNumber + "/"+"a02schema",
                connectionProps);

        System.out.println("Connected to database");
        return conn;
    }

    public String runQuery(String query) {
        String result = "";
        Connection conn;
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            result += "Event Name | Event Weblink | CFP Text\n";
            while (rs.next()) {
                result += rs.getString(2) + " | " + rs.getString(3) + " | "+ rs.getString(4) + "\n";
            }
            conn.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String selectionCheck(boolean conf, boolean jour, boolean book, boolean all , boolean period, String from, String to) {

        String dateRange = null;
        // where Exists (Select ActivityDate From ActivityHappens where EventID = ID AND ActivityDate BETWEEN '2020-01-01' AND '2020-12-31')
        if (!from.isBlank() && !to.isBlank()) {
            dateRange = String.format(" ActivityDate BETWEEN '%s' AND '%s' ", from, to);
        } else if (from.isBlank() && !to.isBlank()) {
            dateRange = String.format(" ActivityDate <= '%s' ", to);
        } else if (to.isBlank() && !from.isBlank()) {
            dateRange = String.format(" ActivityDate >= '%s' ", from);
        }
        String dateConstraint = dateRange==null ? "" : String.format("EXISTS (SELECT ActivityDate FROM ActivityHappens WHERE EventID = ID AND %s)", dateRange);

        // AND (ID IN (Select EventID from EventConference) OR ID IN (Select EventID from EventJournal) OR ID IN (Select EventID From EventBook));
        ArrayList<String> eventTypeConstraints = new ArrayList<>();
        if (conf) eventTypeConstraints.add("ID IN (Select EventID from EventConference)");
        if (jour) eventTypeConstraints.add("ID IN (Select EventID from EventJournal)");
        if (book) eventTypeConstraints.add("ID IN (Select EventID from EventBook)");

        String eventConstraint = "";
        for (String s : eventTypeConstraints) {
            if (!eventConstraint.isBlank()) eventConstraint += " OR ";
            eventConstraint += s;
        }
        if (!eventConstraint.isBlank()) eventConstraint = "("+eventConstraint+")";

        String constraint = dateConstraint;
        if (!dateConstraint.isBlank() && !eventConstraint.isBlank()) constraint += " AND ";
        constraint += eventConstraint;

        if (eventConstraint.isBlank()) return "";
        return String.format("SELECT * FROM EVENT WHERE %s", constraint);
    }

}

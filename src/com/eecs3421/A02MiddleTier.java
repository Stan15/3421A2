package com.eecs3421;

/*
 Connection Parameters:
 IP: 127.0.0.1,
 Port: 3306,
 User: root,
 Password: root1234
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class A02MiddleTier {
	//This class will contain your code for interacting with Database, acquire the query result and display it in the GUI text area.

    String userName = "root";
    String password = "root1234";
    String serverName = "127.0.0.1";
    String portNumber = "3306";

    //query library

    String q_conf_all = "SELECT E.Name, C.City, C.Country, C.EvDate, E.EventWebLink, E.CFPText " +
                        "FROM Event as E, EventConference as C " +
                        "WHERE E.ID = C.EventID;";

    String q_conf_period = "";

    String q_journal_all = "SELECT E.Name, J.JournalName, J.Publisher, E.EventWebLink, E.CFPText " +
                           "FROM Event as E, EventJournal as J " +
                           "WHERE E.ID = J.EventID;";

    String q_journal_period = "";

    String q_book_all = "SELECT E.Name, B.Publisher, E.EventWebLink, E.CFPText " +
                        "FROM Event as E, EventBook as B " +
                        "WHERE E.ID = B.EventID;";

    String q_book_period = "";

    String q_conf_journal_all = "SELECT Name, EventWebLink, CFPText " +
                                "FROM Event as E RIGHT JOIN EventConference as C ON E.ID = C.EventID " +
                                "UNION " +
                                "SELECT Name, EventWeblink, CFPText " +
                                "FROM Event as E RIGHT JOIN EventJournal as J on E.ID = J.EventID;";

    String q_conf_journal_period = "";

    String q_conf_book_all = "SELECT Name, EventWebLink, CFPText " +
                             "FROM Event AS E RIGHT JOIN EventConference AS C ON E.ID = C.EventID " +
                             "UNION SELECT Name, EventWeblink, CFPText " +
                             "FROM Event AS E RIGHT JOIN EventBook AS B on E.ID = B.EventID;";

    String q_conf_book_period = "";

    String q_book_journal_all = "SELECT Name, EventWebLink, CFPText " +
                                "FROM Event as E RIGHT JOIN EventJournal as J ON E.ID = J.EventID " +
                                "UNION " +
                                "SELECT Name, EventWeblink, CFPText " +
                                "FROM Event as E RIGHT JOIN EventBook as B on E.ID = B.EventID; ";

    String q_book_journal_period = "";

    String q_event_all = "SELECT Name, EventWebLink, CFPText " +
                         "From Event;";

    String q_event_period = "";


    /*
    Connection to MySQL database
    Source code from https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
     */
    public Connection getConnection() throws SQLException {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        conn = DriverManager.getConnection(
                "jdbc:" + "mysql" + "://" +
                        this.serverName +
                        ":" + this.portNumber + "/",
                connectionProps);

        System.out.println("Connected to database");
        return conn;
    }

    private String selectionCheck(boolean conf, boolean jour, boolean book, boolean all , boolean period, String from, String to) {

        String outputQuery = "";

        //all three boxes checked and no period specified
        if (conf && jour && book && all) {
            outputQuery = q_event_all;
        }

        //conf only no period
        else if (conf && !jour && !book && all) {
            outputQuery = q_conf_all;
        }

        //conf only with period
        else if (conf && !jour && !book && period) {
            outputQuery = "SELECT E.Name, C.City, C.Country, C.EvDate, E.EventWebLink, E.CFPText " +
                          "FROM Event as E, EventConference as C " +
                          "WHERE E.ID = C.EventID, EvDate BETWEEN " + from + " AND " + to + " ;";
        }

        else if (conf && !jour && !book && all) {
            outputQuery = q_conf_all;
        }

        else if (conf && !jour && !book && all) {
            outputQuery = q_conf_all;
        }

        return outputQuery;

    }

}

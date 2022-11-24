package io.github.oakdh.prometheus;

import java.sql.*;

public class DatabaseHandler {
    public static Connection database_connection;

    public static boolean connectDatabase()
    {
        try 
        {
            Class.forName("org.sqlite.JDBC");
            Class.forName("java.sql.Driver");
            database_connection = DriverManager.getConnection("jdbc:sqlite:./database.db");

            Statement statement = database_connection.createStatement();

            statement.execute("CREATE TABLE \"USER_LOGIN\" (" +
                "\"id\"            UNSIGNED BIG INT   NOT NULL," +
                "\"username\"      TEXT               NOT NULL," +
                "\"password\"      TEXT               NOT NULL," +
                "\"email\"         TEXT               NOT NULL" +
            ");");

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    

    //TODO: put user data, put user login, put measurements  
}

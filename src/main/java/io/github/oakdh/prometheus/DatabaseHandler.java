package io.github.oakdh.prometheus;

import java.sql.*;

public class DatabaseHandler {
    public static Connection database_connection;

    /**
     * Creates a connection between the server and database. Will create the database if it does not exist.
     * 
     * @return Whether the connection made or not.
     */
    public static boolean connectDatabase()
    {
        try 
        {
            Class.forName("org.sqlite.JDBC");
            Class.forName("java.sql.Driver");
            database_connection = DriverManager.getConnection("jdbc:sqlite:./database.db");

            Statement statement = database_connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS \"USER_LOGIN\" (" +
                "\"id\"            BIG INT            NOT NULL," +
                "\"username\"      TEXT               NOT NULL," +
                "\"password\"      TEXT               NOT NULL," +
                "\"email\"         TEXT               NOT NULL" +
            ");");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Saves user login information in the database.
     * 
     * @param userLogin Login information.
     * @return Whether the save was successful or not.
     */
    public static boolean saveUserLogin(UserLogin userLogin)
    {
        String sql = "INSERT INTO USER_LOGIN(id, username, password, email) VALUES(?, ?, ?, ?)";

        try
        {
            // Create statement
            PreparedStatement pstatement = database_connection.prepareStatement(sql);

            // Replace placeholders (questionmarks in sql command). Index starts at 1.
            pstatement.setLong(1, userLogin.id());
            pstatement.setString(2, userLogin.username());
            pstatement.setString(3, userLogin.password());
            pstatement.setString(4, userLogin.email());

            pstatement.executeUpdate();
        }
        catch (Exception e) 
        { 
            e.printStackTrace(); 
            return false;
        }

        return true;
    }

    //TODO: put user data, put user login, put measurements  
}

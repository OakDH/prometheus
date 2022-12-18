package io.github.oakdh.prometheus;

import java.sql.*;

public class DatabaseHandler {
    public static Connection database_connection;

    /**
     * Creates a connection between the server and database. Will create the
     * database if it does not exist.
     * 
     * @return Whether the connection made or not.
     */
    public static boolean connectDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            Class.forName("java.sql.Driver");
            database_connection = DriverManager.getConnection("jdbc:sqlite:./database.db");

            Statement statement = database_connection.createStatement();

            /* USER-LOGIN */

            statement.execute("CREATE TABLE IF NOT EXISTS \"USER_LOGIN\" (" +
                    "\"id\"            BIG INT            NOT NULL," +
                    "\"username\"      TEXT               NOT NULL," +
                    "\"password\"      TEXT               NOT NULL," +
                    "\"email\"         TEXT               NOT NULL" +
                    ");");

            /* USER-DATA */

            statement.execute("CREATE TABLE IF NOT EXISTS \"USER_DATA\" (" +
                    "\"id\"                     UNSIGNED BIG INT            DEFAULT 0," +
                    "\"correct_reports\"        UNSIGNED INTEGER            DEFAULT 0," +
                    "\"incorrect_reports\"      UNSIGNED INTEGER            DEFAULT 0," +
                    "\"points\"                 UNSIGNED INTEGER            DEFAULT 0" +
                    ");");

            /* Measurements */

            statement.execute("CREATE TABLE IF NOT EXISTS \"MEASUREMENTS\" (" +
                    "\"id\"                 UNSIGNED BIG INT                   NOT NULL," +
                    "\"humidity\"           NUMERIC                            NOT NULL," +
                    "\"moisture\"           NUMERIC                            NOT NULL," +
                    "\"time\"               UNSIGNED BIG INT                   NOT NULL" +
                    ");");

        } catch (Exception e) {
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
    public static boolean saveUserLogin(UserLogin userLogin) {
        String sql = "INSERT INTO USER_LOGIN(id, username, password, email) VALUES(?, ?, ?, ?)";

        try {
            // Create statement
            PreparedStatement pstatement = database_connection.prepareStatement(sql);

            // Replace placeholders (questionmarks in sql command). Index starts at 1.
            pstatement.setLong(1, userLogin.id());
            pstatement.setString(2, userLogin.username());
            pstatement.setString(3, userLogin.password());
            pstatement.setString(4, userLogin.email());

            pstatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean saveUserData(int id, int correct_reports, int incorrect_reports, int points) {
        String sql = "INSERT INTO MEASUREMENTS(id, humidity, moisture, time) VALUES(?, ?, ?, ?)";

        try {
            // Create statement
            PreparedStatement pstatement = database_connection.prepareStatement(sql);

            // Replace placeholders (questionmarks in sql command). Index starts at 1.
            pstatement.setLong(1, id);
            pstatement.setInt(2, correct_reports);
            pstatement.setInt(3, incorrect_reports);
            pstatement.setInt(4, points);

            pstatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean saveMeasurements(int id, int humidity, int moisture, int time) {
        String sql = "INSERT INTO MEASUREMENTS(id, humidity, moisture, time) VALUES(?, ?, ?, ?)";

        try {
            // Create statement
            PreparedStatement pstatement = database_connection.prepareStatement(sql);

            // Replace placeholders (questionmarks in sql command). Index starts at 1.
            pstatement.setLong(1, id);
            pstatement.setInt(2, humidity);
            pstatement.setInt(3, moisture);
            pstatement.setInt(4, time);

            pstatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Gets the user login information from the database
     * 
     * @param id The id of the user to get
     * @return Returns a {@link io.github.oakdh.prometheus.UserLogin} filled with
     *         the users data if the user was found.
     *         Returns a {@link io.github.oakdh.prometheus.UserLogin#EMPTY} if the
     *         user wasn't found.
     */
    public static UserLogin getUserLogin(long id) {
        String sql = "SELECT username, password, email FROM USER_LOGIN WHERE id = ?";

        try {
            // Create statement
            PreparedStatement pstatement = database_connection.prepareStatement(sql);

            // Set id in statement
            pstatement.setLong(1, id);

            // Execute statement and save the results
            ResultSet rs = pstatement.executeQuery();

            // Return userlogin data collected from the result
            return new UserLogin(id, rs.getString("username"), rs.getString("password"), rs.getString("email"));
        } catch (Exception e) {
            e.printStackTrace();
            return UserLogin.EMPTY;
        }
    }

    // TODO: put user data, put user login, put measurements
}

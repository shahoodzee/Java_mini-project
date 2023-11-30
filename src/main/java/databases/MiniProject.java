package databases;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * Template Java code for the Mini Project assignment. ONLY MODIFY THE CODE WITHIN THE TODO/end TODO
 * BLOCKS! The automated marking system relies on the structure of the code remaining the same.
 */
public class MiniProject {

  /**
   * Execute the first query.
   * 
   * @param connection a database connection
   * @return the results of the query
   * @throws SQLException if a problem occurs when executing the query
   */
	public static Map<String, Integer> firstQuery(Connection connection) throws SQLException {

	    System.out.println("################## 1st Query ###############");

	    Map<String, Integer> results = new LinkedHashMap<>();

	    try (PreparedStatement statement = connection.prepareStatement(
	            "SELECT uniqueCarrier, COUNT(*) as delayCount " +
	                    "FROM delayedFlights " +
	                    "WHERE arrDelay > 0 OR depDelay > 0 " +
	                    "GROUP BY uniqueCarrier " +
	                    "ORDER BY delayCount ASC " +
	                    "LIMIT 5");
	         ResultSet resultSet = statement.executeQuery()
	    ) {
	        while (resultSet.next()) {
	            String uniqueCarrier = resultSet.getString("uniqueCarrier");
	            int delayCount = resultSet.getInt("delayCount");
	            results.put(uniqueCarrier, delayCount);
	        }
	    }

	    return results;
	}


  /**
   * Execute the second query.
   * 
   * @param connection a database connection
   * @return the results of the query
   * @throws SQLException if a problem occurs when executing the query
   */
	public static Map<String, Integer> secondQuery(Connection connection) throws SQLException {

	    System.out.println("################## 2nd Query ###############");

	    Map<String, Integer> results = new LinkedHashMap<>();

	    try (PreparedStatement statement = connection.prepareStatement(
                "SELECT a.state, COUNT(*) as total_delays " +
                        "FROM delayedFlights d " +
                        "JOIN airport a ON d.dest = a.airportCode " +
                        "GROUP BY a.state " +
                        "ORDER BY total_delays DESC " +
                        "LIMIT 5");
	    		ResultSet resultSet = statement.executeQuery()
	    ){
	        while (resultSet.next()) {
	            String state = resultSet.getString("state");
	            int totalDelays = resultSet.getInt("total_delays");
	            results.put(state, totalDelays);
	        }
	    }

	    return results;
	}

    /**
     * Execute the third query.
     *
     * @param connection a database connection
     * @return the results of the query
     * @throws SQLException if a problem occurs when executing the query
     */
    public static Map<String, Integer> thirdQuery(Connection connection) throws SQLException {

        System.out.println("################## 3rd Query ###############");

        Map<String, Integer> results = new LinkedHashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT airportName, SUM(depDelay) as totalDelay " +
                        "FROM delayedFlights " +
                        "JOIN airport ON delayedFlights.origin = airport.airportCode " +
                        "WHERE month = 5 " +
                        "GROUP BY airportName " +
                        "ORDER BY totalDelay DESC " +
                        "LIMIT 3");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String airportName = resultSet.getString("airportName");
                int totalDelay = resultSet.getInt("totalDelay");
                results.put(airportName, totalDelay);
            }
        }

        return results;
    }

    /**
     * Execute the fourth query.
     *
     * @param connection a database connection
     * @return the results of the query
     * @throws SQLException if a problem occurs when executing the query
     */
    public static Map<String, Integer> fourthQuery(Connection connection) throws SQLException {
        System.out.println("################## 4th Query ###############");

        Map<String, Integer> results = new LinkedHashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT a.airportName, df.depDelay " +
                        "FROM delayedFlights df " +
                        "JOIN airport a ON df.origin = a.airportCode " +
                        "WHERE df.distance > 1500 AND df.depDelay > 240 " +
                        "ORDER BY df.depDelay DESC");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                String airportName = resultSet.getString("airportName");
                int depDelay = resultSet.getInt("depDelay");

                results.put(airportName, depDelay);
            }
        }

        return results;
    }


  /**
   * Execute the fifth query.
   * 
   * @param connection a database connection
   * @return the results of the query
   * @throws SQLException if a problem occurs when executing the query
   */
    public static Map<String, Integer> fifthQuery(Connection connection) throws SQLException {

        System.out.println("################## 5th Query ###############");

        Map<String, Integer> results = new LinkedHashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT origin.state AS state, COUNT(*) AS delayCount " +
                        "FROM delayedFlights " +
                        "JOIN airport AS origin ON delayedFlights.origin = origin.airportCode " +
                        "JOIN airport AS dest ON delayedFlights.dest = dest.airportCode " +
                        "WHERE origin.state = dest.state AND delayedFlights.depDelay > 120 " +
                        "GROUP BY origin.state " +
                        "ORDER BY delayCount DESC " +
                        "LIMIT 3");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                String state = resultSet.getString("state");
                int delayCount = resultSet.getInt("delayCount");
                results.put(state, delayCount);
            }

        }

        return results;
    }


  /**
   * Create the airport table.
   * 
   * @param connection a database connection
   */
  public static void createAirportTable(Connection connection) throws SQLException {
    System.out.println("Creating airport table");

    // statement will get closed here as we are using try-with-resources
    try (PreparedStatement statement = connection
        .prepareStatement("CREATE TABLE airport (\n" + "airportCode varchar(5) PRIMARY KEY, \n"
            + "airportName varchar(100), \n" 
        	+ "city varchar(50), \n" 
        	+ "state varchar(10));");) {
      statement.execute();
    }
  }

  /**
   * Create the airport table.
   * 
   * @param connection a database connection
   */
  public static void createDelayedFlightTable(Connection connection) throws SQLException {

    System.out.println("Creating delayedFlights table");

    // statement will get closed here as we are using try-with-resources
    try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE  delayedFlights \n"
        + "(delayId int PRIMARY KEY, \n" + "month int, \n" + "dayOfMonth int, \n"
        + "dayOfWeek int, \n" + "actualDepTime int, \n" + "scheduledDepTime int, \n"
        + "actualArrTime int,\n" + "scheduledArrTime int, \n" + "uniqueCarrier varchar(5), \n"
        + "flightNum int,\n" + "actualFlightTime int, \n" + "scheduledFlightTime int, \n"
        + "airTime int, \n" + "arrDelay int, \n" + "depDelay int, \n" + "origin varchar(5),\n"
        + "FOREIGN KEY(origin) REFERENCES airport(airportCode), \n" + "dest varchar(5), \n"
        + "FOREIGN KEY(dest) REFERENCES airport(airportCode), \n" + "distance int);");) {
      statement.execute();
    }
  }

  /**
   * Insert data into the airport table.
   * 
   * @param connection a database connection
   * @param file the file containing the data
   * @throws IOException if the file cannot be accessed
   * @throws SQLException if the data cannot be inserted
   */
  public static void insertIntoAirportTableFromFile(Connection connection, String file)
	      throws IOException, SQLException {

	    System.out.println("Inserting data into airport table");

	    // stream, reader, and statement will get closed here as we are using try-with-resources
	    try (InputStream airportFile = MiniProject.class.getClassLoader().getResourceAsStream(file);
	         BufferedReader br = new BufferedReader(new InputStreamReader(airportFile, StandardCharsets.UTF_8));
	         PreparedStatement statement = connection.prepareStatement(
	                 "INSERT INTO airport (airportCode, airportName, city, state) VALUES (?, ?, ?, ?)")
	    ) {

	      String currentLine = null;
	      String[] brokenLine = null;

	      while ((currentLine = br.readLine()) != null) {
	        brokenLine = currentLine.split(",");

	        // Clear parameters from the previous iteration
	        statement.clearParameters();

	        int i;
	        for (i = 0; i < brokenLine.length; i++) {
	          statement.setString(i + 1, brokenLine[i]);
	        }

	        statement.addBatch();
	      }

	      statement.executeBatch();

	    } catch (SQLException e) {
	      throw e;
	    }
	}
/*
	PreparedStatement: I've initialized the PreparedStatement within the try-with-resources block, and it's now properly parameterized for the airport table.
	Clearing Parameters: I added statement.clearParameters() before setting parameters in each iteration to ensure the parameters are cleared from the previous batch.
*/

  /**
   * Insert data into the delayedFlights table.
   * 
   * @param connection a database connection
   * @param file the file containing the data
   * @throws IOException if the file cannot be accessed
   * @throws SQLException if the data cannot be inserted
   */
  public static void insertIntoDelayedFlightTableFromFile(Connection connection, String file)
      throws IOException, SQLException {

    System.out.println("Inserting data into delayedFlights table");

    String insert = "INSERT INTO delayedFlights VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    // stream, reader and statement will get closed here as we are using try-with-resources
    try (InputStream delayedFlights = MiniProject.class.getClassLoader().getResourceAsStream(file);
        PreparedStatement statement = connection.prepareStatement(insert);
        BufferedReader br =
            new BufferedReader(new InputStreamReader(delayedFlights, StandardCharsets.UTF_8));) {

      String currentLine = null;
      String[] brokenLine = null;

      while ((currentLine = br.readLine()) != null) {
        brokenLine = currentLine.split(",");
        int i;
        for (i = 0; i < brokenLine.length; i++) {
          if (i == 8 || i == 15 || i == 16) {
            statement.setString(i + 1, brokenLine[i]); // varchar values
          } else {
            statement.setInt(i + 1, Integer.valueOf(brokenLine[i])); // int values
          }
        }

        statement.addBatch();
      }

      statement.executeBatch();

    } catch (SQLException e) {
      throw e;
    }

  }

  /**
   * Drop the airport table and any associated views/tables.
   * 
   * @param connection a database connection
   */
  public static void dropAirportTable(Connection connection) throws SQLException {

    System.out.println("Dropping airport table");

    // statement will get closed here as we are using try-with-resources
    try (PreparedStatement st =
        connection.prepareStatement("DROP TABLE IF EXISTS airport CASCADE");) {
      st.execute();
    }
  }

  /**
   * Drop the delayed flights table and any associated views/tables.
   * 
   * @param connection a database connection
   */
  public static void dropDelayedFlightTable(Connection connection) throws SQLException {

    System.out.println("Dropping delayedFlights table");

    // statement will get closed here as we are using try-with-resources
    try (PreparedStatement statement =
        connection.prepareStatement("DROP TABLE IF EXISTS delayedFlights CASCADE");) {
      statement.execute();
    }
  }


  /**
   * Connect to your Postgres database on teachdb.cs.rhul.ac.uk.
   * @param user your username
   * @param password your password
   * @param databaseHost the host name of the database server
   * @return a new database connection
   */
  public static Connection connectToDatabase(String user, String password, String databaseHost)
      throws SQLException {

    // TODO - add code to connect to the specified database here
    Connection connection = null;
    try {
        // Load the PostgreSQL JDBC driver
        Class.forName("org.postgresql.Driver");

        // Build the database URL
        String url = "jdbc:postgresql://" + databaseHost + "/localhost";

        // Establish the connection
        connection = DriverManager.getConnection(url, user, password);

        System.out.println("Connected to the database");

    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        throw new SQLException("Failed to connect to the database");
    }
    // end TODO

    return connection;
  }

  
/*
	Method to check weather Insert method added the required values in the table.
*/  
  
  
  public static void showAirportTable(Connection connection) throws SQLException {
	    System.out.println("Showing all values in the Airport table:");

	    try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM airport");
	         ResultSet resultSet = statement.executeQuery()
	    ) {
	        // Print column headers
	        System.out.printf("%-15s %-30s %-20s %-10s%n", "Airport Code", "Airport Name", "City", "State");
	        System.out.println("------------------------------------------------------------");

	        // Iterate through the result set and print values
	        while (resultSet.next()) {
	            String airportCode = resultSet.getString("airportCode");
	            String airportName = resultSet.getString("airportName");
	            String city = resultSet.getString("city");
	            String state = resultSet.getString("state");

	            System.out.printf("%-15s %-30s %-20s %-10s%n", airportCode, airportName, city, state);
	        }
	    }
	}
  
  
/*
	Method to check weather Insert method added the required values in the Delayed Airport table.
*/  
  public static void showDelayedFlightsTable(Connection connection) throws SQLException {
	    System.out.println("Showing all values in the delayedFlights table:");

	    try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM delayedFlights");
	         ResultSet resultSet = statement.executeQuery()
	    ) {
	        // Print column headers
	        System.out.printf("%-10s %-10s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-10s %-10s %-10s%n",
	                "Delay ID", "Month", "Day of Month", "Day of Week", "Actual Dep Time", "Scheduled Dep Time",
	                "Actual Arr Time", "Scheduled Arr Time", "Unique Carrier", "Flight Num", "Actual Flight Time",
	                "Scheduled Flight Time", "Air Time", "Arrival Delay", "Departure Delay", "Origin", "Destination", "Distance");
	        System.out.println("----------------------------------------------------------------------------------------------------------------------------------");

	        // Iterate through the result set and print values
	        while (resultSet.next()) {
	            int delayId = resultSet.getInt("delayId");
	            int month = resultSet.getInt("month");
	            int dayOfMonth = resultSet.getInt("dayOfMonth");
	            int dayOfWeek = resultSet.getInt("dayOfWeek");
	            int actualDepTime = resultSet.getInt("actualDepTime");
	            int scheduledDepTime = resultSet.getInt("scheduledDepTime");
	            int actualArrTime = resultSet.getInt("actualArrTime");
	            int scheduledArrTime = resultSet.getInt("scheduledArrTime");
	            String uniqueCarrier = resultSet.getString("uniqueCarrier");
	            int flightNum = resultSet.getInt("flightNum");
	            int actualFlightTime = resultSet.getInt("actualFlightTime");
	            int scheduledFlightTime = resultSet.getInt("scheduledFlightTime");
	            int airTime = resultSet.getInt("airTime");
	            int arrDelay = resultSet.getInt("arrDelay");
	            int depDelay = resultSet.getInt("depDelay");
	            String origin = resultSet.getString("origin");
	            String dest = resultSet.getString("dest");
	            int distance = resultSet.getInt("distance");

	            System.out.printf("%-10s %-10s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-10s %-10s %-10s%n",
	                    delayId, month, dayOfMonth, dayOfWeek, actualDepTime, scheduledDepTime,
	                    actualArrTime, scheduledArrTime, uniqueCarrier, flightNum, actualFlightTime,
	                    scheduledFlightTime, airTime, arrDelay, depDelay, origin, dest, distance);
	        }
	    }
	}



  /**
   * Main method.
   * 
   * @param args any command line arguments
   */
  public static void main(String[] args) {

    Connection connection = null;

    // scanner will get closed here as we are using try-with-resources
    try (Scanner scanner = new Scanner(System.in);) {
      System.out.println("Please enter your username:");
      String user = scanner.nextLine();
      System.out.println("Please enter your database password:");
      String password = scanner.nextLine();
      System.out.println("Please enter the host name of the database you want to connect to:");
      String host = scanner.nextLine();

      
      //String user = "postgres";
      //String password = "root";
      //String host = "localhost";
      connection = MiniProject.connectToDatabase(user, password, host);

      dropAirportTable(connection);
      dropDelayedFlightTable(connection);

      createAirportTable(connection);
      createDelayedFlightTable(connection);

      insertIntoAirportTableFromFile(connection, "airport.csv");
      //showAirportTable(connection);
      insertIntoDelayedFlightTableFromFile(connection, "delayedFlights.csv");
      //showDelayedFlightsTable(connection);

      

      System.out.println(firstQuery(connection));
      System.out.println(secondQuery(connection));
      System.out.println(thirdQuery(connection));
      System.out.println(fourthQuery(connection));
      System.out.println(fifthQuery(connection));

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        // always ensure connections are closed,
        // note we couldn't use a try-with-resources here
        // because we needed the username / password entered
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }

}



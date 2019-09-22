import jdk.internal.util.xml.impl.Input;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DatabaseManager {
    /*
    Date
     */

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date date = new Date();
    String result = dateFormat.format(date);

    /*
    Database & Scanner
     */

    Database database = new Database("date.db");
    Scanner scanner = new Scanner(System.in);
    String databaseFullPath = database.getDatabaseFullPath();

    /*
    Database Setup
    Table: date
    Fields: name (TEXT), date (TEXT)
     */

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS date (\n" +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "   name TEXT,\n" +
                "   date TEXT\n" +
                ");";

        database.setup(sql);
    }

    /*
    Some instructions
     */

    public void askWhatToDo() {
        int input;

        do {
            System.out.println("Welcome to DateReminder! Date: " + result +
                    "\n1. Add a date" +
                    "\n2. Remove a date" +
                    "\n3. Get all date" +
                    "\n4. Check" +
                    "\n5. Exit");

            input = scanner.nextInt();

            switch (input) {
                case 1:
                    addDate();
                    break;
                case 2:
                    getAllDate();
                    System.out.print("ID: ");
                    int idToDelete = scanner.nextInt();
                    deleteDate(idToDelete);
                    break;
                case 3:
                    System.out.println(String.format("%-6s | %-15s | %-2s", "ID", "NAME", "DATE"));
                    getAllDate();
                    break;
                case 4:
                    check();
                    break;
                default:
                    System.out.println("Please insert a number!");
                    System.out.println();
                    break;
            }
        } while (input != 5);

        System.out.println("Bye!");
    }

    public void addDate() {
        String flush = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.println("Now, you have to add the date in this format: yyyy/MM/dd");
        System.out.print("Date: ");
        String date = scanner.nextLine();
        insertDate(name, date);
        System.out.println();
    }

    public void deleteDate(int id) {
        String sql = "DELETE FROM date WHERE id = ?";

        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void check() {
        String sql = "SELECT * FROM date WHERE date = ?";

        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, result);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(String.format("%-6s | %-15s | %-4s", resultSet.getInt("id"),
                        resultSet.getString("name"), resultSet.getString("date")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
    Database instructions
     */

    private Connection connect() {
        String database = "jdbc:sqlite:" + databaseFullPath;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(database);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return connection;
    }

    public void insertDate(String name, String date) {
        String sql = "INSERT INTO date (name, date) VALUES(?, ?)";

        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name.toUpperCase());
            preparedStatement.setString(2, date);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getAllDate() {
        String sql = "SELECT * FROM date";

        try (Connection connection = this.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                System.out.println(String.format("%-6s | %-15s | %-4s", resultSet.getInt("id"),
                        resultSet.getString("name"), resultSet.getString("date")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Database {
    final String databaseFullPath;

    public Database(String databaseName) {
        databaseFullPath = "DateReminder/database/" + databaseName;
    }

    public String getDatabaseFullPath() {
        return databaseFullPath;
    }

    /*
    Create database dir
     */

    public void setup(String sql) {
        File file = new File(databaseFullPath);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                createNewDatabase(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createNewDatabase(String sql) {
        String url = "jdbc:sqlite:" + databaseFullPath;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                createNewTable(sql);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createNewTable(String sql) {
        String url = "jdbc:sqlite:" + databaseFullPath;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(String.valueOf(sql));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

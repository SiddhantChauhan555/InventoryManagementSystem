
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/inventory_db";

    private static final String USER = "root";

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "DB_PASSWORD environment variable is not set."
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

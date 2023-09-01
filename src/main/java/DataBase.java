import java.sql.*;


public class DataBase {
    private static final String dbClassName = "com.mysql.cj.jdbc.Driver";

    private static final String CONNECTION = "jdbc:mysql://localhost:3306/bank_account_demo";

    private static final String USER = "root";

    private static final String PASSWORD = "root";

    public static java.sql.Connection connection() throws Exception {
        Class.forName(dbClassName);

        return DriverManager.getConnection(CONNECTION, USER, PASSWORD);
    }
}
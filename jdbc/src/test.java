import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class test {
    public static void main(String[] args) {
        try {
            Class.forName("com.sea.lyra.jdbc.LyraDriver");
            Connection connection = DriverManager
                    .getConnection("jdbc:lyra://localhost:5494/lyra",
                            "root",
                            "123456");
            System.out.println(connection);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from `user`");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("username"));
                System.out.println(resultSet.getString("passwd"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

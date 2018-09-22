import java.sql.*;

public class test {
    public static void main(String[] args) {
        try {
            Class.forName("com.sea.lyra.jdbc.LyraDriver");
            Connection connection = DriverManager
                    .getConnection("jdbc:lyra://localhost:5494/test",
                            "root",
                            "123456");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from test_table");
            System.out.println(resultSet.getRow());//获取行数
            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();// 获取列数
                for (int i = 0; i < columnCount; i++) {
                    String columnName = metaData.getColumnName(i);//根据索引获取列名
                    String data = resultSet.getString(columnName);
                    System.out.println(data);
                }
//                String username = resultSet.getString("username");//根据列名获取数据
//                String user = resultSet.getString(1);//根据索引获取数据
//                System.out.println(username);
//                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

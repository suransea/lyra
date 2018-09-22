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
            StringBuilder sql = new StringBuilder("insert into test_table values");
            for (int i = 0; i < 100; i++) {
                sql.append("(12432432,'sdfrg',214325),");
                //System.out.println(i);
            }
            sql.append("(12432432,'sdfrg',214325)");
            statement.execute(sql.toString());
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

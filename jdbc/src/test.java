import java.sql.*;

public class test {
    public static void main(String[] args) {
        try {
            Class.forName("com.sea.lyra.Driver");
            Connection connection = DriverManager
                    .getConnection("jdbc:lyra://localhost:5494/lyra",
                            "root",
                            "123456");
            System.out.println(connection);
            Statement statement = connection.createStatement();
//            StringBuilder sql = new StringBuilder("insert into test_table values");
//            for (int i = 0; i < 10; i++) {
//                sql.append("(12432432,'sdfrg',214325),");
//            }
//            sql.append("(12432432,'sdfrg',214325)");
//            long time = System.currentTimeMillis();
//            System.out.println("begin");
//            statement.execute(sql.toString());
//            System.out.println(System.currentTimeMillis() - time);
            ResultSet resultSet = statement.executeQuery("select * from `user`");
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
            connection.close();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

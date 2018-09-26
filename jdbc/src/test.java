import com.sea.lyra.jdbc.LyraDataSource;

import java.sql.Connection;
import java.sql.Statement;

public class test {
    public static void main(String[] args) {
        try {
//            Class.forName("com.sea.lyra.Driver");
//            Connection connection = DriverManager
//                    .getConnection("jdbc:lyra://localhost:5494/test",
//                            "root",
//                            "123456");
//            System.out.println(connection);
//            Statement statement = connection.createStatement();
//            statement.execute("create table test_table (id int,a int ,b varchar(100),c int , d varchar(10))");
//            StringBuilder sql = new StringBuilder("insert into test_table values");
//            Random random = new Random();
//            for (int i = 0; i < 1000000; i++) {
//                sql.append(String.format("(%d,%d,'%d',%d,'%d'),",
//                        i,
//                        random.nextInt(),
//                        random.nextInt(),
//                        random.nextInt(),
//                        random.nextInt()
//                ));
//            }
//            sql.append("(1000000,213812,'end',214325,'wuuewu')");
//            long time = System.currentTimeMillis();
//            System.out.println("begin");
//            statement.execute(sql.toString());
//            System.out.println(System.currentTimeMillis() - time);
//            ResultSet resultSet = statement.executeQuery("select * from test_table");
//            System.out.println(resultSet.getRow());//获取行数
//            while (resultSet.next()) {
//                ResultSetMetaData metaData = resultSet.getMetaData();
//                int columnCount = metaData.getColumnCount();// 获取列数
//                for (int i = 1; i <= columnCount; i++) {
//                    String columnName = metaData.getColumnName(i);//根据索引获取列名
//                    String data = resultSet.getString(columnName);
//                    System.out.println(data);
//                }
//            }
//            statement.close();
//            connection.close();
            LyraDataSource dataSource = new LyraDataSource();
            dataSource.setUrl("jdbc:lyra://localhost:5494/test");
            Connection connection = dataSource.getConnection("root", "123456");
            Statement statement = connection.createStatement();
            System.out.println(statement.execute("show databases"));
            connection.close();
            dataSource = new LyraDataSource();
            dataSource.setUrl("jdbc:lyra://localhost:5494/lyra");
            connection = dataSource.getConnection("root", "123456");
            statement = connection.createStatement();
            System.out.println(statement.execute("show databases"));
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

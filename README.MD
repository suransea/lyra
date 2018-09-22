# Lyra (天琴座)

## 项目介绍

数据库原型系统

## 使用说明

### lyrad

* `java -jar Lyrad.jar`

### client

* `lyra [-h <address>] [-u <user> [-p]] `
* `address`: 服务器ip地址， 默认为127.0.0.1
* `user`: 用户名
* `p`: 登录用户是否包含密码

##### 测试

* `lyra -u root -p < test.sql`

### JDBC 样例

```java
public class test {
    public static void main(String[] args) {
        try {
            Class.forName("com.sea.lyra.jdbc.LyraDriver");
            Connection connection = DriverManager
                    .getConnection("jdbc:lyra://localhost:5494/lyra",
                            "root",
                            "123456");
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
```

> 默认用户root的初始密码为`123456`

### SQL (不区分大小写)

#### DAL:

* `USE <DB_NAME>`
* `SHOW DATABASES`
* `SHOW TABLES`

#### DCL:

* `CREATE USER <USERNAME> IDENTIFED BY "PASSWORD"`
* `ALTER USER <USERNAME> IDENTIFED BY "PASSWORD"`

> 删除用户请使用DELETE操作`lyra`数据库的`user`表

#### DDL:

* `CREATE DATABASE <DB_NAME>`
* `CREATE TABLE <TABLE_NAME> (<COLUMN_NAME> <TYPE>[,<COLUMN_NAME> <TYPE>]...)`
* `DROP DATABASE <DB_NAME>`
* `DROP TABLE <TABLE_NAME>`

> TYPE 目前支持INT与VARCHAR(len)

#### DML:

* `INSERT INTO <TABLE_NAME>[(<COLUMN_NAME>[,<COLUMN_NAME>]...)] VALUES("VALUE"[,"VALUE"]...)`
* `DELETE FROM <TABLE_NAME> [WHERE <COLUMN>{=|>|<|>=|<=|<>|!=}"VALUE" [{AND|OR} <COLUMN>{=|>|<|>=|<=|<>|!=}"VALUE"]...]`
* `UPDATE <TABLE_NAME> SET <COLUMN_NAME>="VALUE"[,<COLUMN_NAME>="VALUE"]... [WHERE <COLUMN>{=|>|<|>=|<=|<>|!=}"VALUE" [{AND|OR} <COLUMN>{=|>|<|>=|<=|<>|!=}"VALUE"]...]`

#### DQL:

* `SELECT {*|<COLUMN_NAME>[,<COLUMN_NAME>]...} FROM <TABLE_NAME> [WHERE <COLUMN>{=|>|<|>=|<=|<>|!=}"VALUE" [{AND|OR} <COLUMN>{=|>|<|>=|<=|<>|!=}"VALUE"]...] [ORDER BY <COLUMN>[,<COLUMN>]... [{ASC|DESC}]`

#### 当前未支持：

* jdbc 仅实现简单功能
* GROUP BY
* 约束
* IN 和NOT IN
* WITH
* 创建视图
* 索引
* 聚集函数
* WHERE 子句中不能包含括号，只能按顺序判断
* etc...
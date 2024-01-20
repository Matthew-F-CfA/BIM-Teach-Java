import java.sql.*;

class JDBCExample {

  public static void main(String args[]) {
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

      Connection connection=DriverManager.getConnection("jdbc:derby:MyDbTest");

      Statement statement=connection.createStatement();

      try {
        statement.execute("DROP TABLE Persons");
      }
      catch(Exception ex) {
      }

      statement=connection.createStatement();

      statement.execute("CREATE TABLE Persons (name varchar(255), phone varchar(255), email varchar(255))");

      statement=connection.createStatement();

      statement.execute("INSERT INTO Persons (name, phone, email) VALUES ('John Smith', '012-345-6789', 'john@test.com')");

      statement=connection.createStatement();

      statement.execute("INSERT INTO Persons (name, phone, email) VALUES ('Joe Smith', '987-654-3210', 'joe@test.com')");

      statement=connection.createStatement();

      statement.execute("INSERT INTO Persons (name, phone, email) VALUES ('Mary Smith', '121-212-1212', 'mary@test.com')");

      statement=connection.createStatement();

      statement.execute("UPDATE Persons SET phone='878-787-8787' WHERE name LIKE 'Mary%'");

      statement=connection.createStatement();

      statement.execute("DELETE FROM Persons WHERE name='Joe Smith'");

      statement=connection.createStatement();

      ResultSet resultSet=statement.executeQuery("select name, phone, email from Persons");

      while (resultSet.next())
        System.out.println(resultSet.getString(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getString(3));

      connection.close();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}
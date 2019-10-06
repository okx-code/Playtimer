package sh.okx.playtimer.database.pool;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolMySql implements ConnectionPool {
  private final HikariDataSource hikariDataSource;

  public ConnectionPoolMySql(String host, int port, String database, String user, String password) {
    hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false");
    hikariDataSource.setUsername(user);
    hikariDataSource.setPassword(password);
  }

  @Override
  public Connection getConnection() throws SQLException {
    return hikariDataSource.getConnection();
  }

  @Override
  public void close() {
    hikariDataSource.close();
  }
}

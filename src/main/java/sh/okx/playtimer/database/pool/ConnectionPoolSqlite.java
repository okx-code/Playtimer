package sh.okx.playtimer.database.pool;

import sh.okx.playtimer.database.UncloseableConnection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPoolSqlite implements ConnectionPool {
  private UncloseableConnection connection;

  public ConnectionPoolSqlite(File dataFolder, String file) {
    try {
      this.connection = new UncloseableConnection(DriverManager.getConnection("jdbc:sqlite:"
          + new File(dataFolder, file)));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Connection getConnection() {
    return connection;
  }

  @Override
  public void close() throws SQLException {
    connection.forceClose();
  }
}

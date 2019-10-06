package sh.okx.playtimer.database.pool;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {
  Connection getConnection() throws SQLException;
  void close() throws SQLException;
}

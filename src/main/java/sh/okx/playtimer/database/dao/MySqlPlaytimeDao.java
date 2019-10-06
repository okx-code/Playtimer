package sh.okx.playtimer.database.dao;

import sh.okx.playtimer.database.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@SuppressWarnings({"SqlNoDataSourceInspection"})
public class MySqlPlaytimeDao implements PlaytimeDao {
  private static final String CREATE_PLAYTIME = "CREATE TABLE IF NOT EXISTS playtime (" +
      "uuid VARCHAR(36)," +
      "played INT," +
      "limit_remaining INT, " +
      "PRIMARY KEY (uuid))";
  private static final String GET_PLAYTIME = "SELECT played FROM playtime WHERE uuid = ?";
  private static final String ADD_PLAYTIME = "INSERT INTO playtime (uuid, played, limit_remaining) " +
      "VALUES (?, ?, ?) " +
      "ON DUPLICATE KEY UPDATE played = played + LEAST(?, limit_remaining), " +
      "limit_remaining = GREATEST(0, limit_remaining - ?)";
  private static final String SUBTRACT_PLAYTIME = "UPDATE playtime SET played = played - ? WHERE uuid = ?";
  private static final String SET_LIMIT = "UPDATE playtime SET limit_remaining = ?";

  private final ConnectionPool pool;
  private final int defaultLimitRemaining;

  public MySqlPlaytimeDao(ConnectionPool pool, int defaultLimitRemaining) {
    this.pool = pool;
    this.defaultLimitRemaining = defaultLimitRemaining;
  }

  @Override
  public void init() {
    try (Connection connection = pool.getConnection()) {
      connection.createStatement().executeUpdate(CREATE_PLAYTIME);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getPlaytime(UUID uuid) {
    try (Connection connection = pool.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(GET_PLAYTIME);
      statement.setString(1, uuid.toString());

      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        return resultSet.getInt("time");
      } else {
        return 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  @Override
  public void addPlaytime(UUID uuid, int amount) {
    try (Connection connection = pool.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(ADD_PLAYTIME);

      statement.setString(1, uuid.toString());
      statement.setInt(2, Math.min(defaultLimitRemaining, amount));
      statement.setInt(3, Math.max(defaultLimitRemaining - amount, 0));
      statement.setInt(4, amount);
      statement.setInt(5, amount);

      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void subtractPlaytime(UUID uuid, int amount) {
    try (Connection connection = pool.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(SUBTRACT_PLAYTIME);

      statement.setInt(1, amount);
      statement.setString(2, uuid.toString());

      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setLimit(int limit) {
    if (limit < 0) {
      limit = defaultLimitRemaining;
    }

    try (Connection connection = pool.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(SET_LIMIT);

      statement.setInt(1, limit);

      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

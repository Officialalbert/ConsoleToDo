package Dao;

import Errors.WrongException;
import Utils.ConfigFileUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.DaoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DaoClass {

    private static final String URL = ConfigFileUtils.getCONFIG("app.datasource.url");
    private static final String USER = ConfigFileUtils.getCONFIG("app.datasource.user");
    private static final String PASSWORD = ConfigFileUtils.getCONFIG("app.datasource.password");
    private static final Logger logger = LoggerFactory.getLogger(DaoClass.class);

    private static DaoClass INSTANCE;

    public static DaoClass getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoClass();
        }
        return INSTANCE;
    }

    // В DAOClass
    public void update(String newStr, int id) {

        String sql = "UPDATE dao_data SET value = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStr);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Запись с ID {} успешно обновлена", id);
            } else {
                logger.warn("Запись с ID {} не найдена для обновления", id);
            }

        } catch (SQLException e) {
            logger.error("Ошибка при обновлении записи с ID: {}", id, e);
        }

    }

    public void delete(int id) {
        String sql = "DELETE FROM dao_data WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Запись с ID {} успешно удалена", id);
            } else {
                logger.warn("Запись с ID {} не найдена для удаления", id);
            }

        } catch (SQLException e) {
            logger.error("Ошибка при удалении записи с ID: {}", id, e);
        }
    }

    public void save(String value) {
        String sql = "INSERT INTO dao_data (value) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, value);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    System.out.println("Сохранено ID " + id);
                    logger.info("Сохранён новый ID: {}", id);
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка сохранения в БД", e);
        }

    }

    public Optional<DaoEntity> findById(int id) {
        String sql = "SELECT * FROM dao_data WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DaoEntity entity = new DaoEntity();
                    entity.setId(rs.getLong("id"));
                    entity.setValue(rs.getString("value"));
                    entity.setCreatedAt(rs.getTimestamp("createdAt"));
                    entity.setUpdatedAt(rs.getTimestamp("updated_At"));

                    logger.info("Найдена запись с ID: {}", id);
                    return Optional.of(entity);
                }
                else {
                    logger.warn("Запись с ID {} не найдена", id);
                    return Optional.empty();
                }
            }


        } catch (SQLException e) {
            logger.error("Ошибка при поиске записи по ID: {}", id, e);
            return Optional.empty();
        }
    }

    public List<DaoEntity> findAll() {
        List<DaoEntity> results = new ArrayList<>();
        String sql = "SELECT id, value, created_at, updated_at FROM dao_data";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DaoEntity entity = new DaoEntity();
                entity.setId(rs.getLong("id"));
                entity.setValue(rs.getString("value"));
                entity.setCreatedAt(rs.getTimestamp("created_at"));
                entity.setUpdatedAt(rs.getTimestamp("updated_at"));
                results.add(entity);
            }

            logger.info("Загружено записей: {}", results.size());

        } catch (SQLException e) {
            logger.error("Ошибка при получении всех записей", e);
        }

        return results;
    }


    public void exception() { int a = 3; if (a == 3) { try { throw new WrongException("Exception"); } catch (WrongException e) { e.printStackTrace(); } } }

}

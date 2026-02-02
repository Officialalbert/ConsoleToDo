package Dao;

import Errors.WrongException;
import Utils.ConfigFileUtils;
import Utils.HibernateConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.DaoEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DaoJdbc {

    private static final String URL = ConfigFileUtils.getCONFIG("app.datasource.url");
    private static final String USER = ConfigFileUtils.getCONFIG("app.datasource.user");
    private static final String PASSWORD = ConfigFileUtils.getCONFIG("app.datasource.password");
    private static final Logger logger = LoggerFactory.getLogger(DaoJdbc.class);
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    private static HikariDataSource dataSource;
    private static DaoJdbc INSTANCE;

    static {
        initializeDataSource();
    }

    public static synchronized DaoJdbc getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoJdbc();
        }
        return INSTANCE;
    }

    private static void initializeDataSource() {
        try {
            HikariConfig config = new HikariConfig();

            // Базовые настройки подключения
            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PASSWORD);

            // Настройки пула соединений
            config.setPoolName("HikariPool-DaoClass");
            config.setMaximumPoolSize(10); // Максимальное количество соединений
            config.setMinimumIdle(5); // Минимальное количество простаивающих соединений
            config.setConnectionTimeout(30000); // 30 секунд
            config.setIdleTimeout(600000); // 10 минут
            config.setMaxLifetime(1800000); // 30 минут
            config.setConnectionTestQuery("SELECT 1");
            config.setAutoCommit(false); // Отключаем авто-коммит

            dataSource = new HikariDataSource(config);

            logger.info("HikariCP пул инициализирован успешно");

        } catch (Exception e) {
            logger.error("Ошибка инициализации HikariCP пула", e);
            throw new RuntimeException("Не удалось инициализировать пул соединений", e);
        }
    }

    public static void shutdownDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP пул закрыт");
        }
    }

    // В DAOClass
    public void update(String newStr, long id) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try {
                DaoEntity entity = session.find(DaoEntity.class, id);
                if (entity != null) {
                    // Просто меняем значение в объекте
                    entity.setValue(newStr);
                    transaction.commit();
                    logger.info("Id {} успешно заменен", id);
                } else {
                    logger.warn("Запись с ID {} не найдена для обновления", id);
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                logger.error("ошибка в update dao", e);
                throw e;
            }
        }
    }

    public void delete(long id) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            try {
                DaoEntity entity = session.find(DaoEntity.class, id);

                if (entity != null) {
                    session.remove(entity);
                    logger.info("Запись с ID {} удалена", id);
                } else {
                    logger.warn("Запись с ID {} не найдена", id);
                }

                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                logger.error("Ошибка при удалении записи с ID {}", id, e);
                throw e;
            }
        }
    }

    public Optional<Long> save(String value) {
        if (value == null || value.trim().isEmpty()) {
            logger.error("Попытка сохранения пустого значения");
            return Optional.empty();
        }
        String sql = "INSERT INTO dao_data (value) VALUES (?)";
        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, value);
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        long id = keys.getLong(1);
                        conn.commit(); // Durability - фиксируем изменения
                        logger.info("Сохранён новый ID: {}", id);
                        return Optional.of(id);
                    } else {
                        conn.rollback(); // Atomicity
                        logger.error("Не удалось получить сгенерированный ID");
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка сохранения в БД", e);
            rollbackQuietly(conn);
            return Optional.empty();
        } finally {
            closeQuietly(conn);
        }
    }

    public Optional<DaoEntity> findById(long id) {
        String sql = "SELECT id, value, created_at, updated_at FROM dao_data WHERE id = ?";
        Connection conn = null;

        try {
            conn = getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        DaoEntity entity = new DaoEntity();
                        entity.setId(rs.getLong("id"));
                        entity.setValue(rs.getString("value"));
                        entity.setCreatedAt(rs.getTimestamp("created_at"));
                        entity.setUpdatedAt(rs.getTimestamp("updated_at"));

                        conn.commit(); // Завершаем транзакцию чтения
                        logger.info("Найдена запись с ID: {}", id);
                        return Optional.of(entity);
                    } else {
                        conn.commit();
                        logger.warn("Запись с ID {} не найдена", id);
                        return Optional.empty();
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Ошибка при поиске записи по ID: {}", id, e);
            rollbackQuietly(conn);
            return Optional.empty();
        } finally {
            closeQuietly(conn);
        }
    }

    public List<DaoEntity> findAll() {
        List<DaoEntity> results = new ArrayList<>();
        String sql = "SELECT id, value, created_at, updated_at FROM dao_data";
        Connection conn = null;

        try {
            conn = getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    DaoEntity entity = new DaoEntity();
                    entity.setId(rs.getLong("id"));
                    entity.setValue(rs.getString("value"));
                    entity.setCreatedAt(rs.getTimestamp("created_at"));
                    entity.setUpdatedAt(rs.getTimestamp("updated_at"));
                    results.add(entity);
                }

                conn.commit(); // Завершаем транзакцию чтения
                logger.info("Загружено записей: {}", results.size());

            }

        } catch (SQLException e) {
            logger.error("Ошибка при получении всех записей", e);
            rollbackQuietly(conn);
        } finally {
            closeQuietly(conn);
        }

        return results;
    }

    // пример создания исключений и их выбрасывания.
    public void exception() { int a = 3; if (a == 3) { try { throw new WrongException("Exception"); } catch (WrongException e) { e.printStackTrace(); } } }

    private void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Ошибка при закрытии соединения", e);
            }
        }
    }

    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                logger.debug("Транзакция откачена");
            } catch (SQLException e) {
                logger.error("Ошибка при откате транзакции", e);
            }
        }
    }

    private Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        conn.setTransactionIsolation(TRANSACTION_ISOLATION_LEVEL);
        return conn;
    }
}

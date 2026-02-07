package Dao;

import Errors.DaoException;
import Errors.EntityNotFoundException;
import Errors.WrongException;
import Utils.HibernateConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.DaoEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DaoHibernate {

    private static final Logger logger = LoggerFactory.getLogger(DaoHibernate.class);

    private static DaoHibernate INSTANCE;

    public static synchronized DaoHibernate getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoHibernate();
        }
        return INSTANCE;
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
                    logger.info("Id {} успешно заменен", id);
                } else {
                    logger.warn("Запись с ID {} не найдена для обновления", id);
                    throw new EntityNotFoundException(id);
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                logger.error("ошибка в update dao", e);
                throw new DaoException("Ошибка обновления сущности", e);
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
                throw new DaoException("Ошибка удаления сущности", e);
            }
        }
    }

    public Optional<Long> save(String value) {
        DaoEntity entity = new DaoEntity(value);
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(entity);
                logger.info("Запись успешно сохранена");
                transaction.commit();
                Long id = entity.getId();
                logger.info("Сохранён новый ID: {}", id);
                return Optional.of(id);
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Ошибка при сохранение записи", e);
                throw new DaoException("Ошибка сохранения сущности", e);
            }
        }
    }

        public Optional<DaoEntity> findById(long id) {
            try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
                return Optional.ofNullable(session.find(DaoEntity.class, id));
            }
        }

        public List<DaoEntity> findAll(int page, int size) {
            try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
                return session.createQuery("SELECT s FROM DaoEntity s", DaoEntity.class).setFirstResult(page*size).setMaxResults(size).getResultList();
            }
        }

    // пример создания исключений и их выбрасывания.
    public void exception() { int a = 3; if (a == 3) { try { throw new WrongException("Exception"); } catch (WrongException e) { e.printStackTrace(); } } }
}

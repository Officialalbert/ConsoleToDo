package ru.albert.consoletodo.utils;

import lombok.Data;
import ru.albert.consoletodo.model.DaoEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class HibernateConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(HibernateConfiguration.class);

    private static SessionFactory sessionFactory;
    static {
        try{
            logger.info("Начинаем инициализацию Hibernate...");
            Configuration configuration = new Configuration();
            configuration.configure("Hibernate.cfg.xml");
            // Явно регистрируем Entity класс
            configuration.addAnnotatedClass(DaoEntity.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            logger.info("Hibernate SessionFactory успешно инициализирована");

        } catch (Exception e) {
        logger.error("Ошибка при инициализации Hibernate", e);
        throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            logger.info("Закрываем Hibernate SessionFactory...");
            sessionFactory.close();
            logger.info("Hibernate SessionFactory закрыта");
        }
    }
}
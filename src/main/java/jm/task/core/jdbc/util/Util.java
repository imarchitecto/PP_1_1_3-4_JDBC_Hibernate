package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.ServiceRegistry;
public class Util {
    // реализуйте настройку соеденения с БД
    private static Util instance = null;
    private static SessionFactory sessionFactory;
    private static Metadata metadata;

    private Util() {
        if (sessionFactory == null) {
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySetting(Environment.USE_SQL_COMMENTS, false)
                    .applySetting(Environment.SHOW_SQL, true)
                    .applySetting(Environment.HBM2DDL_AUTO, "build")
                    .build();
            sessionFactory = makeSessionFactory(serviceRegistry);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static SessionFactory makeSessionFactory(ServiceRegistry serviceRegistry) {
        metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .getMetadataBuilder()
                .build();
        return metadata.buildSessionFactory();
    }

    public String getTableName(String entityName) throws NullPointerException {
        if (null == metadata) {
            throw new NullPointerException("Metadata is null");
        }

        for (PersistentClass persistentClass : Util.metadata.getEntityBindings()) {
            if (entityName.equals(persistentClass.getJpaEntityName())) {
                return persistentClass.getTable().getName();
            }
        }

        throw new NullPointerException(String.format("Entity {%s} not found", entityName));
    }

    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }

}

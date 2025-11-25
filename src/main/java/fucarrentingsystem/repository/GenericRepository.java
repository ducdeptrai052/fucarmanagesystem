package fucarrentingsystem.repository;

import fucarrentingsystem.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Consumer;

public class GenericRepository<T> {

    private final Class<T> type;

    public GenericRepository(Class<T> type) {
        this.type = type;
    }

    public java.util.List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from " + type.getSimpleName(), type).getResultList();
        }
    }

    public void saveOrUpdate(T entity) {
        executeInTx(session -> session.saveOrUpdate(entity));
    }

    public void delete(T entity) {
        executeInTx(session -> session.delete(entity));
    }

    private void executeInTx(Consumer<Session> action) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }
}

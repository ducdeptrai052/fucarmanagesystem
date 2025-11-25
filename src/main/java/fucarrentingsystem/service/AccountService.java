package fucarrentingsystem.service;

import fucarrentingsystem.config.HibernateUtil;
import fucarrentingsystem.entity.Account;
import org.hibernate.Session;

public class AccountService extends GenericService<Account> {

    public AccountService() {
        super(Account.class);
    }

    public Account findByAccountName(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Account a where a.accountName = :u",
                            Account.class)
                    .setParameter("u", username)
                    .uniqueResult();
        }
    }
}

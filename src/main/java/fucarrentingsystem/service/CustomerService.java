package fucarrentingsystem.service;

import fucarrentingsystem.config.HibernateUtil;
import fucarrentingsystem.entity.Account;
import fucarrentingsystem.entity.CarRental;
import fucarrentingsystem.entity.Customer;
import fucarrentingsystem.entity.Review;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CustomerService {

    // Lấy danh sách khách hàng cho bảng Admin
    public List<Customer> getAllCustomers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Customer", Customer.class).list();
        }
    }

    /* =========== API public – controller dùng =========== */

    public void save(Customer c) {
        saveInternal(c);
    }

    public void saveCustomer(Customer c) {
        saveInternal(c);
    }

    public void delete(Customer c) {
        deleteInternal(c);
    }

    public void deleteCustomer(Customer c) {
        deleteInternal(c);
    }

    /* =========== Logic bên trong =========== */

    private void saveInternal(Customer c) {
        if (c == null) {
            throw new IllegalArgumentException("Customer is null");
        }

        // Kiểm tra các field bắt buộc (tránh NOT NULL constraint fail)
        if (c.getCustomerName() == null || c.getCustomerName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (c.getMobile() == null || c.getMobile().isBlank()) {
            throw new IllegalArgumentException("Mobile is required");
        }
        if (c.getBirthday() == null) {
            throw new IllegalArgumentException("Birthday is required");
        }
        if (c.getEmail() == null || c.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (c.getPassword() == null || c.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Tìm account theo email (nếu có)
            Account existingAcc = session.createQuery(
                            "FROM Account a WHERE a.accountName = :email",
                            Account.class)
                    .setParameter("email", c.getEmail())
                    .uniqueResult();

            if (c.getCustomerId() != null) {
                // Update khách hàng: load bản gốc từ DB rồi cập nhật field
                Customer dbCus = session.get(Customer.class, c.getCustomerId());
                if (dbCus == null) {
                    throw new IllegalArgumentException("Customer not found with id = " + c.getCustomerId());
                }

                dbCus.setCustomerName(c.getCustomerName());
                dbCus.setMobile(c.getMobile());
                dbCus.setBirthday(c.getBirthday());
                dbCus.setIdentityCard(c.getIdentityCard());
                dbCus.setLicenceNumber(c.getLicenceNumber());
                dbCus.setLicenceDate(c.getLicenceDate());
                dbCus.setEmail(c.getEmail());
                dbCus.setPassword(c.getPassword());

                // Xử lý account
                if (dbCus.getAccount() == null) {
                    if (existingAcc != null) {
                        dbCus.setAccount(existingAcc);
                    } else {
                        Account newAcc = new Account();
                        newAcc.setAccountName(c.getEmail());
                        newAcc.setRole("Customer");
                        session.save(newAcc);
                        dbCus.setAccount(newAcc);
                    }
                } else {
                    // đã có account -> update lại email
                    dbCus.getAccount().setAccountName(c.getEmail());
                    dbCus.getAccount().setRole("Customer");
                    session.update(dbCus.getAccount());
                }

                session.update(dbCus);

            } else {
                // Insert khách hàng mới
                Customer newCus = new Customer();
                newCus.setCustomerName(c.getCustomerName());
                newCus.setMobile(c.getMobile());
                newCus.setBirthday(c.getBirthday());
                newCus.setIdentityCard(c.getIdentityCard());
                newCus.setLicenceNumber(c.getLicenceNumber());
                newCus.setLicenceDate(c.getLicenceDate());
                newCus.setEmail(c.getEmail());
                newCus.setPassword(c.getPassword());

                if (existingAcc != null) {
                    newCus.setAccount(existingAcc);
                } else {
                    Account newAcc = new Account();
                    newAcc.setAccountName(c.getEmail());
                    newAcc.setRole("Customer");
                    session.save(newAcc);
                    newCus.setAccount(newAcc);
                }

                session.save(newCus);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    private void deleteInternal(Customer c) {
        if (c == null || c.getCustomerId() == null) {
            throw new IllegalArgumentException("Please select a customer to delete");
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Customer dbCus = session.get(Customer.class, c.getCustomerId());
            if (dbCus == null) {
                throw new IllegalArgumentException("Customer not found");
            }

            // Kiểm tra ràng buộc: đã có rental hoặc review chưa
            Long rentalCount = session.createQuery(
                            "SELECT COUNT(r) FROM CarRental r WHERE r.customer.customerId = :cid",
                            Long.class)
                    .setParameter("cid", dbCus.getCustomerId())
                    .uniqueResult();

            Long reviewCount = session.createQuery(
                            "SELECT COUNT(rv) FROM Review rv WHERE rv.customer.customerId = :cid",
                            Long.class)
                    .setParameter("cid", dbCus.getCustomerId())
                    .uniqueResult();

            if (rentalCount != null && rentalCount > 0) {
                throw new IllegalStateException("Cannot delete customer because there are rental transactions.");
            }

            if (reviewCount != null && reviewCount > 0) {
                throw new IllegalStateException("Cannot delete customer because there are reviews.");
            }

            // Nếu không có ràng buộc -> xóa
            session.delete(dbCus);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}

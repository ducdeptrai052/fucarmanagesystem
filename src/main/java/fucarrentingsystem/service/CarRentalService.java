package fucarrentingsystem.service;

import fucarrentingsystem.config.HibernateUtil;
import fucarrentingsystem.entity.Car;
import fucarrentingsystem.entity.CarRental;
import fucarrentingsystem.entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CarRentalService {

    // Lấy toàn bộ rental cho tab Manage Rental & Report
    public List<CarRental> getAllRentals() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CarRental", CarRental.class).list();
        }
    }

    /* =========== API public – dùng trong AdminMainController =========== */

    public void save(CarRental r) {
        saveInternal(r);
    }

    public void delete(CarRental r) {
        deleteInternal(r);
    }

    public List<CarRental> getRentalsByPeriod(LocalDate start, LocalDate end) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM CarRental cr " +
                                    "WHERE cr.pickupDate >= :start AND cr.returnDate <= :end " +
                                    "ORDER BY cr.pickupDate DESC",
                            CarRental.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .list();
        }
    }

    /* ================= Logic bên trong ================= */

    private void saveInternal(CarRental r) {
        if (r == null) {
            throw new IllegalArgumentException("Rental is null");
        }
        if (r.getCustomer() == null) {
            throw new IllegalArgumentException("Customer is required");
        }
        if (r.getCar() == null) {
            throw new IllegalArgumentException("Car is required");
        }
        if (r.getPickupDate() == null || r.getReturnDate() == null) {
            throw new IllegalArgumentException("Pickup and return date are required");
        }
        if (r.getReturnDate().isBefore(r.getPickupDate())) {
            throw new IllegalArgumentException("Return date must be after pickup date");
        }
        if (r.getRentPrice() == null || r.getRentPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Rent price is invalid");
        }
        if (r.getStatus() == null || r.getStatus().isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Đảm bảo Customer & Car là entity nằm trong Session hiện tại
            Customer dbCus = session.get(Customer.class, r.getCustomer().getCustomerId());
            Car dbCar = session.get(Car.class, r.getCar().getCarId());
            if (dbCus == null) {
                throw new IllegalArgumentException("Customer not found");
            }
            if (dbCar == null) {
                throw new IllegalArgumentException("Car not found");
            }

            if (r.getRentalId() != null) {
                // UPDATE: load từ DB rồi gán field
                CarRental dbRental = session.get(CarRental.class, r.getRentalId());
                if (dbRental == null) {
                    throw new IllegalArgumentException("Rental not found");
                }

                dbRental.setCustomer(dbCus);
                dbRental.setCar(dbCar);
                dbRental.setPickupDate(r.getPickupDate());
                dbRental.setReturnDate(r.getReturnDate());
                dbRental.setRentPrice(r.getRentPrice());
                dbRental.setStatus(r.getStatus());

                session.update(dbRental);

            } else {
                // INSERT
                CarRental newRental = new CarRental();
                newRental.setCustomer(dbCus);
                newRental.setCar(dbCar);
                newRental.setPickupDate(r.getPickupDate());
                newRental.setReturnDate(r.getReturnDate());
                newRental.setRentPrice(r.getRentPrice());
                newRental.setStatus(r.getStatus());

                session.save(newRental);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    private void deleteInternal(CarRental r) {
        if (r == null || r.getRentalId() == null) {
            throw new IllegalArgumentException("Please select a rental to delete");
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            CarRental dbRental = session.get(CarRental.class, r.getRentalId());
            if (dbRental == null) {
                throw new IllegalArgumentException("Rental not found");
            }

            session.delete(dbRental);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}

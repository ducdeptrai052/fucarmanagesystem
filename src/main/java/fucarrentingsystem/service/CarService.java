package fucarrentingsystem.service;

import fucarrentingsystem.config.HibernateUtil;
import fucarrentingsystem.entity.Car;
import fucarrentingsystem.entity.CarRental;
import org.hibernate.Session;

import java.util.List;

public class CarService extends GenericService<Car> {

    public CarService() {
        super(Car.class);
    }

    public List<Car> getAllCars() {
        return findAll();
    }
    public void save(Car car) {
        saveOrUpdate(car);
    }

    public void delete(Car car) {
        deleteCar(car);
    }


    public void saveCar(Car car) {
        saveOrUpdate(car);
    }

    public void deleteCar(Car car) {
        // If car already has rentals -> only set Inactive
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(r) from CarRental r where r.car = :car", Long.class)
                    .setParameter("car", car)
                    .getSingleResult();
            if (count != null && count > 0) {
                car.setStatus("Inactive");
                saveOrUpdate(car);
            } else {
                delete(car);
            }
        }
    }
}

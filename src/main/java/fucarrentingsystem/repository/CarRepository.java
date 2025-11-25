package fucarrentingsystem.repository;

import fucarrentingsystem.entity.Car;

public class CarRepository extends GenericRepository<Car> {

    public CarRepository() {
        super(Car.class);
    }
}

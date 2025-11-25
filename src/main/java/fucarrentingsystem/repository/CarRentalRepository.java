package fucarrentingsystem.repository;

import fucarrentingsystem.entity.CarRental;

public class CarRentalRepository extends GenericRepository<CarRental> {

    public CarRentalRepository() {
        super(CarRental.class);
    }
}

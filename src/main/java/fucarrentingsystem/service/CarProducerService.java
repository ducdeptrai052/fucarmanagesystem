package fucarrentingsystem.service;

import fucarrentingsystem.entity.CarProducer;

import java.util.List;

public class CarProducerService extends GenericService<CarProducer> {

    public CarProducerService() {
        super(CarProducer.class);
    }

    public List<CarProducer> getAllProducers() {
        return findAll();
    }
}

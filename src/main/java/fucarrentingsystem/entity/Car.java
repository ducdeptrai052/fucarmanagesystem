package fucarrentingsystem.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CarID")
    private Integer carId;

    @Column(name = "CarName", nullable = false, length = 100)
    private String carName;

    @Column(name = "CarModelYear", nullable = false)
    private Integer carModelYear;

    @Column(name = "Color", nullable = false, length = 50)
    private String color;

    @Column(name = "Capacity", nullable = false)
    private Integer capacity;

    @Column(name = "Description", nullable = false, length = 500)
    private String description;

    @Column(name = "ImportDate", nullable = false)
    private LocalDate importDate;

    @Column(name = "RentPrice", nullable = false, precision = 18, scale = 2)
    private BigDecimal rentPrice;

    @Column(name = "Status", nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "ProducerID", nullable = false)
    private CarProducer producer;

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Integer getCarModelYear() {
        return carModelYear;
    }

    public void setCarModelYear(Integer carModelYear) {
        this.carModelYear = carModelYear;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public BigDecimal getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(BigDecimal rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CarProducer getProducer() {
        return producer;
    }

    public void setProducer(CarProducer producer) {
        this.producer = producer;
    }

    @Override
    public String toString() {
        return carName;
    }
}

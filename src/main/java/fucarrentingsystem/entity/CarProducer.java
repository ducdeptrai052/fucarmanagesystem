package fucarrentingsystem.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CarProducer")
public class CarProducer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProducerID")
    private Integer producerId;

    @Column(name = "ProducerName", nullable = false, length = 100)
    private String producerName;

    @Column(name = "Address", nullable = false, length = 200)
    private String address;

    @Column(name = "Country", nullable = false, length = 50)
    private String country;

    @OneToMany(mappedBy = "producer")
    private Set<Car> cars = new HashSet<>();

    public Integer getProducerId() {
        return producerId;
    }

    public void setProducerId(Integer producerId) {
        this.producerId = producerId;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        return producerName;
    }
}

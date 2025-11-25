package fucarrentingsystem.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")
    private Integer customerId;

    @Column(name = "CustomerName", nullable = false, length = 100)
    private String customerName;

    @Column(name = "Mobile", nullable = false, length = 20)
    private String mobile;

    @Column(name = "Birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "IdentityCard", nullable = false, length = 50)
    private String identityCard;

    @Column(name = "LicenceNumber", nullable = false, length = 50)
    private String licenceNumber;

    @Column(name = "LicenceDate", nullable = false)
    private LocalDate licenceDate;

    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Column(name = "Password", nullable = false, length = 200)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "AccountID")
    private Account account;


    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public LocalDate getLicenceDate() {
        return licenceDate;
    }

    public void setLicenceDate(LocalDate licenceDate) {
        this.licenceDate = licenceDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return customerName;
    }
}

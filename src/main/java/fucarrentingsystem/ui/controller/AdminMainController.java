package fucarrentingsystem.ui.controller;

import fucarrentingsystem.entity.*;
import fucarrentingsystem.repository.CarProducerRepository;
import fucarrentingsystem.service.CarRentalService;
import fucarrentingsystem.service.CarService;
import fucarrentingsystem.service.CustomerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminMainController {

    private Account loggedInAccount;

    private final CarService carService = new CarService();
    private final CustomerService customerService = new CustomerService();
    private final CarRentalService rentalService = new CarRentalService();
    private final CarProducerRepository producerRepository = new CarProducerRepository();

    // Dashboard
    @FXML private PieChart pieStatusChart;

    // ==== Car Tab ====
    @FXML private TableView<Car> tblCars;
    @FXML private TableColumn<Car, Integer> colCarId;
    @FXML private TableColumn<Car, String> colCarName;
    @FXML private TableColumn<Car, String> colCarColor;
    @FXML private TableColumn<Car, BigDecimal> colCarPrice;
    @FXML private TableColumn<Car, String> colCarStatus;

    @FXML private TextField txtCarName;
    @FXML private TextField txtCarModelYear;
    @FXML private TextField txtCarColor;
    @FXML private TextField txtCarCapacity;
    @FXML private TextField txtCarDescription;
    @FXML private TextField txtCarRentPrice;
    @FXML private ComboBox<CarProducer> cbCarProducer;
    @FXML private ComboBox<String> cbCarStatus;

    // ==== Customer Tab ====
    @FXML private TableView<Customer> tblCustomers;
    @FXML private TableColumn<Customer, Integer> colCustomerId;
    @FXML private TableColumn<Customer, String> colCustomerName;
    @FXML private TableColumn<Customer, String> colCustomerMobile;

    @FXML private TextField txtCustomerName;
    @FXML private TextField txtCustomerMobile;
    @FXML private DatePicker dpCustomerBirthday;
    @FXML private TextField txtCustomerIdentityCard;
    @FXML private TextField txtCustomerLicenceNumber;
    @FXML private DatePicker dpCustomerLicenceDate;
    @FXML private TextField txtCustomerEmail;
    @FXML private PasswordField txtCustomerPassword;

    // ==== Rental Tab ====
    @FXML private TableView<CarRental> tblRentals;
    @FXML private TableColumn<CarRental, Integer> colRentalId;
    @FXML private TableColumn<CarRental, String> colRentalCustomer;
    @FXML private TableColumn<CarRental, String> colRentalCar;
    @FXML private TableColumn<CarRental, LocalDate> colPickupDate;
    @FXML private TableColumn<CarRental, LocalDate> colReturnDate;
    @FXML private TableColumn<CarRental, BigDecimal> colRentPrice;
    @FXML private TableColumn<CarRental, String> colRentalStatus;

    @FXML private ComboBox<Customer> cbRentalCustomer;
    @FXML private ComboBox<Car> cbRentalCar;
    @FXML private DatePicker dpPickupDate;
    @FXML private DatePicker dpReturnDate;
    @FXML private TextField txtRentalPrice;
    @FXML private ComboBox<String> cbRentalStatus;

    // ==== Report Tab ====
    @FXML private DatePicker dpReportStart;
    @FXML private DatePicker dpReportEnd;
    @FXML private TableView<CarRental> tblReport;
    @FXML private TableColumn<CarRental, Integer> colReportRentalId;
    @FXML private TableColumn<CarRental, String> colReportCustomer;
    @FXML private TableColumn<CarRental, String> colReportCar;
    @FXML private TableColumn<CarRental, LocalDate> colReportPickup;
    @FXML private TableColumn<CarRental, LocalDate> colReportReturn;
    @FXML private TableColumn<CarRental, BigDecimal> colReportPrice;

    // Setter khi login
    public void setLoggedInAccount(Account account) {
        this.loggedInAccount = account;
    }

    @FXML
    public void initialize() {
        initDashboardTab();
        initCarTab();
        initCustomerTab();
        initRentalTab();
        initReportTab();
    }

    /* ===========================================================
                                DASHBOARD
       =========================================================== */

    private void initDashboardTab() {
        refreshDashboardChart();
    }

    private void refreshDashboardChart() {
        List<CarRental> rentals = rentalService.getAllRentals();
        Map<String, Long> countByStatus = rentals.stream()
                .collect(Collectors.groupingBy(CarRental::getStatus, Collectors.counting()));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        countByStatus.forEach((k, v) -> pieData.add(new PieChart.Data(k, v)));

        pieStatusChart.setData(pieData);
        pieStatusChart.setTitle("Rentals by Status");
    }

    private void updateDashboardAfterChange() {
        refreshDashboardChart();
    }

    /* ===========================================================
                                MANAGE CAR
       =========================================================== */

    private void initCarTab() {
        colCarId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getCarId()).asObject()
        );
        colCarName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCarName()));
        colCarColor.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getColor()));
        colCarPrice.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getRentPrice()));
        colCarStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        cbCarStatus.setItems(FXCollections.observableArrayList("Available", "Rented", "Inactive"));
        cbCarProducer.setItems(FXCollections.observableArrayList(producerRepository.findAll()));

        refreshCarTable();

        tblCars.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showCarDetails(newSel));
    }

    private void refreshCarTable() {
        tblCars.setItems(FXCollections.observableArrayList(carService.getAllCars()));
    }

    private void showCarDetails(Car car) {
        if (car == null) return;

        txtCarName.setText(car.getCarName());
        txtCarModelYear.setText(String.valueOf(car.getCarModelYear()));
        txtCarColor.setText(car.getColor());
        txtCarCapacity.setText(String.valueOf(car.getCapacity()));
        txtCarDescription.setText(car.getDescription());
        txtCarRentPrice.setText(car.getRentPrice().toPlainString());
        cbCarProducer.getSelectionModel().select(car.getProducer());
        cbCarStatus.getSelectionModel().select(car.getStatus());
    }

    @FXML
    private void handleCarSave() {
        try {
            Car selected = tblCars.getSelectionModel().getSelectedItem();
            Car car = selected != null ? selected : new Car();

            car.setCarName(txtCarName.getText().trim());
            car.setCarModelYear(Integer.parseInt(txtCarModelYear.getText().trim()));
            car.setColor(txtCarColor.getText().trim());
            car.setCapacity(Integer.parseInt(txtCarCapacity.getText().trim()));
            car.setDescription(txtCarDescription.getText().trim());
            car.setRentPrice(new BigDecimal(txtCarRentPrice.getText().trim()));
            car.setProducer(cbCarProducer.getSelectionModel().getSelectedItem());
            car.setStatus(cbCarStatus.getSelectionModel().getSelectedItem());

            if (car.getImportDate() == null) {
                car.setImportDate(LocalDate.now());
            }

            carService.save(car);
            refreshCarTable();
            clearCarForm();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleCarNew() {
        tblCars.getSelectionModel().clearSelection();
        clearCarForm();
    }

    @FXML
    private void handleCarDelete() {
        Car selected = tblCars.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a car to delete");
            return;
        }

        try {
            carService.delete(selected);
            refreshCarTable();
            clearCarForm();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void clearCarForm() {
        txtCarName.clear();
        txtCarModelYear.clear();
        txtCarColor.clear();
        txtCarCapacity.clear();
        txtCarDescription.clear();
        txtCarRentPrice.clear();
        cbCarProducer.getSelectionModel().clearSelection();
        cbCarStatus.getSelectionModel().clearSelection();
    }

    /* ===========================================================
                               MANAGE CUSTOMER
       =========================================================== */

    private void initCustomerTab() {
        colCustomerId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getCustomerId()).asObject()
        );
        colCustomerName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCustomerName()));
        colCustomerMobile.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getMobile()));

        refreshCustomerTable();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showCustomerDetails(newSel));
    }

    private void refreshCustomerTable() {
        tblCustomers.setItems(FXCollections.observableArrayList(customerService.getAllCustomers()));
    }

    private void showCustomerDetails(Customer c) {
        if (c == null) return;

        txtCustomerName.setText(c.getCustomerName());
        txtCustomerMobile.setText(c.getMobile());
        dpCustomerBirthday.setValue(c.getBirthday());
        txtCustomerIdentityCard.setText(c.getIdentityCard());
        txtCustomerLicenceNumber.setText(c.getLicenceNumber());
        dpCustomerLicenceDate.setValue(c.getLicenceDate());
        txtCustomerEmail.setText(c.getEmail());
        txtCustomerPassword.setText(c.getPassword());
    }

    @FXML
    private void handleCustomerSave() {
        try {
            Customer selected = tblCustomers.getSelectionModel().getSelectedItem();
            Customer c = selected != null ? selected : new Customer();

            c.setCustomerName(txtCustomerName.getText().trim());
            c.setMobile(txtCustomerMobile.getText().trim());
            c.setBirthday(dpCustomerBirthday.getValue());
            c.setIdentityCard(txtCustomerIdentityCard.getText().trim());
            c.setLicenceNumber(txtCustomerLicenceNumber.getText().trim());
            c.setLicenceDate(dpCustomerLicenceDate.getValue());
            c.setEmail(txtCustomerEmail.getText().trim());
            c.setPassword(txtCustomerPassword.getText());

            customerService.save(c);
            refreshCustomerTable();
            refreshRentalCombos();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleCustomerNew() {
        tblCustomers.getSelectionModel().clearSelection();
        clearCustomerForm();
    }

    @FXML
    private void handleCustomerDelete() {
        Customer selected = tblCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a customer to delete");
            return;
        }

        try {
            customerService.delete(selected);
            refreshCustomerTable();
            refreshRentalCombos();
            clearCustomerForm();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void clearCustomerForm() {
        txtCustomerName.clear();
        txtCustomerMobile.clear();
        dpCustomerBirthday.setValue(null);
        txtCustomerIdentityCard.clear();
        txtCustomerLicenceNumber.clear();
        dpCustomerLicenceDate.setValue(null);
        txtCustomerEmail.clear();
        txtCustomerPassword.clear();
    }

    /* ===========================================================
                                 MANAGE RENTAL
       =========================================================== */

    private void initRentalTab() {
        colRentalId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getRentalId()).asObject()
        );
        colRentalCustomer.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCustomer().getCustomerName()));
        colRentalCar.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCar().getCarName()));
        colPickupDate.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getPickupDate()));
        colReturnDate.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getReturnDate()));
        colRentPrice.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getRentPrice()));
        colRentalStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getStatus()));

        cbRentalStatus.setItems(FXCollections.observableArrayList("Active", "Completed", "Canceled"));

        refreshRentalCombos();
        refreshRentalTable();

        tblRentals.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showRentalDetails(newSel));
    }

    private void refreshRentalCombos() {
        if (cbRentalCustomer != null)
            cbRentalCustomer.setItems(FXCollections.observableArrayList(customerService.getAllCustomers()));

        if (cbRentalCar != null)
            cbRentalCar.setItems(FXCollections.observableArrayList(carService.getAllCars()));
    }

    private void refreshRentalTable() {
        tblRentals.setItems(FXCollections.observableArrayList(rentalService.getAllRentals()));
    }

    private void showRentalDetails(CarRental r) {
        if (r == null) return;

        cbRentalCustomer.getSelectionModel().select(r.getCustomer());
        cbRentalCar.getSelectionModel().select(r.getCar());
        dpPickupDate.setValue(r.getPickupDate());
        dpReturnDate.setValue(r.getReturnDate());
        txtRentalPrice.setText(r.getRentPrice().toPlainString());
        cbRentalStatus.getSelectionModel().select(r.getStatus());
    }

    @FXML
    private void handleRentalSave() {
        try {
            CarRental selected = tblRentals.getSelectionModel().getSelectedItem();
            CarRental r = selected != null ? selected : new CarRental();

            r.setCustomer(cbRentalCustomer.getSelectionModel().getSelectedItem());
            r.setCar(cbRentalCar.getSelectionModel().getSelectedItem());
            r.setPickupDate(dpPickupDate.getValue());
            r.setReturnDate(dpReturnDate.getValue());
            r.setRentPrice(new BigDecimal(txtRentalPrice.getText().trim()));
            r.setStatus(cbRentalStatus.getSelectionModel().getSelectedItem());

            rentalService.save(r);
            refreshRentalTable();
            refreshReportTable();
            updateDashboardAfterChange();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleRentalNew() {
        tblRentals.getSelectionModel().clearSelection();
        clearRentalForm();
    }

    @FXML
    private void handleRentalDelete() {
        CarRental selected = tblRentals.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a rental to delete");
            return;
        }

        try {
            rentalService.delete(selected);
            refreshRentalTable();
            refreshReportTable();
            clearRentalForm();
            updateDashboardAfterChange();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void clearRentalForm() {
        cbRentalCustomer.getSelectionModel().clearSelection();
        cbRentalCar.getSelectionModel().clearSelection();
        dpPickupDate.setValue(null);
        dpReturnDate.setValue(null);
        txtRentalPrice.clear();
        cbRentalStatus.getSelectionModel().clearSelection();
    }

    /* ===========================================================
                                   REPORT TAB
       =========================================================== */

    private void initReportTab() {
        colReportRentalId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getRentalId()).asObject()
        );
        colReportCustomer.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCustomer().getCustomerName()));
        colReportCar.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCar().getCarName()));
        colReportPickup.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPickupDate()));
        colReportReturn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getReturnDate()));
        colReportPrice.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getRentPrice()));
    }

    @FXML
    private void handleGenerateReport() {
        if (dpReportStart.getValue() == null || dpReportEnd.getValue() == null) {
            showError("Please select start and end date");
            return;
        }
        refreshReportTable();
    }

    private void refreshReportTable() {
        if (dpReportStart.getValue() == null || dpReportEnd.getValue() == null) {
            tblReport.setItems(FXCollections.observableArrayList());
            return;
        }

        tblReport.setItems(FXCollections.observableArrayList(
                rentalService.getRentalsByPeriod(
                        dpReportStart.getValue(), dpReportEnd.getValue()
                )
        ));
    }

    /* ===========================================================
                                   LOGOUT
       =========================================================== */

    @FXML
    private void handleLogout(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FU Car Renting System - Login");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Cannot logout: " + e.getMessage());
        }
    }

    /* ===========================================================
                                   UTIL
       =========================================================== */

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

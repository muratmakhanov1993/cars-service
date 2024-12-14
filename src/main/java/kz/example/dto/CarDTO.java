package kz.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public class CarDTO {

    private UUID id;

    @NotBlank(message = "Make must not be empty")
    private String make;

    @NotBlank(message = "Model must not be empty")
    private String model;

    @Min(value = 1986, message = "Year must not be earlier than 1886")
    private int year;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be a positive number")
    private BigDecimal price;

    @Size(min = 17, max = 17, message = "VIN must be contains 17 symbols")
    private String vin;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}

package model;

import java.io.Serializable;

public class Promotion implements Serializable {
    private String code;
    private double discount;
    private String description;

    public Promotion(String code, double discount, String description) {
        this.code = code;
        this.discount = discount;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return code + " - " + description;
    }
}

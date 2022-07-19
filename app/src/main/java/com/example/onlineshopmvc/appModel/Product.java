package com.example.onlineshopmvc.appModel;

public class Product {
    private int codeId;
    private String title;
    private String description;
    private Double pries;
    private int stock;

    public Product(int codeId, String title, String description, Double pries, int stock) {

        this.codeId = codeId;
        this.title = title;
        this.description = description;
        this.pries = pries;
        this.stock = stock;
    }

    public int getCodeId() {
        return this.codeId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getPries() {
        return pries;
    }

    public int getStock() {
        return stock;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPries(Double pries) {
        this.pries = pries;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product [" +
                " codeId : " + codeId +
                " , title : " + title +
                " , description : " + description +
                " , pries : " + pries + "€" +
                " , stock : " + stock +
                " ]";
    }
}

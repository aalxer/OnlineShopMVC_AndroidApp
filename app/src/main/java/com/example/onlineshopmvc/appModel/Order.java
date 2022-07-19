package com.example.onlineshopmvc.appModel;

import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Serializable {
    private int orderID;
    private LocalDate date;
    private OrderState state;
    private double totalPries;
    private ArrayList<Integer> orderedProducts;
    private String customerName;
    private String firstLineAddress;
    private String city;
    private int postcode;
    private String email;

    public Order(int orderID, LocalDate date, String customerName, OrderState state, double totalPries, ArrayList<Integer> orderedProducts, String firstLineAddress, String city, int postcode, String email) {
        this.orderID = orderID;
        this.date = date;
        this.customerName = customerName;
        this.state = state;
        this.totalPries = totalPries;
        this.orderedProducts = orderedProducts;
        this.firstLineAddress = firstLineAddress;
        this.city = city;
        this.postcode = postcode;
        this.email = email;
    }

    public int getOrderID() {
        return orderID;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public OrderState getState() {
        return state;
    }

    public double getTotalPries() {
        return totalPries;
    }

    public ArrayList<Integer> getOrderedProducts() {
        return orderedProducts;
    }

    public String getFirstLineAddress() {
        return firstLineAddress;
    }

    public String getCity() {
        return city;
    }

    public int getPostcode() {
        return postcode;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Order [" +
                "orderID : " + orderID +
                ", date : " + date +
                ", customerName : " + customerName +
                ", state : " + state +
                ", totalPries : " + totalPries +
                ", orderedProducts : " + orderedProducts +
                ", firstLineAddress : " + firstLineAddress +
                ", city : " + city +
                ", postcode : " + postcode +
                ", email : " + email +
                " ]";
    }
}
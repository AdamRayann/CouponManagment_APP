package com.example.coupon_managment_app;

import java.io.Serializable;
import java.util.Date;

public class Coupon implements Serializable {
    private int id;
    private int companyId;
    private Category category;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int amount;
    private Double price;
    private String image;

    public Coupon(int id, int companyId, Category categoryId, String title, String description, Date start_Date, Date end_Date,int amount, Double price, String image) {
        this.id = id;
        this.companyId = companyId;
        this.category = categoryId;
        this.title = title;
        this.description = description;
        startDate = start_Date;
        endDate = end_Date;
        this.amount=amount;
        this.price = price;
        this.image = image;
    }

    public Coupon(int companyId, Category categoryId, String title, String description, Date start_Date, Date end_Date,int amount, Double price, String image) {
        this.companyId = companyId;
        this.category = categoryId;
        this.title = title;
        this.description = description;
        startDate = start_Date;
        endDate = end_Date;
        this.amount=amount;
        this.price = price;
        this.image = image;
    }

    public Coupon() {

    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public Double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public void setCategoryId(Category categoryId) {
        this.category = categoryId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public void setPrice(Double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

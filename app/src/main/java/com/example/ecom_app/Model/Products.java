package com.example.ecom_app.Model;

public class Products {
    private String category, data, description, pid, pname, price, time, image;

    public Products(String category, String data, String description, String pid, String pname, String price, String time, String image) {
        this.category = category;
        this.data = data;
        this.description = description;
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.time = time;
        this.image = image;
    }
    public Products(){

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

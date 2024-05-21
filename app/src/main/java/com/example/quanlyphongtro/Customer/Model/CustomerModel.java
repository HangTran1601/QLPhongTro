package com.example.quanlyphongtro.Customer.Model;

public class CustomerModel {
   private String name;
   private String cccd;
   private String phone;
   private String address;
   private String job;
    public  CustomerModel(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public CustomerModel(String name, String cccd, String phone, String address, String job) {
        this.name = name;
        this.cccd = cccd;
        this.phone = phone;
        this.address = address;
        this.job = job;
    }
}

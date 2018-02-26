package com.example.rentit;

import okio.Buffer;

/**
 * Created by aashish on 6/2/18.
 */

public class Rent {

    public String city,address,phone,description;
    public Rent(){

    }
    public Rent(String city, String address, String phone, String description){
        this.address=address;
        this.city=city;
        this.description=description;
        this.phone=phone;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {

        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }
}

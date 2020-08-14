package com.robin.mdpanel;

public class MerchantRequest {

    private String name,phone,email,businessname,businessdetails,businessaddress,bkash,rocket,id,status,pickUp_district,pickUp_thana,address_details;

    public MerchantRequest(){

    }

    public MerchantRequest(String name, String phone, String email, String businessname, String businessdetails, String businessaddress, String bkash, String rocket, String id, String status, String pickUp_district, String pickUp_thana, String address_details) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.businessname = businessname;
        this.businessdetails = businessdetails;
        this.businessaddress = businessaddress;
        this.bkash = bkash;
        this.rocket = rocket;
        this.id = id;
        this.status = status;
        this.pickUp_district = pickUp_district;
        this.pickUp_thana = pickUp_thana;
        this.address_details = address_details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getBusinessdetails() {
        return businessdetails;
    }

    public void setBusinessdetails(String businessdetails) {
        this.businessdetails = businessdetails;
    }

    public String getBusinessaddress() {
        return businessaddress;
    }

    public void setBusinessaddress(String businessaddress) {
        this.businessaddress = businessaddress;
    }

    public String getBkash() {
        return bkash;
    }

    public void setBkash(String bkash) {
        this.bkash = bkash;
    }

    public String getRocket() {
        return rocket;
    }

    public void setRocket(String rocket) {
        this.rocket = rocket;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPickUp_district() {
        return pickUp_district;
    }

    public void setPickUp_district(String pickUp_district) {
        this.pickUp_district = pickUp_district;
    }

    public String getPickUp_thana() {
        return pickUp_thana;
    }

    public void setPickUp_thana(String pickUp_thana) {
        this.pickUp_thana = pickUp_thana;
    }

    public String getAddress_details() {
        return address_details;
    }

    public void setAddress_details(String address_details) {
        this.address_details = address_details;
    }
}

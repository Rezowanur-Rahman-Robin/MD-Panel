package com.robin.mdpanel;

public class OM {

    private String name,email,phone,password,district,om_id,n_id;

    public OM(){

    }

    public OM(String name, String email, String phone, String password, String district, String om_id, String n_id) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.district = district;
        this.om_id = om_id;
        this.n_id = n_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getOm_id() {
        return om_id;
    }

    public void setOm_id(String om_id) {
        this.om_id = om_id;
    }

    public String getN_id() {
        return n_id;
    }

    public void setN_id(String n_id) {
        this.n_id = n_id;
    }
}

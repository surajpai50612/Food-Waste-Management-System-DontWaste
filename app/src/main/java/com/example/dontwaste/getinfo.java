package com.example.dontwaste;

public class getinfo
{

    String orgName;
    String regName;
    String Address;
    String Quantity;
    String Food;
    String Date;
    String Time;
    String keyid;
    String phone;
    String email;
    String image;


    getinfo()
    {

    }

    public getinfo(String orgname, String keyid, String regName, String address, String quantity, String Food, String date, String time,String phone,String image,String email) {
        this.orgName = orgname;
        this.regName = regName;
        this.Address = address;
        this.Quantity = quantity;
        this.Food = Food;
        this.Date = date;
        this.Time = time;
        this.keyid = keyid;
        this.phone = phone;
        this.email = email;
        this.image=image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getOrgname() {
        return orgName;
    }

    public void setOrgname(String orgname) {
        this.orgName = orgname;
    }

    public String getRegName() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getFood() {
        return Food;
    }

    public void setFood(String Food) {
        this.Food = Food;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public  String getPhone() { return  phone;}

    public  void setPhone(String phone){this.phone = phone;}
}
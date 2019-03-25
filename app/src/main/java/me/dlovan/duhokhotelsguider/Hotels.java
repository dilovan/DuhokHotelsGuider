package me.dlovan.duhokhotelsguider;

import java.util.ArrayList;
import java.util.List;

public class Hotels {

    private int ID;
    private String Name;
    private String Address;
    private String Phone;
    private String Desc;
    private Double Lat;
    private Double Lng;
    //private String[] Images;
    public ArrayList<String> Images;

    public Hotels() {
        Images = new ArrayList();
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
    public String getImages(int index) {
        return Images.get(index);
    }

    public void setImages(ArrayList<String> image) {
            Images =  image;
    }
}

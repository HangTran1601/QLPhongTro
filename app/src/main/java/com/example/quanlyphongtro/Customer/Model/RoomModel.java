package com.example.quanlyphongtro.Customer.Model;

public class RoomModel {
    private int id;
    private String roomName;
    private String roomDescription;
    private String roomPrice;
    private String roomElectronic;
    private String roomWater;
    private String roomAddress;
    private String roomArea;
    private String status;
    private String image;
    public RoomModel(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomElectronic() {
        return roomElectronic;
    }

    public void setRoomElectronic(String roomElectronic) {
        this.roomElectronic = roomElectronic;
    }

    public String getRoomWater() {
        return roomWater;
    }

    public void setRoomWater(String roomWater) {
        this.roomWater = roomWater;
    }

    public String getRoomAddress() {
        return roomAddress;
    }

    public void setRoomAddress(String roomAddress) {
        this.roomAddress = roomAddress;
    }

    public String getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(String roomArea) {
        this.roomArea = roomArea;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public RoomModel(int id, String roomName, String roomDescription, String roomPrice, String roomElectronic, String roomWater, String roomAddress, String roomArea, String status, String image) {
        this.id = id;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.roomPrice = roomPrice;
        this.roomElectronic = roomElectronic;
        this.roomWater = roomWater;
        this.roomAddress = roomAddress;
        this.roomArea = roomArea;
        this.status = status;
        this.image = image;
    }
    public RoomModel(String roomName, String roomDescription, String roomPrice, String roomElectronic, String roomWater, String roomAddress, String roomArea, String status, String image) {
        this.id = id;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.roomPrice = roomPrice;
        this.roomElectronic = roomElectronic;
        this.roomWater = roomWater;
        this.roomAddress = roomAddress;
        this.roomArea = roomArea;
        this.status = status;
        this.image = image;
    }
}

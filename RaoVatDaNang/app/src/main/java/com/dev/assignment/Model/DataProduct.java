package com.dev.assignment.Model;

public class DataProduct {
    private int productId;
    private String name;
    private int price;
    private String type1;
    private String type2;
    private String detail;
    private String status;
    private int lockPr;
    private int idUser;
    private String image_1;
    private String image_2;

    public DataProduct() {
    }

    public DataProduct(int productId, String name, int price, String type1, String type2, String detail, String status, int lockPr, int idUser, String image_1, String image_2) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type1 = type1;
        this.type2 = type2;
        this.detail = detail;
        this.status = status;
        this.lockPr = lockPr;
        this.idUser = idUser;
        this.image_1 = image_1;
        this.image_2 = image_2;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLockPr() {
        return lockPr;
    }

    public void setLockPr(int lockPr) {
        this.lockPr = lockPr;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getImage_2() {
        return image_2;
    }

    public void setImage_2(String image_2) {
        this.image_2 = image_2;
    }

    @Override
    public String toString() {
        return "DataProduct{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type1='" + type1 + '\'' +
                ", type2='" + type2 + '\'' +
                ", detail='" + detail + '\'' +
                ", status='" + status + '\'' +
                ", lockPr=" + lockPr +
                ", idUser=" + idUser +
                ", image_1='" + image_1 + '\'' +
                ", image_2='" + image_2 + '\'' +
                '}';
    }
}



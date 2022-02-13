package com.dev.assignment.Model;

public class ListFavorite {
    int idFavorites;
    String productId;
    String name;
    int price;
    String image;
    int idUser;


    public ListFavorite() {

    }

    public ListFavorite(int idFavorites, String productId, String name, int price, String image, int idUser) {
        this.idFavorites = idFavorites;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.idUser = idUser;
    }

    public int getIdFavorites() {
        return idFavorites;
    }

    public void setIdFavorites(int idFavorites) {
        this.idFavorites = idFavorites;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
package com.dev.assignment.Model;

public class ListReport {

    int id_lockPr;
    int productId;
    String list_idUser;
    String dateRP;

    public ListReport(int id_lockPr, int productId, String list_idUser, String dateRP) {
        this.id_lockPr = id_lockPr;
        this.productId = productId;
        this.list_idUser = list_idUser;
        this.dateRP = dateRP;
    }

    public ListReport() {

    }

    public int getId_lockPr() {
        return id_lockPr;
    }

    public void setId_lockPr(int id_lockPr) {
        this.id_lockPr = id_lockPr;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getList_idUser() {
        return list_idUser;
    }

    public void setList_idUser(String list_idUser) {
        this.list_idUser = list_idUser;
    }

    public String getDateRP() {
        return dateRP;
    }

    public void setDateRP(String dateRP) {
        this.dateRP = dateRP;
    }
}
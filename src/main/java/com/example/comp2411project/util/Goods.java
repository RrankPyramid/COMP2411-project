package com.example.comp2411project.util;

import com.example.comp2411project.func.OracleDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Goods implements Table {
    long id;
    long merchantID;
    double price;
    String name;
    int counts;

    public Goods(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(long merchantID) {
        this.merchantID = merchantID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    @Override
    public void pushInfo(){
        OracleDB oracleDB = OracleDB.getInstance();
        oracleDB.getConnection();
        boolean hasValue = oracleDB.existValue("GOODS", "ID", id);
        if(hasValue){
            oracleDB.update("UPDATE GOODS "+
                    "SET MERCHANTID = ?, PRICE = ?, NAME = ?, COUNTS = ? "+
                    "WHERE ID = ?", merchantID, price, name, counts, id);

        }else{
            id = oracleDB.insert("INSERT INTO GOODS(MERCHANTID, PRICE, NAME, COUNTS) VALUES(?, ?, ?, ?)",merchantID, price, name, counts);
        }
        oracleDB.closeConnection();
    }

    @Override
    public void pullUpdate(){
        OracleDB oracleDB = OracleDB.getInstance();
        oracleDB.getConnection();
        try(ResultSet rs = oracleDB.query("SELECT MERCHANTID, PRICE, NAME, COUNTS FROM GOODS WHERE ID = ?", id)){
            if(rs.next()){
                merchantID = rs.getInt(1);
                price = rs.getDouble(2);
                name = rs.getString(3);
                counts = rs.getInt(4);
            }
        }catch (SQLException e){
            System.out.println("pull Error: ");
            while(e != null){
                System.out.println("message: " + e.getMessage());
                e = e.getNextException();
            }
        }
        oracleDB.closeConnection();
    }

}
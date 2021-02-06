package com.example.mybank;

import java.io.Serializable;

public class CustomerallModel implements Serializable {

    String name;

    String account;


    String TransctionId;
    long balance;
     String tranctionmoney;
    String comment_time;




    public CustomerallModel() {
    }


    public CustomerallModel(String name, String account, String tranctionmoney, String comment_time) {
        this.name = name;
        this.account = account;
        this.tranctionmoney = tranctionmoney;
        this.comment_time = comment_time;
    }

    public String getTranctionmoney() {
        return tranctionmoney;
    }

    public void setTranctionmoney(String tranctionmoney) {
        this.tranctionmoney = tranctionmoney;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getTransctionId() {
        return TransctionId;
    }

    public void setTransctionId(String transctionId) {
        TransctionId = transctionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}

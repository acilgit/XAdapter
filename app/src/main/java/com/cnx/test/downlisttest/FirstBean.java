package com.cnx.test.downlisttest;

/**
 * Created by 18953 on 2016/4/3.
 */
public class FirstBean {
    private String name;
    private int type;
    private int ID;


    public FirstBean(int ID, String name, int type) {
        this.ID = ID;
        this.name = name;
        this.type = type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

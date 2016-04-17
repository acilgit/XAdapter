package com.cnx.test.downlisttest;

/**
 * Created by 18953 on 2016/4/3.
 */
public class EventBean {
    private String des;
    private int ID;

    public EventBean(String des, int ID) {
        this.des = des;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

}

/*
    NIM     : 10119234
    NAMA    : ARHAM JUSNI INDRAWAN
    KELAS   : IF-4
 */

package com.example.uas_10119234.ui.dashboard;

import java.io.Serializable;
public class Note {

    String id;
    String title;
    String category;
    String desc;
    String date;

    public Note(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

package com.example.taobao.entity;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

/**
 * @创建人: lzh
 * @创建时间: 2022/3/7
 * @描述:
 */
public class FileInfo {

    private final StringProperty fileName = new SimpleStringProperty();

    private StringProperty softid = new SimpleStringProperty();

    private StringProperty date = new SimpleStringProperty();

    private BooleanProperty free = new SimpleBooleanProperty();

    private StringProperty status = new SimpleStringProperty();

    private StringProperty dir = new SimpleStringProperty();



    public String getDir() {
        return dir.get();
    }

    public StringProperty dirProperty() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir.set(dir);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getSoftid() {
        return softid.get();
    }

    public StringProperty softidProperty() {
        return softid;
    }

    public void setSoftid(String softid) {
        this.softid.set(softid);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public boolean isFree() {
        return free.get();
    }

    public BooleanProperty freeProperty() {
        return free;
    }

    public void setFree(boolean free) {
        this.free.set(free);
    }
}

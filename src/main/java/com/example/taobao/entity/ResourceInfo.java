package com.example.taobao.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @创建人: lzh
 * @创建时间: 2022/2/15
 * @描述: 资源详情
 */
public class ResourceInfo {

    private final StringProperty name = new SimpleStringProperty();

    private final StringProperty title = new SimpleStringProperty();

    private final StringProperty updateSttus = new SimpleStringProperty();

    private final StringProperty id = new SimpleStringProperty();

    private final StringProperty status = new SimpleStringProperty();

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getUpdateSttus() {
        return updateSttus.get();
    }

    public StringProperty updateSttusProperty() {
        return updateSttus;
    }

    public void setUpdateSttus(String updateSttus) {
        this.updateSttus.set(updateSttus);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
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

    @Override
    public String toString() {
        return "ResourceInfo{" +
                "name=" + name.get() +
                ", updateTime=" + updateSttus.get() +
                ", id=" + id.get() +
                ", status=" + status.get() +
                '}';
    }
}

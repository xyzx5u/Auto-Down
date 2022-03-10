package com.example.taobao.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * @创建人: lzh
 * @创建时间: 2021/12/15
 * @描述:
 */
public class AlertController {

    /**
     * 不允许实例化
     */
    private AlertController() {
    }

    /**
     * 提示
     *
     * @param title 标题
     * @param message 内容
     *
     * @return 点击按钮
     */
    public static final Optional<ButtonType> info(String title, String message) {
        return build(title, message, Alert.AlertType.INFORMATION);
    }

    /**
     * 警告
     *
     * @param title 标题
     * @param message 内容
     *
     * @return 点击按钮
     */
    public static final Optional<ButtonType> warn(String title, String message) {
        return build(title, message, Alert.AlertType.WARNING);
    }

    /**
     * 创建提示窗口
     *
     * @param title 标题
     * @param message 内容
     * @param type 类型
     *
     * @return 点击按钮
     */
    public static final Optional<ButtonType> build(String title, String message, Alert.AlertType type) {
        final Alert alert = new Alert(type);
        final Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/image/favicon.ico"));
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }


}

package com.example.taobao.controller;

import com.example.taobao.entity.FileInfo;
import com.example.taobao.entity.HttpResult;
import com.example.taobao.entity.Lable;
import com.example.taobao.entity.LoginInfo;
import com.example.taobao.utils.ChromiumUtil;
import com.example.taobao.utils.HttpUtils;
import com.example.taobao.utils.LableUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainController implements Initializable {

    public static ExecutorService executor = new ThreadPoolExecutor(4,20,1000, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());

    //表格数据
    private final ObservableList<FileInfo> data = FXCollections.observableArrayList();
    //资源类型
    private final ObservableList<String> subjectData = FXCollections.observableArrayList();
    //资源种类
    private final ObservableList<String> navigationData = FXCollections.observableArrayList();
    //资源种类
    private final ObservableList<String> filterData = FXCollections.observableArrayList();

    /**
     * 当前选择的文件夹
     */
    private String filePath;

    /**
     * 显示文件夹
     */
    @FXML
    private TextField path;


    //表格
    @FXML
    private TableView<FileInfo> mainTable;

    //是否在运行
    private boolean isRun = false;

    //下拉框
    @FXML
    private ChoiceBox subject;//科目
    @FXML
    private ChoiceBox navigation;//类型
    @FXML
    private ChoiceBox filter;//试卷类型

    private String cookie;//cookie

    private String userId;

    /**
     * 初始化
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //禁止编辑
        path.setEditable(false);
        mainTable.getColumns().add(new TableColumn<>("文件名"){{
            prefWidthProperty().bind(mainTable.widthProperty().multiply(.6));
        }});
        mainTable.getColumns().add(new TableColumn<>("更新时间"){{
            prefWidthProperty().bind(mainTable.widthProperty().multiply(.2));
        }});
        mainTable.getColumns().add(new TableColumn<>("下载状态"){{
            prefWidthProperty().bind(mainTable.widthProperty().multiply(.2));
        }});

        //绑定数据
        ObservableList<TableColumn<FileInfo,?>> prodObservableList = mainTable.getColumns();
        prodObservableList.get(0).setCellValueFactory(new PropertyValueFactory("fileName"));
        prodObservableList.get(1).setCellValueFactory(new PropertyValueFactory("date"));
        prodObservableList.get(2).setCellValueFactory(new PropertyValueFactory("status"));
        mainTable.setItems(data);
        //禁止排序
        prodObservableList.get(0).setSortable(false);
        prodObservableList.get(1).setSortable(false);
        prodObservableList.get(2).setSortable(false);

        //下拉框
        subject.setItems(subjectData);
        navigation.setItems(navigationData);
        filter.setItems(filterData);
        //赋值
        for (Lable lable : LableUtil.subjects) {
            subjectData.add(lable.getName());
        }
        for (Lable lable : LableUtil.navigations) {
            navigationData.add(lable.getName());
        }
        for (Lable lable : LableUtil.filters) {
            filterData.add(lable.getName());
        }
        subject.getSelectionModel().selectFirst();
        navigation.getSelectionModel().selectFirst();
        filter.getSelectionModel().selectFirst();
        filter.setDisable(false);
        navigation.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1.toString().equals("0")){
                    filter.setDisable(false);
                }else{
                    filter.setDisable(true);
                }
            }
        });

    }

    @FXML
    public void start() {
        if (this.filePath == null || this.filePath.isEmpty()){
            AlertController.info("提示","请选择文件夹");
            return;
        }
        if (this.cookie != null && !this.cookie.isEmpty()){
            executor.submit(()->{
                //免费下载
                for (FileInfo datum : data) {
                    //FileInfo datum = data.get(0);
                    try {
                        datum.setStatus("正在下载");
                        //1.获取nbKey
                        String nbKey = HttpUtils.getNbKey(datum.getSoftid(), this.userId);
                        //2.获取下载的js
                        HttpResult result = HttpUtils.getScripts(this.cookie, datum.getSoftid(), nbKey);
                        //3.获取下载地址
                        String url = HttpUtils.getFileUrl(this.cookie + result.getCookie(), "https:" + result.getRespose(),datum.isFree());
                        //4.下载
                        String pathStr = path.getText();
                        Calendar ca1 = Calendar.getInstance();
                        pathStr = pathStr + "/" + DateFormatUtils.format(ca1, "yyyy-MM-dd") + "/" + datum.getDir();
                        File file = new File(pathStr);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        if (HttpUtils.download(url, pathStr)) {
                            datum.setStatus("已下载");
                        }
                    }catch (Exception e){
                        datum.setStatus("下载失败");
                        e.printStackTrace();
                    }
                }
            });
        }else{
            AlertController.info("提示","请登录");
        }
    }

    /**
     * 获取下载的文件
     */
    @FXML
    public void getFile(){
        StringBuilder filePathBuild = new StringBuilder();
        //科目
        Lable choiceSubject = LableUtil.subjects.get(subject.getSelectionModel().getSelectedIndex());
        filePathBuild.append(choiceSubject.getName()).append("/");
        //类型
        Lable choiceNavigation = LableUtil.navigations.get(navigation.getSelectionModel().getSelectedIndex());
        filePathBuild.append(choiceNavigation.getName()).append("/");
        if (choiceNavigation.getName().equals("试卷")){
            //试卷类型
            Lable choiceFilter = LableUtil.filters.get(filter.getSelectionModel().getSelectedIndex());
            filePathBuild.append(choiceFilter.getName()).append("/");
            //试卷类型
            data.addAll(HttpUtils.getFile(choiceSubject.getUrl(),choiceFilter.getUrl(),filePathBuild.toString()));
        }else{
            //其他两个
            data.addAll(HttpUtils.getFile(choiceSubject.getUrl(),choiceNavigation.getUrl(),filePathBuild.toString()));
        }
    }

    @FXML
    public void clear(){
        if (isRun){
            AlertController.info("提示","正在发布");
            return;
        }
        data.clear();
    }

    /**
     * 选择文件夹
     */
    @FXML
    public void importDir(){
        if (isRun){
            AlertController.info("提示","正在发布");
            return;
        }
        Stage stage = new Stage();
        DirectoryChooser directoryChooser=new DirectoryChooser();
        directoryChooser.setTitle("选择文件夹");
        File file = directoryChooser.showDialog(stage);
        if (file!=null){
            this.filePath = file.getAbsolutePath();
            path.setText(this.filePath);
        }
    }

    /**
     * 打开浏览器登录
     */
    @FXML
    public void toLogin(){
        executor.submit(()->{
            //获取cookie
            LoginInfo loginInfo = ChromiumUtil.openChrome();
            this.cookie = loginInfo.getCookies();
            this.userId = loginInfo.getUserId();

            System.out.println("cookie:"+this.cookie);
        });
    }

}

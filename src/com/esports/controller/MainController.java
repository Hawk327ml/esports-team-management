package com.esports.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MainController {
    
    @FXML
    private StackPane contentPane;
    
    @FXML
    private Label lblStatus;
    
    @FXML
    private void initialize() {
        lblStatus.setText("System Ready (系统已就绪)");
        System.out.println("MainController initialized (MainController已初始化)");
    }
    
    @FXML
    private void showPlayerManagement() {
        try {
            lblStatus.setText("Loading player management module... (正在加载选手管理模块...)");
            Parent playerView = FXMLLoader.load(getClass().getResource("/com/esports/view/PlayerView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(playerView);
            lblStatus.setText("Player management module loaded (选手管理模块加载完成)");
        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Failed to load player management module (加载选手管理模块失败)");
            showAlert("Error (错误)", "Unable to load player management interface (无法加载选手管理界面): " + e.getMessage());
        }
    }
    
    // Update showContractManagement method in MainController class (在MainController类中更新showContractManagement方法)
    @FXML
    private void showContractManagement() {
        try {
            lblStatus.setText("Loading contract management module... (正在加载合同管理模块...)");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esports/view/ContractView.fxml"));
            Parent contractView = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(contractView);
            lblStatus.setText("Contract management module loaded (合同管理模块加载完成)");
        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Failed to load contract management module (加载合同管理模块失败)");
            showAlert("Error (错误)", "Unable to load contract management interface (无法加载合同管理界面): " + e.getMessage());
        }
    }
    
    // Add tournament management method in MainController class (在MainController类中添加比赛记录管理方法)
    @FXML
    private void showTournamentManagement() {
        try {
            lblStatus.setText("Loading tournament record module... (正在加载比赛记录模块...)");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esports/view/TournamentView.fxml"));
            Parent tournamentView = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(tournamentView);
            lblStatus.setText("Tournament record module loaded (比赛记录模块加载完成)");
        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Failed to load tournament record module (加载比赛记录模块失败)");
            showAlert("Error (错误)", "Unable to load tournament record interface (无法加载比赛记录界面): " + e.getMessage());
        }
    }
    
    @FXML
    private void showStatistics() {
        lblStatus.setText("Loading data statistics module... (正在加载数据统计模块...)");
        showAlert("Hint (提示)", "Data statistics function under development... (数据统计功能开发中...)");
    }
    
    @FXML
    private void showAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About (关于)");
        alert.setHeaderText("Esports Player Management System (电竞选手管理系统)");
        alert.setContentText("Version (版本): 1.0.0\nDevelopment Team (开发团队): CSC3104 Group\nFunctions (功能): Player Management (选手管理), Contract Management (合同管理), Tournament Record Management (比赛记录管理)");
        alert.showAndWait();
        lblStatus.setText("View about information (查看关于信息)");
    }
    
    @FXML
    private void exitApplication() {
        System.exit(0);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        lblStatus.setText("Ready (就绪)");
    }
}
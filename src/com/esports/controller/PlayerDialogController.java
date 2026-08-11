package com.esports.controller;

import com.esports.dao.PlayerDAO;
import com.esports.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PlayerDialogController {
    
    @FXML private Label dialogTitle;
    @FXML private TextField txtUsername;
    @FXML private TextField txtRealName;
    @FXML private Spinner<Integer> spnAge;
    @FXML private ComboBox<String> cmbPosition;
    @FXML private TextField txtNationality;
    @FXML private DatePicker dpJoinDate;
    @FXML private TextField txtSalary;
    @FXML private ComboBox<String> cmbStatus;
    
    private Stage dialogStage;
    private Player player;
    private boolean okClicked = false;
    private boolean isEditMode = false;
    
    private PlayerDAO playerDAO = new PlayerDAO();
    
    @FXML
    private void initialize() {
        // Configure age Spinner (15-40 years, default 20) (配置年龄Spinner（15-40岁，默认20）)
        SpinnerValueFactory<Integer> ageFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 40, 20);
        spnAge.setValueFactory(ageFactory);
        
        // Set position options (设置位置选项)
        cmbPosition.getItems().addAll("Mid", "ADC", "Top", "Support", "Jungle");
        cmbPosition.setValue("Mid");
        
        // Set status options (设置状态选项)
        cmbStatus.getItems().addAll("Active", "Inactive", "Retired");
        cmbStatus.setValue("Active");
        
        // Set default date as today (设置默认日期为今天)
        dpJoinDate.setValue(LocalDate.now());
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
        this.isEditMode = (player != null && player.getPlayerId() > 0);
        
        if (isEditMode) {
            dialogTitle.setText("Edit Player (编辑选手)");
            // Fill existing data (填充现有数据)
            txtUsername.setText(player.getUsername());
            txtRealName.setText(player.getRealName());
            spnAge.getValueFactory().setValue(player.getAge());
            cmbPosition.setValue(player.getPosition());
            txtNationality.setText(player.getNationality());
            dpJoinDate.setValue(player.getJoinDate());
            txtSalary.setText(player.getSalary() != null ? player.getSalary().toString() : "");
            cmbStatus.setValue(player.getStatus());
        } else {
            dialogTitle.setText("Add New Player (添加新选手)");
            this.player = new Player();
        }
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }
    
    @FXML
    private void handleSave() {
        if (isInputValid()) {
            try {
                // Get data from interface (从界面获取数据)
                player.setUsername(txtUsername.getText().trim());
                player.setRealName(txtRealName.getText().trim());
                player.setAge(spnAge.getValue());
                player.setPosition(cmbPosition.getValue());
                player.setNationality(txtNationality.getText().trim());
                player.setJoinDate(dpJoinDate.getValue());
                
                // Process salary (处理薪资)
                try {
                    BigDecimal salary = new BigDecimal(txtSalary.getText().trim());
                    player.setSalary(salary);
                } catch (NumberFormatException e) {
                    showAlert("Input Error (输入错误)", "Please enter a valid salary number, for example (请输入有效的薪资数字，例如): 1000000.00");
                    return;
                }
                
                player.setStatus(cmbStatus.getValue());
                
                // Save to database (保存到数据库)
                boolean success;
                if (isEditMode) {
                    System.out.println("Updating player (正在更新选手): " + player.getUsername());
                    success = playerDAO.updatePlayer(player);
                } else {
                    System.out.println("Adding player (正在添加选手): " + player.getUsername());
                    success = playerDAO.addPlayer(player);
                }
                
                if (success) {
                    okClicked = true;
                    System.out.println("Player information saved successfully! (选手信息保存成功！)");
                    dialogStage.close();
                } else {
                    showAlert("Save Failed (保存失败)", "Unable to save player information to database (无法保存选手信息到数据库)");
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("System Error (系统错误)", "Error occurred during saving (保存过程中发生错误): " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (txtUsername.getText() == null || txtUsername.getText().trim().isEmpty()) {
            errorMessage.append("Game ID cannot be empty! (游戏ID不能为空！)\n");
        }
        
        if (txtSalary.getText() == null || txtSalary.getText().trim().isEmpty()) {
            errorMessage.append("Monthly salary cannot be empty! (月薪不能为空！)\n");
        } else {
            try {
                new BigDecimal(txtSalary.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage.append("Monthly salary must be a valid number! (月薪必须是有效数字！)\n");
            }
        }
        
        if (dpJoinDate.getValue() == null) {
            errorMessage.append("Please select a join date! (请选择加入日期！)\n");
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            showAlert("Input Validation (输入验证)", errorMessage.toString());
            return false;
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Static method to display dialog (静态方法用于显示对话框)
    public static boolean showPlayerDialog(Player player, Stage parentStage) {
        try {
            // Load FXML (加载FXML)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PlayerDialogController.class.getResource("/com/esports/view/PlayerDialog.fxml"));
            VBox page = loader.load();
            
            // Create dialog stage (创建对话框舞台)
            Stage dialogStage = new Stage();
            dialogStage.setTitle(player != null ? "Edit Player (编辑选手)" : "Add Player (添加选手)");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parentStage);
            dialogStage.setScene(new Scene(page));
            
            // Set controller (设置控制器)
            PlayerDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPlayer(player);
            
            // Show dialog and wait (显示对话框并等待)
            dialogStage.showAndWait();
            
            return controller.isOkClicked();
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error (错误)");
            alert.setHeaderText("Unable to load dialog (无法加载对话框)");
            alert.setContentText("Error message (错误信息): " + e.getMessage());
            alert.showAndWait();
            return false;
        }
    }
}
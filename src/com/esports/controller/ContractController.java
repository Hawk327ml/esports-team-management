package com.esports.controller;

import com.esports.dao.ContractDAO;
import com.esports.dao.PlayerDAO;
import com.esports.model.Contract;
import com.esports.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;  // Add Optional import

public class ContractController {
    
    @FXML
    private TableView<Contract> contractTable;
    
    @FXML
    private TableColumn<Contract, Integer> contractIdColumn;
    
    @FXML
    private TableColumn<Contract, Integer> playerIdColumn;
    
    @FXML
    private TableColumn<Contract, String> teamNameColumn;
    
    @FXML
    private TableColumn<Contract, LocalDate> startDateColumn;
    
    @FXML
    private TableColumn<Contract, LocalDate> endDateColumn;
    
    @FXML
    private TableColumn<Contract, BigDecimal> salaryColumn;
    
    @FXML
    private TableColumn<Contract, String> statusColumn;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Label totalContractsLabel;
    
    @FXML
    private Label activeContractsLabel;
    
    @FXML
    private Label expiringContractsLabel;
    
    @FXML
    private Label totalSalaryLabel;
    
    @FXML
    private Label contractInfoLabel;
    
    private ObservableList<Contract> contractList = FXCollections.observableArrayList();
    private ContractDAO contractDAO = new ContractDAO();
    private PlayerDAO playerDAO = new PlayerDAO();
    
    @FXML
    private void initialize() {
        System.out.println("ContractController initialization (合同控制器初始化)");
        setupTableColumns();
        loadContracts();
        updateStatistics();
        
        // Add table selection listener (添加表格选择监听器)
        contractTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showContractDetails(newValue));
    }
    
    private void setupTableColumns() {
        // Use PropertyValueFactory (使用PropertyValueFactory)
        contractIdColumn.setCellValueFactory(new PropertyValueFactory<>("contractId"));
        playerIdColumn.setCellValueFactory(new PropertyValueFactory<>("playerId"));
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        System.out.println("Contract table columns configured (合同表格列配置完成)");
    }
    
    private void loadContracts() {
        System.out.println("Loading contract data... (加载合同数据...)");
        contractList.clear();
        contractList.addAll(contractDAO.getAllContracts());
        contractTable.setItems(contractList);
        System.out.println("Loaded " + contractList.size() + " contracts (加载了 " + contractList.size() + " 份合同)");
    }
    
    private void updateStatistics() {
        int total = contractList.size();
        int active = 0;
        int expiringSoon = 0;
        BigDecimal totalSalary = BigDecimal.ZERO;
        
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);
        
        for (Contract contract : contractList) {
            if (contract.getSalary() != null) {
                totalSalary = totalSalary.add(contract.getSalary());
            }
            
            LocalDate endDate = contract.getEndDate();
            if (endDate != null) {
                if (!endDate.isBefore(today)) {  // Today or later (今天或之后)
                    active++;
                }
                
                if (endDate.isAfter(today) && endDate.isBefore(thirtyDaysLater)) {
                    expiringSoon++;
                }
            }
        }
        
        totalContractsLabel.setText("Total Contracts (总合同数): " + total);
        activeContractsLabel.setText("Active Contracts (有效合同): " + active);
        expiringContractsLabel.setText("Expiring Soon (即将到期): " + expiringSoon);
        totalSalaryLabel.setText(String.format("Total Salary (总年薪): ¥%.2f", totalSalary));
    }
    
    private void showContractDetails(Contract contract) {
        if (contract == null) {
            contractInfoLabel.setText("No contract selected (没有选中任何合同)");
            return;
        }
        
        try {
            Player player = playerDAO.getPlayerById(contract.getPlayerId());
            String playerName = (player != null) ? player.getUsername() : "Unknown Player (未知选手)";
            
            String info = String.format(
                "Contract ID (合同ID): %d | Player (选手): %s (ID: %d) | Team (战队): %s | Period (期限): %s to (至) %s | Status (状态): %s",
                contract.getContractId(),
                playerName,
                contract.getPlayerId(),
                contract.getTeamName() != null ? contract.getTeamName() : "N/A",
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getStatus()
            );
            contractInfoLabel.setText(info);
        } catch (Exception e) {
            contractInfoLabel.setText("Contract ID (合同ID): " + contract.getContractId() + " | Player ID (选手ID): " + contract.getPlayerId());
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadContracts();
        updateStatistics();
        showAlert("Refresh Successful (刷新成功)", "Contract data has been refreshed! (合同数据已刷新！)");
    }
    
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadContracts();
            updateStatistics();
            return;
        }
        
        ObservableList<Contract> filteredList = FXCollections.observableArrayList();
        for (Contract contract : contractList) {
            boolean matches = false;
            
            // Search player ID (搜索选手ID)
            if (String.valueOf(contract.getPlayerId()).contains(searchText)) {
                matches = true;
            }
            
            // Search team name (搜索战队名称)
            if (contract.getTeamName() != null && 
                contract.getTeamName().toLowerCase().contains(searchText.toLowerCase())) {
                matches = true;
            }
            
            // Search contract type (搜索合同类型)
            if (contract.getContractType() != null && 
                contract.getContractType().toLowerCase().contains(searchText.toLowerCase())) {
                matches = true;
            }
            
            if (matches) {
                filteredList.add(contract);
            }
        }
        
        contractTable.setItems(filteredList);
        updateStatistics();
        
        if (filteredList.isEmpty()) {
            showAlert("Search Results (搜索结果)", "No matching contracts found (没有找到符合条件的合同)");
        }
    }
    
    @FXML
    private void handleClearSearch() {
        searchField.clear();
        loadContracts();
    }
    
    @FXML
    private void handleAddContract() {
        showAlert("Function Hint (功能提示)", "Add contract function pending implementation (添加合同功能待实现)");
    }
    
    @FXML
    private void handleEditContract() {
        Contract selected = contractTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error (选择错误)", "Please select a contract to edit first! (请先选择一份合同进行编辑！)");
            return;
        }
        showAlert("Function Hint (功能提示)", "Edit contract function pending implementation - Selected Contract ID (编辑合同功能待实现 - 选择合同ID): " + selected.getContractId());
    }
    
    @FXML
    private void handleDeleteContract() {
        Contract selected = contractTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error (选择错误)", "Please select a contract to delete first! (请先选择一份合同进行删除！)");
            return;
        }
        
        // Solution 1: Simplified processing, without using lambda (方案1：简化处理，不使用lambda)
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion (确认删除)");
        confirm.setHeaderText("Delete Contract (删除合同)");
        confirm.setContentText("Are you sure you want to delete Contract ID (确定要删除合同ID) " + selected.getContractId() + " ?");
        
        // Use traditional Optional processing (使用传统的Optional处理方式)
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean success = contractDAO.deleteContract(selected.getContractId());
                if (success) {
                    showAlert("Deletion Successful (删除成功)", "Contract has been successfully deleted! (合同已成功删除！)");
                    loadContracts();
                    updateStatistics();
                } else {
                    showAlert("Deletion Failed (删除失败)", "Unable to delete contract, please try again! (无法删除合同，请重试！)");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Deletion Error (删除错误)", "Error occurred during deletion (删除过程中发生错误): " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleBackToMain() {
        try {
            // Get current scene and return to main interface (获取当前场景并返回主界面)
            Stage stage = (Stage) contractTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/esports/view/MainView.fxml"));
            stage.setScene(new Scene(root, 1000, 700));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error (错误)", "Unable to return to main menu (无法返回主菜单): " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
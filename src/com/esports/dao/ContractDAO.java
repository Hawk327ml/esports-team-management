package com.esports.dao;

import com.esports.model.Contract;
import com.esports.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {
    
    // Get all contracts (ensure team_name is included) (获取所有合同（确保包含team_name）)
    public List<Contract> getAllContracts() {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT contract_id, player_id, " +
                    "COALESCE(team_name, 'N/A') as team_name, " +
                    "start_date, end_date, annual_salary, contract_type " +
                    "FROM CONTRACTS ORDER BY contract_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Contract contract = new Contract();
                contract.setContractId(rs.getInt("contract_id"));
                contract.setPlayerId(rs.getInt("player_id"));
                contract.setTeamName(rs.getString("team_name"));
                contract.setStartDate(rs.getDate("start_date").toLocalDate());
                contract.setEndDate(rs.getDate("end_date").toLocalDate());
                contract.setAnnualSalary(rs.getBigDecimal("annual_salary"));
                contract.setContractType(rs.getString("contract_type"));
                
                contracts.add(contract);
            }
            System.out.println("Retrieved " + contracts.size() + " contracts (获取到 " + contracts.size() + " 份合同)");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contracts;
    }
    
    // Get contracts by player ID (根据选手ID获取合同)
    public List<Contract> getContractsByPlayerId(int playerId) {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT contract_id, player_id, " +
                    "COALESCE(team_name, 'N/A') as team_name, " +
                    "start_date, end_date, annual_salary, contract_type " +
                    "FROM CONTRACTS WHERE player_id = ? ORDER BY start_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, playerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Contract contract = new Contract();
                    contract.setContractId(rs.getInt("contract_id"));
                    contract.setPlayerId(rs.getInt("player_id"));
                    contract.setTeamName(rs.getString("team_name"));
                    contract.setStartDate(rs.getDate("start_date").toLocalDate());
                    contract.setEndDate(rs.getDate("end_date").toLocalDate());
                    contract.setAnnualSalary(rs.getBigDecimal("annual_salary"));
                    contract.setContractType(rs.getString("contract_type"));
                    
                    contracts.add(contract);
                }
            }
            System.out.println("Retrieved " + contracts.size() + " contracts for player (获取到选手) " + playerId + " 的 " + contracts.size() + " 份合同");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contracts;
    }
    
    // Add new contract (添加新合同)
    public boolean addContract(Contract contract) {
        String sql = "INSERT INTO CONTRACTS (player_id, team_name, start_date, end_date, annual_salary, contract_type) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, contract.getPlayerId());
            pstmt.setString(2, contract.getTeamName());
            pstmt.setDate(3, Date.valueOf(contract.getStartDate()));
            pstmt.setDate(4, Date.valueOf(contract.getEndDate()));
            pstmt.setBigDecimal(5, contract.getAnnualSalary());
            pstmt.setString(6, contract.getContractType());
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Contract added successfully, affected rows (添加合同成功，影响行数): " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update contract (更新合同)
    public boolean updateContract(Contract contract) {
        String sql = "UPDATE CONTRACTS SET player_id = ?, team_name = ?, start_date = ?, end_date = ?, " +
                    "annual_salary = ?, contract_type = ? WHERE contract_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, contract.getPlayerId());
            pstmt.setString(2, contract.getTeamName());
            pstmt.setDate(3, Date.valueOf(contract.getStartDate()));
            pstmt.setDate(4, Date.valueOf(contract.getEndDate()));
            pstmt.setBigDecimal(5, contract.getAnnualSalary());
            pstmt.setString(6, contract.getContractType());
            pstmt.setInt(7, contract.getContractId());
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Contract updated successfully, affected rows (更新合同成功，影响行数): " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete contract (删除合同)
    public boolean deleteContract(int contractId) {
        String sql = "DELETE FROM CONTRACTS WHERE contract_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, contractId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Contract deleted successfully, affected rows (删除合同成功，影响行数): " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get contract by contract ID (根据合同ID获取合同)
    public Contract getContractById(int contractId) {
        String sql = "SELECT contract_id, player_id, " +
                    "COALESCE(team_name, 'N/A') as team_name, " +
                    "start_date, end_date, annual_salary, contract_type " +
                    "FROM CONTRACTS WHERE contract_id = ?";
        Contract contract = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, contractId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    contract = new Contract();
                    contract.setContractId(rs.getInt("contract_id"));
                    contract.setPlayerId(rs.getInt("player_id"));
                    contract.setTeamName(rs.getString("team_name"));
                    contract.setStartDate(rs.getDate("start_date").toLocalDate());
                    contract.setEndDate(rs.getDate("end_date").toLocalDate());
                    contract.setAnnualSalary(rs.getBigDecimal("annual_salary"));
                    contract.setContractType(rs.getString("contract_type"));
                }
            }
            System.out.println("Retrieved contract ID (获取到合同ID) " + contractId + (contract != null ? " successfully (成功)" : " failed (失败)"));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contract;
    }
}
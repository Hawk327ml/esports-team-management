package com.esports.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Contract {
    private int contractId;
    private int playerId;
    private String teamName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal annualSalary;
    private String contractType;
    
    // Constructor (构造函数)
    public Contract() {}
    
    public Contract(int contractId, int playerId, String teamName, LocalDate startDate, 
                   LocalDate endDate, BigDecimal annualSalary, String contractType) {
        this.contractId = contractId;
        this.playerId = playerId;
        this.teamName = teamName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.annualSalary = annualSalary;
        this.contractType = contractType;
    }
    
    // Getter and Setter
    public int getContractId() { return contractId; }
    public void setContractId(int contractId) { this.contractId = contractId; }
    
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public BigDecimal getAnnualSalary() { return annualSalary; }
    public void setAnnualSalary(BigDecimal annualSalary) { this.annualSalary = annualSalary; }
    
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    
    // For convenience, add getSalary() method (returns annualSalary) (为了方便使用，添加getSalary()方法（返回annualSalary）)
    public BigDecimal getSalary() { return annualSalary; }
    
    // Add getStatus() method, calculate status based on date (添加getStatus()方法，根据日期计算状态)
    public String getStatus() {
        if (endDate == null) {
            return "No Date (无日期)";
        }
        
        LocalDate today = LocalDate.now();
        if (endDate.isBefore(today)) {
            return "Expired (已过期)";
        } else if (endDate.isBefore(today.plusMonths(1))) {
            return "Expiring Soon (即将到期)";
        } else {
            return "Valid (有效)";
        }
    }
    
    @Override
    public String toString() {
        return String.format("Contract{id=%d, playerId=%d, team='%s', salary=%.2f, status=%s}", 
                contractId, playerId, teamName, annualSalary, getStatus());
    }
}
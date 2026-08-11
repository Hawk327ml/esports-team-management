package com.esports.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Player {
    private int playerId;
    private String realName;      // New: real name (新增：真实姓名)
    private String username;      // Game ID/nickname (游戏ID/昵称)
    private int age;
    private String position;
    private String nationality;   // New: nationality (新增：国籍)
    private LocalDate joinDate;
    private BigDecimal salary;
    private String status;
    
    // Constructor (构造函数)
    public Player() {}
    
    public Player(int playerId, String realName, String username, int age, String position, 
                  String nationality, LocalDate joinDate, BigDecimal salary, String status) {
        this.playerId = playerId;
        this.realName = realName;
        this.username = username;
        this.age = age;
        this.position = position;
        this.nationality = nationality;
        this.joinDate = joinDate;
        this.salary = salary;
        this.status = status;
    }
    
    // Getter and Setter
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    // For convenient table display, add a method to display name (为了方便表格显示，添加一个显示名称的方法)
    public String getDisplayName() {
        return realName != null && !realName.isEmpty() ? 
               String.format("%s (%s)", realName, username) : username;
    }
    
    @Override
    public String toString() {
        return String.format("Player{id=%d, username='%s', position='%s', salary=%.2f}", 
                playerId, username, position, salary);
    }
}
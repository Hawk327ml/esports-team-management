package com.esports.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TournamentResult {
    private int recordId;
    private int playerId;
    private String tournamentName;
    private LocalDate tournamentDate;
    private String ranking;
    private BigDecimal prizeMoney;
    private String team;
    
    // Constructor (构造函数)
    public TournamentResult() {}
    
    public TournamentResult(int recordId, int playerId, String tournamentName, 
                          LocalDate tournamentDate, String ranking, 
                          BigDecimal prizeMoney, String team) {
        this.recordId = recordId;
        this.playerId = playerId;
        this.tournamentName = tournamentName;
        this.tournamentDate = tournamentDate;
        this.ranking = ranking;
        this.prizeMoney = prizeMoney;
        this.team = team;
    }
    
    // Getter and Setter
    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }
    
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    
    public LocalDate getTournamentDate() { return tournamentDate; }
    public void setTournamentDate(LocalDate tournamentDate) { this.tournamentDate = tournamentDate; }
    
    public String getRanking() { return ranking; }
    public void setRanking(String ranking) { this.ranking = ranking; }
    
    public BigDecimal getPrizeMoney() { return prizeMoney; }
    public void setPrizeMoney(BigDecimal prizeMoney) { this.prizeMoney = prizeMoney; }
    
    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }
    
    @Override
    public String toString() {
        return String.format("Tournament{id=%d, playerId=%d, tournament='%s', ranking='%s'}", 
                recordId, playerId, tournamentName, ranking);
    }
}
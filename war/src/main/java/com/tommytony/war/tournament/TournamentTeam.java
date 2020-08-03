package com.tommytony.war.tournament;

import com.tommytony.war.Team;
import com.tommytony.war.War;
import com.tommytony.war.config.TeamKind;
import org.apache.commons.lang.SerializationUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TournamentTeam {

 protected ArrayList<Player> players= new ArrayList<Player>();
 protected TeamKind kind;
 protected String name;
 protected Player owner;
 protected Boolean ready;
 public void setReady(Boolean b){
     this.ready=b;
 }
 public Boolean isReady(){
     return this.ready;
 }
public TournamentTeam(Team t) throws CloneNotSupportedException {
    for(Player p:t.getPlayers()){
    this.players.add(p);}
    this.kind=t.getKind();
    this.name=t.getName();

}
public static TournamentTeam GetPlayerTeam(Player p){
    for(TournamentTeam t: War.getTeams()){
        if(t.getPlayers().contains(p)){
            return t;
        }
    }
    return null;

}
public static TournamentTeam GetTeamByName(String s){
        for(TournamentTeam t: War.getTeams()){
            if(t.getName().equalsIgnoreCase(s)){
                return t;
            }
        }
        return null;

    }
public TournamentTeam(TeamKind k, ArrayList<Player> Players, String name, Player o){
    this.owner=o;
    this.kind=k;
    this.players.clear();
    this.players.addAll(Players);
    this.name=name;
}
public Player getOwner(){
    return this.owner;
}
public void setOwner(Player p){
     this.owner=p;

}
public void disband(){

     this.players.clear();

     War.removeFromTeams(this);

}
public static TournamentTeam getOwnerTeam(Player p){
    for(TournamentTeam t: War.getTeams()){
        if(t.getOwner().equals(p)){
            return t;
        }
    }
    return null;
}

public ArrayList<Player> getPlayers(){
    return this.players;
}
public String getName(){
    return this.name;
}
public void addPlayer(Player p){
    this.players.add(p);
}
public TeamKind getKind(){
    return this.kind;
}
public TournamentTeam getTeam(){
    return this;
}
public void removePlayer(Player p){
     this.players.remove(p);
}
}

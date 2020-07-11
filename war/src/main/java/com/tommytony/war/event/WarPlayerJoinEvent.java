package com.tommytony.war.event;

import com.tommytony.war.Team;
import com.tommytony.war.Warzone;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarPlayerJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Team team;

    public WarPlayerJoinEvent(Player player, Team team) {
        this.player = player;
        this.team = team;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }

    public Warzone getWarzone() {
        return team.getZone();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

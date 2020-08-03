package com.tommytony.war.event;

import com.tommytony.war.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarPlayerScoreEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;

    public WarPlayerScoreEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() { return Team.getTeamByPlayerName(player.getName()); }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

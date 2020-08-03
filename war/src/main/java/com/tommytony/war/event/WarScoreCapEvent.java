package com.tommytony.war.event;

import java.util.List;

import com.tommytony.war.Warzone;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.tommytony.war.Team;

public class WarScoreCapEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private List<Team> winningTeams;
	private Warzone warzone;

	public List<Team> getWinningTeams() {
		return winningTeams;
	}

	public WarScoreCapEvent(Warzone warzone, List<Team> winningTeams) {
		this.warzone = warzone;
		this.winningTeams = winningTeams;
	}

	public Warzone getWarzone() {
		return warzone;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}

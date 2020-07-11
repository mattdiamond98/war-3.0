package com.tommytony.war.config;

import com.tommytony.war.Warzone;
import net.kitesoftware.board.criteria.AbstractCriteria;
import net.kitesoftware.board.user.KiteUser;
import org.bukkit.configuration.ConfigurationSection;

public class InWarzoneCriteria extends AbstractCriteria {
    public InWarzoneCriteria(ConfigurationSection config) {
        super("IN_WARZONE", config);
    }
    @Override
    public boolean isMet(KiteUser kiteUser) {
        return Warzone.getZoneByPlayerName(kiteUser.getPlayer().getName()) != null;
    }
    @Override
    public String toString() { return "InWarzone"; }
}

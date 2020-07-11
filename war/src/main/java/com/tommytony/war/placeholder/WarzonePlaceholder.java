package com.tommytony.war.placeholder;

import com.tommytony.war.Team;
import com.tommytony.war.War;
import com.tommytony.war.Warzone;
import com.tommytony.war.config.TeamConfig;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
public class WarzonePlaceholder extends PlaceholderExpansion {

    private War plugin;

    /**
     * @param plugin
     *        The instance of our plugin.
     */
    public WarzonePlaceholder(War plugin){
        this.plugin = plugin;
    }

    /**
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "war";
    }

    /**
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    /**
     * @param  player
     *         A {@link org.bukkit.Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        if(player == null || Team.getTeamByPlayerName(player.getName()) == null){
            return "";
        }
        Team team = Team.getTeamByPlayerName(player.getName());
        Warzone zone = team.getZone();
        List<Team> enemies = new ArrayList<>(zone.getTeams());
        enemies.remove(team);

        // %war_warzone%
        if (identifier.equals("warzone")) {
            return zone.getName();
        }

        if (identifier.startsWith("team")) {
            return process(team, identifier.substring("team".length()));
        }
        if (identifier.startsWith("enemy")) {
            return process(
                    enemies.get(Integer.parseInt(""+identifier.charAt("enemy".length())) - 1),
                    identifier.substring("enemy".length()+1)
            );
        }

        // %war_team%


        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
        return null;
    }

    private String process(Team team, String identifier) {
        if (identifier.equals("name")) {
            return team.getName();
        }

        if (identifier.equals("color")) {
            return team.getKind().getColor().toString();
        }

        if (identifier.equals("score")) {
            return "" + team.getPoints();
        }

        if (identifier.equals("maxscore")) {
            return "" + team.getTeamConfig().resolveInt(TeamConfig.MAXSCORE);
        }

        if (identifier.equals("lifepool")) {
            return "" + team.getRemainingLives();
        }

        if (identifier.equals("maxlifepool")) {
            return "" + team.getTeamConfig().resolveInt(TeamConfig.LIFEPOOL);
        }
        return null;
    }
}
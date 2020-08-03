package com.tommytony.war.tournament;

import com.tommytony.war.Team;
import com.tommytony.war.Warzone;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class TournamentCommand {

    public static void onCommand(CommandSender sender, Command cmd, String[] args) throws CloneNotSupportedException {
        if(sender instanceof ConsoleCommandSender||sender instanceof Player){
            String lowerCmd= cmd.getName().toLowerCase();
            switch(lowerCmd) {
                case "nextphase":
                    if(sender.isOp()){
                        if(args.length>=1){
                            Tournament.getTournamentByName(args[0]).NextPhase();

                        }

                    }
                    break;
                case "establishtourneys":
                    TournamentWarzone zonnne=new TournamentWarzone(Warzone.getZoneByName("oshawott"));
                    ArrayList<TournamentTeam> teamys=new ArrayList<TournamentTeam>();
                    try {
                        TournamentTeam t1=new TournamentTeam(Warzone.getZoneByName("oshawott").getTeams().get(0));
                        t1.getTeam().addPlayer(Bukkit.getPlayer("treepuncherxt"));
                        t1.getTeam().addPlayer(Bukkit.getPlayer("mysterymask"));
                        TournamentTeam t2=new TournamentTeam(Warzone.getZoneByName("oshawott").getTeams().get(1));
                        Bukkit.broadcastMessage(t2.getTeam().getKind().getCapsName());
                        t2.getTeam().addPlayer(Bukkit.getPlayer("BucketofJava"));
                        t2.getTeam().addPlayer(Bukkit.getPlayer("Malatak1"));
                        teamys.add(t1);
                        teamys.add(t2);
                    } catch (CloneNotSupportedException e) {

                    }
                    Tournament tn=new Tournament(teamys, new Warzone[]{Warzone.getZoneByName("oshawott")}, Warzone.getZoneByName("oshawott").getLobby(), null, "oj", null, null, 1, 0);
                    break;
                case "starttourney":
                    if(sender.isOp()){
                        if(args.length>=1){
                            Tournament.getTournamentByName(args[0]).StartTournament();

                        }

                    }
                    break;
                case "startround":
                    if(sender.isOp()){
                        if(args.length>=1){
                            Tournament.getTournamentByName(args[0]).SetStart();

                        }

                    }
                    break;
                case "createtourney":
                    if(sender.isOp()&&sender instanceof Player){
                        if(args.length>=1){
                            Player p=(Player) sender;
                            TournamentWarzone tw=new TournamentWarzone(Warzone.getZoneByLocation(p));
                            ArrayList<TournamentTeam> teaet=new ArrayList<TournamentTeam>();
                            for(Team t:Warzone.getZoneByLocation(p).getTeams()){
                                TournamentTeam newteam=new TournamentTeam(t);
                                teaet.add(newteam);
                            }
                            Tournament t=new Tournament(teaet, new TournamentWarzone[]{tw}, tw.getLobby(),
                                    p.getInventory().getItemInMainHand(), args[0], null, null, 1,0);
                    }}
                    break;


            }

        }



    }
}

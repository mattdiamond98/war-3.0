package com.tommytony.war.command;

import com.tommytony.war.War;
import com.tommytony.war.Warzone;
import com.tommytony.war.config.TeamKind;
import com.tommytony.war.tournament.Tournament;
import com.tommytony.war.tournament.TournamentTeam;
import com.tommytony.war.utility.JSONMessage;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamCreateCommand {
    public static boolean onCommand(CommandSender sender, String cmde, String[] argse){
        if(sender instanceof Player){
            Player p=(Player) sender;

            if(cmde.equalsIgnoreCase("party")&&cmde.length()>0){

               String cmd=argse[0];

              String[] args= Arrays.copyOfRange(argse, 1, argse.length);
            switch(ChatColor.stripColor(cmd).toLowerCase()) {

                case "help":
                    p.sendMessage(new String[]{ChatColor.DARK_BLUE+"/party create (teamname) (optional) color-Creates a team with the specified name and color", ChatColor.DARK_BLUE+"/party add (playername)-Adds the specified player to your team", ChatColor.DARK_BLUE+"/party challenge (teamName) optional(Warzone name)-Challenges the specified team to a battle within the specified arena or a random one", ChatColor.DARK_BLUE+"/party promote (playername)-promotes the specified player to owner of the team", ChatColor.DARK_BLUE+"/party leave -Leaves the party you are in",  ChatColor.DARK_BLUE+"/party disband (teamname)-deletes the specified team", ChatColor.DARK_BLUE+"/party challengeaccept depends(teamname)-accepts the specified/only challenge request",ChatColor.DARK_BLUE+"/party accept depends(teamname)-Accepts the add request from the specified or only team",ChatColor.DARK_BLUE+"/party list-Lists the members of your team", ChatColor.DARK_BLUE+"Admin command: /party forcechallenge (teamname1) (teamname2)-forces the two parties to challenge each other"});
                    break;
                case "list":
                    if(TournamentTeam.GetPlayerTeam(p)!=null){
                        p.sendMessage((ChatColor.YELLOW+"Owner: ")+(ChatColor.DARK_BLUE+TournamentTeam.GetPlayerTeam(p).getOwner().getName()));
                        for(Player P: TournamentTeam.GetPlayerTeam(p).getPlayers()){
                            if(!TournamentTeam.GetPlayerTeam(p).getOwner().equals(P)){
                            p.sendMessage(ChatColor.DARK_BLUE+P.getName());}
                        }
                    }
                    break;
                case "leave":
                    if(TournamentTeam.getOwnerTeam(p)==null){
                    if(TournamentTeam.GetPlayerTeam(p)!=null){
                        p.sendMessage(ChatColor.GREEN+"Successfully left");
                        TournamentTeam.GetPlayerTeam(p).removePlayer(p);
                    }else{
                        p.sendMessage(ChatColor.RED+"You're not in a team!");
                    }
                    }else{
                        p.sendMessage(ChatColor.RED+"You are the owner of this team. Promote another player with /party promote first, or do /party disband to delete the team.");
                    }
                    break;
                case "disband":
                    if(TournamentTeam.getOwnerTeam(p)!=null){
                        p.sendMessage(ChatColor.GREEN+"Successfully disbanded");
                        TournamentTeam.getOwnerTeam(p).disband();
                    }
                    break;
                case "promote":
                    if(args.length>=1){
                    if(Bukkit.getPlayer(args[0])!=null){
                    if(TournamentTeam.getOwnerTeam(p)!=null){
                    if(TournamentTeam.getOwnerTeam(p).getPlayers().contains(Bukkit.getPlayer(args[0]))){

                       List<Player> ps= Objects.requireNonNull(TournamentTeam.getOwnerTeam(p)).getPlayers();
                       for(Player P:ps){
                           P.sendMessage(ChatColor.GREEN+"Player "+args[0]+" was promoted to owner of the team!");
                       }
                        Objects.requireNonNull(TournamentTeam.getOwnerTeam(p)).setOwner(Bukkit.getPlayer(args[0]));
                    }else{
                        p.sendMessage(ChatColor.RED+"That Player's not in your team");
                    }
                    }else{p.sendMessage(ChatColor.RED+"You're not an owner of a team");}
                    }else{
                        p.sendMessage(ChatColor.RED+"That Player doesn't exist");
                    }
                    }else{
                        p.sendMessage(ChatColor.RED+"usage: /party promote PlayerName");
                    }
                    break;
                case "forcechallenge":
                    if(p.hasPermission("war.admin")){
                    if(args.length>=3){
                     if(TournamentTeam.GetTeamByName(args[0])!=null&&TournamentTeam.GetTeamByName(args[1])!=null){
                         if(Warzone.getZoneByName(args[2])!=null){
                             Warzone.getZoneByName(args[2]).setupChallenge(TournamentTeam.GetTeamByName(args[0]));
                             Warzone.getZoneByName(args[2]).setupChallenge(TournamentTeam.GetTeamByName(args[1]));
                             Warzone.getZoneByName(args[2]).HandleConversation(new TournamentTeam[]{TournamentTeam.GetTeamByName(args[0]),TournamentTeam.GetTeamByName(args[1])});
                         }else{p.sendMessage(ChatColor.RED+"That warzone doesn't exist");}
                     }else{
                         p.sendMessage(ChatColor.RED+"One or both of those teams doesn't exist.");
                     }
                    }else if(args.length>=2){
                        if(TournamentTeam.GetTeamByName(args[0])!=null&&TournamentTeam.GetTeamByName(args[1])!=null){
                            Warzone zone=null;
                            ArrayList<Warzone> zones = (ArrayList<Warzone>) War.war.getEnabledWarzones();

                            Collections.shuffle(zones);
                            if(zones.size()>=1){
                                zone=zones.get(0);
                            }
                       zone.setupChallenge(TournamentTeam.GetTeamByName(args[0]));
                        zone.setupChallenge(TournamentTeam.GetTeamByName(args[1]));
                        zone.HandleConversation(new TournamentTeam[]{TournamentTeam.GetTeamByName(args[0]),TournamentTeam.GetTeamByName(args[1])});
                        }else{
                            p.sendMessage(ChatColor.RED+"One or both of those teams doesn't exist.");
                        }
                    }else{
                        p.sendMessage(ChatColor.RED+"Usage: /party forcechallenge team1 team2 (optional-Warzone)");
                    }

                    }else{
                        p.sendMessage(ChatColor.RED+"You don't have permission for that command");
                    }
                    break;
                case "deny":
                    if(args.length>=1){
                        if(War.getJoinRequests().get(p)!=null){
                            if(TournamentTeam.GetTeamByName(args[0])!=null){
                                if(War.getJoinRequests().get(p).contains(TournamentTeam.GetTeamByName(args[0]))){
                                    p.sendMessage(ChatColor.GREEN+"Request successfully denied");
                                    War.denyJoinRequest(p, TournamentTeam.GetTeamByName(args[0]));
                                }
                            }
                        }
                    }else{
                        if(War.getJoinRequests().get(p)!=null){

                            if(War.getJoinRequests().get(p).size()==1){

                                War.denyJoinRequest(p, War.getJoinRequests().get(p).get(0));
                            }else{
                                p.sendMessage(ChatColor.RED+"You have multiple requests. Please specify the team");
                            }

                        }
                    }
                    break;
                case "accept":
                    if(TournamentTeam.GetPlayerTeam(p)==null){
                    if(args.length>=1){
                        if(War.getJoinRequests().get(p)!=null){
                            if(TournamentTeam.GetTeamByName(args[0])!=null){
                                if(War.getJoinRequests().get(p).contains(TournamentTeam.GetTeamByName(args[0]))){
                                    for(Player pe:TournamentTeam.GetTeamByName(args[0]).getPlayers()){
                                        pe.sendMessage(ChatColor.GREEN+p.getName()+" joined the team");
                                    }
                                    p.sendMessage( ChatColor.GREEN+"You Joined the Team!");
                                    TournamentTeam.GetTeamByName(args[0]).addPlayer(p);
                            War.clearJoinRequests(p);

                                }
                            }
                        }
                    }else{
                        if(War.getJoinRequests().get(p)!=null){

                                if(War.getJoinRequests().get(p).size()==1){
                                    for(Player pe:War.getJoinRequests().get(p).get(0).getPlayers()){
                                        pe.sendMessage(ChatColor.GREEN+p.getName()+" joined the team");
                                    }
                                    p.sendMessage( ChatColor.GREEN+"You Joined the Team!");
                                   War.getJoinRequests().get(p).get(0).addPlayer(p);
                                    War.clearJoinRequests(p);
                                }else{
                                    p.sendMessage(ChatColor.RED+"You have multiple requests. Please specify the request you would like to accept.");
                                }

                        }
                    }}else{p.sendMessage(ChatColor.RED+"You are already on a team");}
                    break;
                case "create":

                    if(TournamentTeam.GetPlayerTeam(p)==null){
                        p.sendMessage(ChatColor.GREEN+"Creating team...");
                    if (args.length >= 2) {
                        if(TournamentTeam.GetTeamByName(args[0])==null) {
                            ArrayList<Player> players = new ArrayList<Player>();
                            players.add(p);
                            TournamentTeam t = new TournamentTeam(TeamKind.valueOf(args[1]), players, args[0], p);
                            War.putInTeams(t);
                            p.sendMessage(ChatColor.GREEN + "Team Successfully created");
                        }else{
                        p.sendMessage(ChatColor.RED+"A team with that name already exists");
                        }

                    } else if (args.length >= 1) {
                        if(TournamentTeam.GetTeamByName(args[0])==null) {
                        ArrayList<Player> players = new ArrayList<Player>();
                        players.add(p);
                        TournamentTeam t = new TournamentTeam(TeamKind.IRON, players, args[0], p);

                        War.putInTeams(t);
                        p.sendMessage(ChatColor.GREEN+"Team Successfully created");
                        }else{
                            p.sendMessage(ChatColor.RED+"A team with that name already exists");
                        }

                    } else if (args.length == 0) {
                        p.sendMessage(ChatColor.RED+"Usage: /party create name (optional)color");
                    }}else{p.sendMessage(ChatColor.RED+"You're already in a team.");}
                    break;
                case "add":
                    if (TournamentTeam.getOwnerTeam(p) != null) {
                        if (args.length >= 1) {
                            if(Bukkit.getPlayer(args[0])!=null){
                            War.JoinRequest(Bukkit.getPlayer(args[0]), TournamentTeam.getOwnerTeam(p));
                            p.sendMessage(ChatColor.GREEN+"You requested for "+Bukkit.getPlayer(args[0])+" to join your team!");
                            Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GREEN+"Team "+TournamentTeam.getOwnerTeam(p).getName()+ " has requested to add you to their party. Type /party accept to accept. Note: if there are multiple teams that have sent you a request then you must specify which one.");}

                    } else {
                        p.sendMessage(ChatColor.RED+"Usage: /party add playername");
                    }


                    }else{
                        p.sendMessage(ChatColor.RED+"You Haven't Made A Team Yet. Use /party create");

                    }
                    break;
                case "save":
                    /*if(TournamentTeam.getOwnerTeam(p)!=null){
                        if(!War.getTeams().contains(TournamentTeam.getOwnerTeam(p))){
                        War.saveTeam(TournamentTeam.getOwnerTeam(p));}else{
                            p.sendMessage("");
                        }

                    }else{
                        p.sendMessage(ChatColor.RED+"You're not the captain of a party");
                    }*/
                    break;
                case "challenge":

                    if(TournamentTeam.getOwnerTeam(p)!=null){
                        if(args.length>=2){
                        if(Warzone.getZoneByName(args[1])!=null){
                            if(TournamentTeam.GetTeamByName(args[0])!=null){
                              TournamentTeam.GetTeamByName(args[0]).getOwner().sendMessage(ChatColor.GREEN+"Team "+TournamentTeam.getOwnerTeam(p).getName()+" has requested to challenge your team at Warzone "+args[1]+ ". Type /party challengeaccept to accept. If you have multiple challenges then you must specify the team that is challenging you");
                                p.sendMessage(ChatColor.GREEN+"Successfully challenged");
                                War.createChallenge(TournamentTeam.GetTeamByName(args[0]), TournamentTeam.getOwnerTeam(p), Warzone.getZoneByName(args[1]));
                            }



                        }else{
                            p.sendMessage(ChatColor.RED+"That isn't a warzone or it isn't a tournament warzone");
                        }
                        }else if(args.length>=1){
                            if(TournamentTeam.GetTeamByName(args[0])!=null){
                                Warzone zone=null;
                                ArrayList<Warzone> zones = (ArrayList<Warzone>) War.war.getEnabledWarzones();

                                Collections.shuffle(zones);
                                if(zones.size()>=1){
                                    zone=zones.get(0);
                                }
                                TournamentTeam tt=TournamentTeam.GetTeamByName(args[0]);
                               tt.getOwner().sendMessage(ChatColor.GREEN+"Team "+TournamentTeam.getOwnerTeam(p).getName()+" has requested to challenge your team at Warzone "+zone.getName()+ ". Type /party challengeaccept to accept. If you have multiple challenges then you must specify the team that is challenging you");
                                War.createChallenge(TournamentTeam.GetTeamByName(args[0]), TournamentTeam.getOwnerTeam(p), zone);
                            }

                        }
                    }
                    break;
                case "challengeaccept":

                    if(TournamentTeam.getOwnerTeam(p)!=null){

                        if(War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p))!=null){
                            p.sendMessage(ChatColor.GREEN+"You have accepted the challenge");
                         if(args.length>=1){
                             if(TournamentTeam.GetTeamByName(args[0])!=null){
                                 if(War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).containsKey(TournamentTeam.GetTeamByName(args[0]))){
                                War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).get(TournamentTeam.GetTeamByName(args[0])).setupChallenge(TournamentTeam.GetPlayerTeam(p));
                                     War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).get(TournamentTeam.GetTeamByName(args[0])).setupChallenge(TournamentTeam.GetTeamByName(args[0]));
                                     War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).get(TournamentTeam.GetTeamByName(args[0])).HandleConversation(new TournamentTeam[]{TournamentTeam.GetPlayerTeam(p), TournamentTeam.GetTeamByName(args[0])});
                                 }else{
                                     p.sendMessage(ChatColor.RED+"That team hasn't challenged you");
                                 }

                             }else{
                                 p.sendMessage(ChatColor.RED+"That team doesn't exist");
                             }
                         }else if(War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).size()<=1){
                             War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).get(War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).keySet().toArray(new TournamentTeam[1])[0]).setupChallenge(TournamentTeam.GetPlayerTeam(p));
                             War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).get(War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).keySet().toArray(new TournamentTeam[1])[0]).setupChallenge(War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).keySet().toArray(new TournamentTeam[1])[0]);
                             War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).get(War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).keySet().toArray(new TournamentTeam[1])[0]).HandleConversation(new TournamentTeam[]{TournamentTeam.GetPlayerTeam(p), War.getActiveChallenges().get(TournamentTeam.getOwnerTeam(p)).keySet().toArray(new TournamentTeam[1])[0]});
                         }else{
                             p.sendMessage(ChatColor.RED+"You have multiple challenges. Please specify a team.");
                         }

                        }
                    }
            }
        }}
        return false;

    }

}

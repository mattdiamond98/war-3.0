package com.tommytony.war.tournament;


import com.mysql.fabric.xmlrpc.base.Array;
import com.sun.jnlp.FileOpenServiceNSBImpl;
import com.tommytony.war.Team;
import com.tommytony.war.War;
import com.tommytony.war.Warzone;
import com.tommytony.war.event.WarBattleWinEvent;
import com.tommytony.war.structure.ZoneLobby;
import com.tommytony.war.volume.Volume;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.text.EditorKit;
import java.io.File;
import java.io.Serializable;
import java.util.*;

public class Tournament implements Listener, Serializable {
   public Inventory GUI= Bukkit.createInventory(null, 9, "Teleport to Battle");
    ZoneLobby lobby;
    Warzone[] warzones;
    List<TournamentTeam> teams;
    public int gamesperbattle=1;
    BukkitRunnable run=null;
    public int lol=0;
    public int totalrounds=0;
    HashMap<TournamentTeam[], Warzone> Bracket=new HashMap<TournamentTeam[], Warzone>();
    ArrayList<HashMap<TournamentTeam[], Warzone>> RoundBrackets=new ArrayList<HashMap<TournamentTeam[], Warzone>>();
    public ItemStack rulebook;
    public boolean started;
    public int round=0;
    public boolean setstart=false;
    public HashMap<Warzone, TournamentTeam> FinishedBattles=new HashMap<Warzone, TournamentTeam>();
    public String Id;
    public final ArrayList<TournamentTeam> fakereturnvalue=new ArrayList<TournamentTeam>();
    public ItemStack navigator=new ItemSmith(Material.COMPASS, "Navigator", "Teleport to different matches", false, false, 0, 1, null);
    public static File datafolder= War.getProvidingPlugin(War.class).getDataFolder();
    ArrayList<TournamentTeam> returnvaluel=new ArrayList<TournamentTeam>();
    ArrayList<Warzone> waryzonesl=new ArrayList<Warzone>();
    HashMap<Warzone, ArrayList<TournamentTeam>> Winningteamsl;
    public static HashMap<String, Tournament> tourneys=new HashMap<String, Tournament>();

    public List<TournamentTeam> getTeams() {
        return teams;
    }
    public void SetStart(){
        Bukkit.broadcastMessage("setstart is working");
        this.setstart=true;
        SecondStartRound(this.returnvaluel, this.waryzonesl, this.Winningteamsl);

    }
    public void SetBracket(HashMap<TournamentTeam[], Warzone> bracket){

        this.Bracket=bracket;

        teams.clear();
        for(TournamentTeam[] teamies:Bracket.keySet()){
        for(TournamentTeam teim:teamies){
            teams.add(teim);
        }

        }
    }
    public void OnWarBattleWIn(WarBattleWinEvent e) throws CloneNotSupportedException {
        if(e.getZone() instanceof Warzone){
            FinishedBattles.put(((Warzone) e.getZone()), new TournamentTeam(e.getWinningTeams().get(0)));

        }

    }
    public static Tournament getTournamentByName(String name){

        if(tourneys==null){

            return null;
        }
        return tourneys.get(name);


    }
    final public static void setValue(Object x, Object y){
        x=y;
    }


    public boolean SaveTourney(){

    if(tourneys==null){
        tourneys=new HashMap<String, Tournament>();

    }
    tourneys.put(Id, this);

    return true;



    }


    public boolean isStarted() {
        return started;
    }
    public void NextPhase(){
        this.setstart=false;
        if(lol!=0){
            this.teams=fakereturnvalue;
        }
        if(RoundBrackets.size()<lol){
            ArrangeTeams();
            RoundBrackets.add(this.Bracket);
        }else{

            this.SetBracket(RoundBrackets.get(lol));
        }
        this.StartRound(RoundBrackets.get(lol));
        lol++;


    }
    public void StartTournament(){
        started=true;

         if(lol!=0){
             this.teams=fakereturnvalue;
         }
         if(RoundBrackets.size()<lol){
             ArrangeTeams();
             RoundBrackets.add(this.Bracket);
         }else{

             this.SetBracket(RoundBrackets.get(lol));
         }
        this.StartRound(RoundBrackets.get(lol));
         lol++;

    }

    public void StartRound(HashMap<TournamentTeam[], Warzone> bracket){
        ArrayList<TournamentTeam> returnvalue=new ArrayList<TournamentTeam>();
        int i=0;
        HashMap<Warzone, ArrayList<TournamentTeam>> Winningteams=new HashMap<Warzone, ArrayList<TournamentTeam>>();
        ArrayList<Warzone> waryzones=new ArrayList<Warzone>();
        for(TournamentTeam[] ts:Bracket.keySet()){
            for(TournamentTeam t:ts){
                for(Player p:t.getTeam().getPlayers()){
                    Bukkit.broadcastMessage("Whateverary4"+p.getName());}}
        }
        for(TournamentTeam[] teame:Bracket.keySet()){

            if(teame.length==2){
                Bukkit.broadcastMessage("Thisthisthis"+String.valueOf(teame[0].getTeam().getPlayers().size()));

                Team teamei=Bracket.get(teame).getTeamByKind(teame[0].getTeam().getKind());
                Bukkit.broadcastMessage(String.valueOf(Bracket.get(teame).getTeams().size()));
                for(Player p:teame[0].getTeam().getPlayers()){

                    Bukkit.broadcastMessage("Secondary"+p.getName());
                }
                for(Player p:teame[1].getTeam().getPlayers()){

                    Bukkit.broadcastMessage("Secondary"+p.getName());
                }

                for(Player p:teame[0].getTeam().getPlayers()){

                    BattleSpectator.setPlayerNotSpectator(p);
                    p.teleport(teamei.getRandomSpawn());
                    Bracket.get(teame).assign(p, teamei);
                    Volume volume= teamei.getSpawnVolumes().get(teamei.getRandomSpawn());

                    for(int y=volume.getMaxY(); y<=4+volume.getMaxY(); y++){

                        for(int x=volume.getMinX(); x<=volume.getMaxX(); x++){
                            Bukkit.broadcastMessage("Cyan"+String.valueOf(volume!=null));
                            new Location(p.getWorld(), x, y, volume.getMaxZ()+1).getBlock();
                            new Location(p.getWorld(), x, y, volume.getMinZ()-1).getBlock();

                        }
                        for(int z=volume.getMinZ(); z<=volume.getMaxZ(); z++){
                            new Location(p.getWorld(), volume.getMaxX()-1, y, z).getBlock();
                            new Location(p.getWorld(), volume.getMinX()-1, y, z).getBlock();


                        }
                    }
                }
                Team teameia=Bracket.get(teame).getTeamByKind(teame[1].getTeam().getKind());
                for(Player p:teame[1].getTeam().getPlayers()){
                    BattleSpectator.setPlayerNotSpectator(p);

                    Bukkit.broadcastMessage(teameia.getKind().getCapsName());

                    p.teleport(teameia.getRandomSpawn());
                    Bracket.get(teame).assign(p, teameia);



                    Volume volume= teameia.getSpawnVolumes().get(teameia.getRandomSpawn());
                    for(int y=volume.getMaxY(); y<=4; y++){
                        for(int x=volume.getMinX(); x<=volume.getMaxX(); x++){

                            new Location(p.getWorld(), x, y, volume.getMaxZ()+1).getBlock().setType(Material.BARRIER);
                            new Location(p.getWorld(), x, y, volume.getMinZ()-1).getBlock().setType(Material.BARRIER);

                        }
                        for(int z=volume.getMinZ(); z<=volume.getMaxZ(); z++){
                            new Location(p.getWorld(), volume.getMaxX()-1, y, z).getBlock().setType(Material.BARRIER);
                            new Location(p.getWorld(), volume.getMinX()-1, y, z).getBlock().setType(Material.BARRIER);


                        }
                    }
                }
                waryzones.add(Bracket.get(teame));
                Winningteams.put(Bracket.get(teame), new ArrayList<TournamentTeam>());
            }else{
                for(Player p:teame[0].getTeam().getPlayers()){
                    BattleSpectator.setPlayerSpectator(p);
                    p.getInventory().addItem(navigator);
                }

            }

        }
    this.returnvaluel=returnvalue;
     this.waryzonesl=waryzones;
     this.Winningteamsl=Winningteams;
    }

        public void SecondStartRound(ArrayList<TournamentTeam> returnvalue, ArrayList<Warzone> waryzones, HashMap<Warzone, ArrayList<TournamentTeam>> Winningteams) {


         final HashMap<Warzone, TournamentTeam> fb=this.FinishedBattles;
         final ArrayList<TournamentTeam> teams1t= (ArrayList<TournamentTeam>) this.teams;
         this.run=new BukkitRunnable() {
            @Override
            public void run() {




        TournamentTeam toller=null;
        Warzone zonnnnnne = null;
        final ArrayList<TournamentTeam> tollers=new ArrayList<TournamentTeam>();
        final ArrayList<Warzone> zonnes=new ArrayList<Warzone>();
        final ArrayList<Integer> ids=new ArrayList<Integer>();
            BukkitRunnable run2=new BukkitRunnable() {

                @Override
                public void run() {



                for (Warzone zonewoman:waryzones){

                if(fb.keySet().contains(zonewoman)){
                    zonnes.add(zonewoman);
                    tollers.add(fb.get(zonewoman));
                    this.cancel();
                }
                }

            }};
            run2.runTaskTimer(War.getProvidingPlugin(War.class), 5L, 2L);

            if(run2.isCancelled()){

                toller=tollers.get(0);

                zonnnnnne=zonnes.get(0);
            ArrayList<TournamentTeam> HHAHHAHAHAHA=Winningteams.get(zonnnnnne);
        HHAHHAHAHAHA.add(toller);
            Winningteams.remove(zonnnnnne);
            Winningteams.put(zonnnnnne, HHAHHAHAHAHA);
            TournamentTeam[] teame={};
            if(HHAHHAHAHAHA.size()<gamesperbattle){
                for(TournamentTeam[] EDDDDD: Bracket.keySet()){
                    if(Bracket.get(EDDDDD).equals(zonnnnnne)){
                        teame=EDDDDD;
                    }
                }
                for(Player p:teame[0].getTeam().getPlayers()){
                    p.teleport(Bracket.get(teame).getTeleport());
                }
                for(Player p:teame[1].getTeam().getPlayers()){
                    p.teleport(Bracket.get(teame).getTeleport());
                }


            }else{

                if(HHAHHAHAHAHA.size()<gamesperbattle){
                    for(TournamentTeam[] EDDDDD: Bracket.keySet()){
                        if(Bracket.get(EDDDDD).equals(zonnnnnne)){
                            teame=EDDDDD;
                        }
                    }}
                int team1value=0;
                int team2value=0;
                for(TournamentTeam t:Winningteams.get(zonnnnnne)){
                    if(t.equals(teame[0])){
                        team1value++;
                    }else if(t.equals(teame[1])){
                        team2value++;
                    }
                }
                if(team1value>team2value){
                    returnvalue.add(teame[0]);
                    teams1t.remove(teame[1]);

                }else if(team2value>team1value){

                    returnvalue.add(teame[1]);
                    teams1t.remove(teame[0]);
                }

                if(returnvalue.size()>=Bracket.keySet().size()-(teams1t.size()%2)){

                fakereturnvalue.addAll(returnvalue);
                 this.cancel();
                }else{
                    for(TournamentTeam t:teame){
                        for(Player p:t.getTeam().getPlayers()){
                            BattleSpectator.setPlayerSpectator(p);
                        }
                    }

                }
            }}}};
        this.run.runTaskTimer(War.getProvidingPlugin(War.class), 5L, 2L);





    }

    public Tournament(List<TournamentTeam> teamsy, Warzone[] warzonesy, ZoneLobby lobbyy, ItemStack book, String idy, @Nullable HashMap<TournamentTeam[], Warzone> bracket, @Nullable ArrayList<HashMap<TournamentTeam[], Warzone>> roundbrackets, int gamesperbattled, int roundys){
        if(gamesperbattled!=0){this.gamesperbattle=gamesperbattled;}
        this.rulebook=book;
        this.teams=teamsy;
        this.warzones=warzonesy;

        this.lobby=lobbyy;
        for(Warzone hi:warzonesy){
            hi.setLobby(lobby);

        }
        this.Id=idy;
        if(bracket==null){
        ArrangeTeams();}
        else {
        this.SetBracket(bracket);
            }
        if(roundys!=0){this.totalrounds=roundys;}else{roundys=teams.size() == 0 ? 0 : 32 - Integer.numberOfLeadingZeros(teams.size() - 1);}
        if(roundbrackets==null){RoundBrackets.add(this.Bracket);}else{this.RoundBrackets=roundbrackets; this.totalrounds=roundbrackets.size();}
        Warzone zone=warzonesy[0];
        this.SaveTourney();

    }

    public void OpenTourneyGUI(Player player){
    GUI.setItem(0, new ItemStack(Material.AIR));
    if(!Bracket.isEmpty()){
        String newstring="Teleport to match: "+teams.get(0).getTeam().getName()+" vs. "+teams.get(1).getTeam().getName();
        GUI.setItem(1, new ItemSmith(Material.ENDER_EYE, newstring, "",  false, false, 0, 1, null));

    }else{
        GUI.setItem(1, new ItemStack(Material.AIR));
    }
    GUI.setItem(2, new ItemStack(Material.AIR));
        if(teams.size()>=2){
            String newstring="Teleport to match: "+teams.get(2).getTeam().getName()+" vs. "+teams.get(3).getTeam().getName();
            GUI.setItem(3, new ItemSmith(Material.ENDER_EYE, newstring, "",  false, false, 0, 1, null));

        }else{
            GUI.setItem(3, new ItemStack(Material.AIR));
        }
        GUI.setItem(4, new ItemStack(Material.AIR));
        if(teams.size()>=4){
            String newstring="Teleport to match: "+teams.get(4).getTeam().getName()+" vs. "+teams.get(5).getTeam().getName();
            GUI.setItem(5, new ItemSmith(Material.ENDER_EYE, newstring, "",  false, false, 0, 1, null));

        }else{
            GUI.setItem(5, new ItemStack(Material.AIR));
        }
        GUI.setItem(6, new ItemStack(Material.AIR));
        if(teams.size()>=6){
            String newstring="Teleport to match: "+teams.get(6).getTeam().getName()+" vs. "+teams.get(7).getTeam().getName();
            GUI.setItem(5, new ItemSmith(Material.ENDER_EYE, newstring, "",  false, false, 0, 1, null));

        }else{
            GUI.setItem(5, new ItemStack(Material.AIR));
        }
        player.openInventory(this.GUI);

    }
    public Tournament(){}
   /* @EventHandler
    public void OnPlayerMoveEvent(PlayerMoveEvent e){
        if(e.getTo().getBlock().getType()== Material.NETHER_PORTAL&&e.getTo().subtract(0, 1, 0).getBlock().getType()!=Material.OBSIDIAN){
     TeleporttoColloseum(e.getPlayer());

        }

        if(this.setstart==false) {

            Warzone zone = Warzone.getZoneByLocation(e.getPlayer());
            if (zone != null) {
                if (zone.getPlayerTeam(e.getPlayer().getName()) != null) {
                    if (zone.getPlayerTeam(e.getPlayer().getName()).getSpawnVolumes().get(zone.getPlayerTeam(e.getPlayer().getName()).getRandomSpawn()).contains(e.getFrom()) && !(zone.getPlayerTeam(e.getPlayer().getName()).getSpawnVolumes().get(zone.getPlayerTeam(e.getPlayer().getName()).getRandomSpawn()).contains(e.getTo()))) {
                        e.setCancelled(true);
                    }
                }
            }
        }

    }
    /*@EventHandler
    public void OnPlayerInteract(PlayerInteractEvent e){
        if(e.getItem().isSimilar(this.navigator)){
            OpenTourneyGUI(e.getPlayer());

        }


    }*/
    public void TeleporttoColloseum(Player p) {
        p.openBook(this.rulebook);



        boolean condition = false;
        if (condition) {


        } else {
            BattleSpectator.setPlayerSpectator(p);
            p.getInventory().addItem(navigator);
        }


    }

    public void ArrangeTeams() {


        Collections.shuffle(teams);
        for(TournamentTeam t:teams){
            for(Player p:t.getTeam().getPlayers()){
            Bukkit.broadcastMessage("Whateverary"+p.getName());}
        }

        TournamentTeam bye = null;
        if (teams.size() % 2 != 0) {
            bye = teams.get(0);
            teams.remove(bye);

        }
        this.Bracket.clear();
        int i = 0;
        for (TournamentTeam team : teams) {
            Bukkit.broadcastMessage("Number of teams:" + String.valueOf(teams.size()));
            this.Bracket.put(new TournamentTeam[]{
                    teams.get(i),
                    teams.get(i + 1)},
                    warzones[i/2]);
            i += 2;
            if(i+1>=teams.size()-1){
                break;

            }
Bukkit.broadcastMessage("End of loop");
        }
        if(bye!=null){
           this.Bracket.put(new TournamentTeam[]{bye}, null);
            teams.add(bye);
        }
        for(TournamentTeam t:teams){
            for(Player p:t.getTeam().getPlayers()){
                Bukkit.broadcastMessage("Whateverary2"+p.getName());}
        }
        for(TournamentTeam[] ts:Bracket.keySet()){
            for(TournamentTeam t:ts){
            for(Player p:t.getTeam().getPlayers()){
                Bukkit.broadcastMessage("Whateverary3"+p.getName());}}
        }

    }
}
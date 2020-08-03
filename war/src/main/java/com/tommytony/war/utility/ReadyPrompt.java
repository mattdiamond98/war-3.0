package com.tommytony.war.utility;

import com.tommytony.war.War;
import com.tommytony.war.Warzone;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.scheduler.BukkitRunnable;

public class ReadyPrompt extends StringPrompt {


    Warzone wz;
    public ReadyPrompt( Warzone WZ) {

        this.wz=WZ;
    }
    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return "Say Ready when ready";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if(s.equalsIgnoreCase("ready")){

        if(wz.secondtrue()) {

            wz.setsecond(false);
            wz.StartCountdown();
        }else{wz.setsecond(true);}

            return null;}
        return this;

    }
}

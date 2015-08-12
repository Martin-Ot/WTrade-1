package eu.weltensiedler.wtrade;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private ArrayList<Player> abos;
    private ArrayList<String> state; 
    String prefix = "§6[Handel] ";

    @Override
    public void onEnable() {
        abos = new ArrayList<Player>();
        state = new ArrayList<String>();
        this.getServer().getPluginManager().registerEvents(this, this);
        System.out.println("[HandelsChat v1.0 by Martin_Ot] Initialized the Lists and registered all Events.");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent pqe) {
    	if (abos.contains(pqe.getPlayer())) {
    		if (!state.contains(pqe.getPlayer())) {
    		  state.add(pqe.getPlayer().getUniqueId().toString());
    	    }
    	    abos.remove(pqe.getPlayer());
    	    for (CommandSender k : abos) {
                try {
                    k.sendMessage(prefix + "§e" + pqe.getPlayer().getName() + " verlässt den Handelschat.");
                } catch (Exception ex) {
                    abos.remove(k);
                }
            }
    	}
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent pke) {
        if(abos.contains(pke.getPlayer())) {
        	if (!state.contains(pke.getPlayer())) {
      		  state.add(pke.getPlayer().getUniqueId().toString());
      		  
      	    }
            abos.remove(pke.getPlayer());
            for (Player k : abos) {
                try {
                    k.sendMessage(prefix + "§e" + pke.getPlayer().getName() + " verlässt den Handelschat.");
                } catch (Exception ex) {
                    abos.remove(k);
                }
            }
            
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje) {
    	if(state.contains(pje.getPlayer().getUniqueId().toString())) {
    		
    		pje.getPlayer().sendMessage(prefix + "§e Der HandelsChat wurde automatisch wieder abonniert.");
    		for (Player k : abos) {
                try {
                    k.sendMessage(prefix + "§e" + pje.getPlayer().getName() + " betritt den Handelschat.");
                } catch (Exception ex) {
                    abos.remove(k);	
                }
    		}
    		abos.add(pje.getPlayer());
    		}
    		
    	}
    
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChatEvent (AsyncPlayerChatEvent apce) {
    	Player p = apce.getPlayer();
    	if (apce.getMessage().startsWith("$")) {
    		apce.setCancelled(true);
    		if (abos.size() <= 1 && abos.contains(p)) {
                p.sendMessage(prefix
                    + "§cAktuell ist kein Spieler im Handelschat.");
            } else {
                if (!abos.contains(p)) {
                    p.sendMessage(prefix
                        + "§cDu kannst nur in den Handelschat schreiben, wenn du diesen abonnierst. §o/handel abo");
                    
                } else {
                    
                    for (Player k : abos) {
                        try {
                        	 k.sendMessage(prefix + "§e" + p.getName() + ": §r§o" + apce.getMessage().substring(1));
                        } catch (Exception ex) {
                            abos.remove(k);
                        }
                        System.out.println("[HandelsChat] " + p.getName() + ": " + apce.getMessage().substring(1));

                    }
                   
                }

            }
                      

                }
    		}
    	
    
    
    @Override
    public boolean onCommand(CommandSender sender, Command command,
    String label, String[] args) {

        if (command.getName().equalsIgnoreCase("handel")
        || command.getName().equalsIgnoreCase("$")) {
            if (args.length == 0) {
                sender.sendMessage(prefix
                    + "§cAnleitung für den Handels-Chat: Benutze §o/handel abo §cum deine Benachrichtigungen umzuschalten. Benutze §o/handel <text> §cum einen Text an alle im Handelschat zu senden.");
                return true;
            } else {
                if (args.length == 1 && args[0].equalsIgnoreCase("abo")) {
                    if (!abos.contains(sender)) {

                        
                        for (Player k : abos) {
                            try {
                                k.sendMessage(prefix + "§e" + sender.getName() + " betritt den Handelschat.");
                            } catch (Exception ex) {
                                abos.remove(k);	
                            }
                        }
                        abos.add((Player)sender);
                        sender.sendMessage(prefix
                                + "§r§oDu hast den Handelschat abonniert.\nOnline im Handelschat: §e" + abos.size() + " Spieler");
                        return true; 
                    }  else {
                        abos.remove(sender);
                        state.remove(((Player)sender).getUniqueId().toString());
                        sender.sendMessage(prefix
                            + "§r§oDu hast den Handelschat deabonniert.");
                        for (CommandSender k : abos) {
                            try {
                                k.sendMessage(prefix + "§e" + sender.getName() + " verlässt den Handelschat.");
                            } catch (Exception ex) {
                                abos.remove(k);
                            }
                        }
                        return true;
                    }
                } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                    if(abos.size() == 0 ) {
                        sender.sendMessage(prefix + "§cEs ist kein Spieler im Handelschat online.");
                    } else if (abos.size() == 1 && abos.contains(sender)) {
                        sender.sendMessage(prefix + "§cNur du bist im Handelschat online.");
                    } else {
                        String msg = prefix + "§eOnline im Handelschat(" + abos.size() + "): §r§o";
                        Boolean first = true;
                        for (Player p : abos) {
                            if (first) {
                                msg += p.getName();
                                first = false;
                            } else {
                                msg += ", " + p.getName();
                            }
                           
                        }
                        
                        sender.sendMessage(msg);
                    }
                    return true;
                
            }
            if (args.length >= 1) {
                if (abos.size() <= 1 && abos.contains(sender)) {
                    sender.sendMessage(prefix
                        + "§cAktuell ist kein Spieler im Handelschat.");
                    return true;
                } else {
                    if (!abos.contains(sender)) {
                        sender.sendMessage(prefix
                            + "§cDu kannst nur in den Handelschat schreiben, wenn du diesen abonnierst. §o/handel abo");
                        return true;
                    } else {
                        String message = "§e" + sender.getName() + ":§r§o";
                        for (String s : args) {
                            message += " " + s;
                        }
                        for (Player k : abos) {
                            try {
                                k.sendMessage(prefix + "" + message);
                            } catch (Exception ex) {
                                abos.remove(k);
                            }

                            System.out.println("[HandelsChat] " + sender.getName() + ": " + message);
                        }
                        return true;
                    }

                }
            }

        }


    }
        return false;
}
}


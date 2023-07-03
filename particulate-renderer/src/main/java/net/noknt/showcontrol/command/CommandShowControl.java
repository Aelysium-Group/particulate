package net.noknt.showcontrol.command;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.noknt.showcontrol.ShowControl;
import net.noknt.showcontrol.chat.Chat;
import net.noknt.showcontrol.chat.Mute;
import net.noknt.showcontrol.chat.Message;
import net.noknt.showcontrol.controlPanel.chat.FxPanel;
import net.noknt.showcontrol.controlPanel.chat.ScenePanel;
import net.noknt.showcontrol.serverConnect.http;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CommandShowControl implements CommandExecutor {
    Chat chat = new Chat();
    FxPanel fxPanel = new FxPanel();
    ScenePanel scenePanel = new ScenePanel();
    Mute mute = new Mute();
    Message controlPanel = new Message();

    private final String chatPrefix = ChatColor.DARK_GRAY+"["+ChatColor.AQUA+"ShowControl"+ChatColor.DARK_GRAY+"]: "+ChatColor.GRAY;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length == 0) {
                sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc <webcontrol/control/commit/emitter/reload>");
                return true;
            }
            switch(args[0]) {
                case "getlinkstatus":
                    for (Map.Entry<Integer, Integer> linkStatusLoopMap : ShowControl.getInstance().getLinkStatus().entrySet()) { // Loop through all of the current linkStatus items
                        ShowControl.getInstance().log(linkStatusLoopMap.getKey()+": "+linkStatusLoopMap.getValue());
                    }
                    break;
                case "webcontrol":
                    sender.sendMessage(chatPrefix + "Preparing a control session...");
                    http http = new http();
                    JsonObject json = new JsonObject();
                    json.add("registration", new JsonPrimitive(ShowControl.getInstance().getMap("mainConfig").get("registration-key").toString()));
                    for (Map.Entry<String, Object> slot : ShowControl.getInstance().getMap("mainConfig").entrySet()) {
                        int i = 0;
                        if (slot.getKey().matches("control-panel\\.[0-9]*") ) {
                            if (ShowControl.getInstance().getMap("mainConfig").containsKey(slot.getKey() + ".name")) {
                                json.add(slot.getKey(), new JsonPrimitive(ShowControl.getInstance().getMap("mainConfig").get(slot.getKey() + ".link-id").toString()+"-"+ShowControl.getInstance().getMap("mainConfig").get(slot.getKey() + ".name").toString()));
                            } else {
                                json.add(slot.getKey(), new JsonPrimitive(ShowControl.getInstance().getMap("mainConfig").get(slot.getKey() + ".link-id").toString()+"-"+"null"));
                            }
                        }
                    }
                    String response = http.post("a0863b205a40822a4015d76efb6a832f",json);
                    if(response.equals("FALSE")) {
                    }
                break;
                case "control":
                    if(args.length == 1) {
                        sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control <screen/fx>");
                        return true;
                    }
                    switch(args[1]) {
                        case "fx":
                            if(args.length <= 3) {
                                sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control fx <toggle/kill/panel>");
                                sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Controlling effects from chat");
                                return true;
                            }
                            switch(args[2]) {
                                case "toggle":
                                    if(!ShowControl.getInstance().getMap("controlPanel").containsKey("control-panel."+args[3])) {
                                        sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control fx toggle <linkID>");
                                        sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Controlling single effects via commands");
                                    }
                                    try {
                                        if(ShowControl.getInstance().getLinkStatus().containsKey(Integer.valueOf(args[3]))) {
                                            ShowControl.getInstance().getLinkStatus().put(Integer.valueOf(args[3]), -1);
                                        } else {
                                            if(ShowControl.getInstance().getMap("controlPanel").containsKey("control-panel."+args[4]+".single-use")) {
                                                ShowControl.getInstance().getLinkStatus().put(Integer.valueOf(args[3]), -1);
                                            } else {
                                                ShowControl.getInstance().getLinkStatus().put(Integer.valueOf(args[3]), 1);
                                            }

                                        }
                                    } catch(NumberFormatException e) {
                                        sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control fx toggle <linkID>");
                                        sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Controlling single effects via commands");
                                        return true;
                                    }
                                    fxPanel.build(controlPanel);
                                    chat.send(player,controlPanel);
                                    controlPanel.clear();
                                    break;
                                case "kill":
                                    ShowControl.getInstance().getLinkStatus().clear();
                                    break;
                                case "panel":
                                    fxPanel.build(controlPanel);
                                    chat.send(player, controlPanel);
                                    controlPanel.clear();
                                break;
                                default:
                                    sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control panel <enable/disable/help>");
                                    sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Controlling effects from a chat GUI");
                                break;
                            }

                        break;
                        case "screen":
                            if(args.length == 2) {
                                sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control screen <change>");
                                sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Controlling screens from chat");
                                return true;
                            }
                            switch(args[2]) {
                                case "change":
                                    if(args.length <= 4) {
                                        sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control screen change <screen name> <scene name>");
                                        sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Changes the scene of a screen");
                                        return true;
                                    }
                                    if(ShowControl.getInstance().getMap("scenes").containsKey("scenes."+args[4])) { // Check if the current scene name is valid
                                        for (Map.Entry<String, Object> screensLoopMap : ShowControl.getInstance().getMap("screens").entrySet()) { // Get entire screen config
                                            if (screensLoopMap.getKey().matches("screens\\." + args[3] + "\\.panels\\.[0-9]*\\.scene-link")) { // Check if current item is a scene-link item
                                                Integer scenePanel = (Integer) screensLoopMap.getValue();/*.replace("screens\\." + args[3] + "\\.panels\\.([0-9]*)\\.scene-link","$1")*/ // Get value of scene-link item
                                                if(ShowControl.getInstance().getMap("scenes").containsKey("scenes."+args[4]+"."+scenePanel)) { // Check if the scene-link item is a valid scenePanel
                                                    String scenePanelVisual = (String) ShowControl.getInstance().getMap("scenes").get("scenes."+args[4]+"."+scenePanel); // Get the value of the scenePanel

                                                    // Change the scenePanel of all valid Panels
                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                                            "replaceitem entity @e[type=armor_stand,name="+args[3]+",tag="+scenePanel+"] slot.armor.head minecraft:iron_sword 1 0 {CustomModelData:"+scenePanelVisual+"}");
                                                }
                                            }
                                        }
                                        scenePanel.build(controlPanel,args[3]);
                                        chat.send(player, controlPanel);
                                        controlPanel.clear();
                                    } else {
                                        ShowControl.getInstance().log("There is no scene with the name:"+ args[4]);
                                    }
                                    break;
                                default:
                                    sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control screen <change>");
                                    sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Controlling screens from chat");
                                    break;
                            }
                        break;
                        case "scene":
                            if(args.length == 2) {
                                sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control scene <panel>");
                                sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Controlling scenes from chat");
                                return true;
                            }
                            switch(args[2]) {
                                case "panel":
                                    if(args.length == 3) {
                                        sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control scene panel <screen name>");
                                        sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Controlling the scenes of a screen from chat");
                                        return true;
                                    }
                                    scenePanel.build(controlPanel,args[3]);
                                    chat.send(player, controlPanel);
                                    controlPanel.clear();
                                    break;
                                default:
                                    sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control scene <panel>");
                                    sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Controlling scenes from chat");
                                    break;
                            }
                            break;
                        default:
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc control <fx/screen>");
                            sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Controlling effects via chat");
                        break;
                    }
                break;
                case "chat":
                    if(args.length == 1) {
                        sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc chat <mute/unmute>");
                        return true;
                    }
                    switch(args[1]) {
                        case "mute":
                            player.sendTitle(ChatColor.GRAY + "Chat: " + ChatColor.GREEN + "muted", ChatColor.DARK_GRAY + "Incoming messages are now muted", 0, 50, 10);

                            mute.muteChat();
                            mute.add(player.getName());
                            break;
                        case "unmute":
                            player.sendTitle(ChatColor.GRAY + "Chat Controls: " + ChatColor.RED + "unmuted", ChatColor.DARK_GRAY + "Incoming messages are now unmuted", 0, 50, 10);
                            mute.remove(player.getName());
                            break;
                    }
                break;
                case "reload":
                    sender.sendMessage(chatPrefix + "Disabling all running fx...");
                    ShowControl.getInstance().getLinkStatus().clear();
                    sender.sendMessage(chatPrefix + "Reloading configs...");
                    ShowControl.getInstance().reloadConfig("controlPanel.yml");
                    ShowControl.getInstance().setYML(ShowControl.getInstance().getYML("generic"),"controlPanel");
                    ShowControl.getInstance().setMap(ShowControl.getInstance().getYML("controlPanel").getValues(true),"controlPanel");

                    ShowControl.getInstance().reloadConfig("emitters.yml");
                    ShowControl.getInstance().setYML(ShowControl.getInstance().getYML("generic"),"emitters");
                    ShowControl.getInstance().setMap(ShowControl.getInstance().getYML("emitters").getValues(true),"emitters");

                    ShowControl.getInstance().reloadConfig("effects.yml");
                    ShowControl.getInstance().setYML(ShowControl.getInstance().getYML("generic"),"effects");
                    ShowControl.getInstance().setMap(ShowControl.getInstance().getYML("effects").getValues(true),"effects");

                    ShowControl.getInstance().reloadConfig("screens.yml");
                    ShowControl.getInstance().setYML(ShowControl.getInstance().getYML("generic"),"screens");
                    ShowControl.getInstance().setMap(ShowControl.getInstance().getYML("screens").getValues(true),"screens");

                    ShowControl.getInstance().reloadConfig("scenes.yml");
                    ShowControl.getInstance().setYML(ShowControl.getInstance().getYML("generic"),"scenes");
                    ShowControl.getInstance().setMap(ShowControl.getInstance().getYML("scenes").getValues(true),"scenes");

                    sender.sendMessage(chatPrefix + ChatColor.GREEN + "Finished!");
                break;
                case "commit":
                    sender.sendMessage(chatPrefix + "Committing changes to configs...");

                    ShowControl.getInstance().saveConfig("emitters.yml",ShowControl.getInstance().getMap("emitters"));

                    sender.sendMessage(chatPrefix + ChatColor.GREEN + "Finished!");
                break;
                case "emitter":
                    if(args.length == 1) {
                        sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc emitter <add/remove>");
                        return true;
                    }
                    switch(args[1]) {
                        case "add":
                            if(args.length != 8) {
                                sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc emitter add <link-id> <effect-id> <world> <x> <y> <z>");
                                sender.sendMessage(chatPrefix + ChatColor.GRAY + "Usage: Creates an emitter");
                                return true;
                            }
                            int emittersTopID = 0;
                            for (Map.Entry<String, Object> emittersLoopMap : ShowControl.getInstance().getMap("emitters").entrySet()) {
                                if (emittersLoopMap.getKey().matches("emitters\\.[0-9]*")) {
                                    emittersTopID = Integer.parseInt(emittersLoopMap.getKey().replaceAll("emitters\\.([0-9]*)","$1"));
                                }
                            }
                            try {
                                emittersTopID++;
                                ShowControl.getInstance().getMap("emitters").put("emitters."+emittersTopID,null);
                                ShowControl.getInstance().getMap("emitters").put("emitters."+emittersTopID+".link-id",Integer.parseInt(args[2]));
                                ShowControl.getInstance().getMap("emitters").put("emitters."+emittersTopID+".effect-id",Integer.parseInt(args[3]));
                                ShowControl.getInstance().getMap("emitters").put("emitters."+emittersTopID+".loc.world",args[4]);
                                ShowControl.getInstance().getMap("emitters").put("emitters."+emittersTopID+".loc.x",Double.parseDouble(args[5]) + 0.5);
                                ShowControl.getInstance().getMap("emitters").put("emitters."+emittersTopID+".loc.y",Double.parseDouble(args[6]));
                                ShowControl.getInstance().getMap("emitters").put("emitters."+emittersTopID+".loc.z",Double.parseDouble(args[7]) + 0.5);
                            } catch(NullPointerException e) {
                                sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc emitter add <link-id> <effect-id> <world> <x> <y> <z>");
                                return false;
                            }
                            sender.sendMessage(chatPrefix + ChatColor.GREEN + "Emitter added! Don't forget to use "+ChatColor.AQUA+"/sc commit"+ChatColor.GREEN+" to save your changes!");
                        break;
                        case "remove":
                            sender.sendMessage(chatPrefix + ChatColor.RED + "This command has not yet been implemented into ShowControl.");
                            sender.sendMessage(chatPrefix + ChatColor.GRAY + "Instead, go into your emitters.yml config file and make this change yourself!");
                        break;
                    }
                break;
                default:
                    sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /sc <commit/emitter/reload>");
                break;
            }
        }
        return true;
    }

}

package group.aelysium.particulaterenderer.command;

import group.aelysium.particulaterenderer.lib.effects.Effect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CommandFx implements CommandExecutor {
    private final String chatPrefix = ChatColor.DARK_GRAY+"["+ChatColor.AQUA+"ShowControl"+ChatColor.DARK_GRAY+"]: "+ChatColor.GRAY;
    private Location location;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


            if(args.length > 3) {
                try {
                    Effect.Type.valueOf(args[0]);
                    if (sender instanceof Player) {
                        Player player = ((Player) sender).getPlayer();
                        location = new Location(player.getWorld(),Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
                    } else {
                        location = new Location(Bukkit.getWorld(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4]));
                    }

                    Effect.Builder builder = Effect.builder().location(location);
                    for(int i = 3; args.length > i; i++) {
                        builder.argument(args[i].replaceAll("([A-z\\-]*):[A-z\\-]*","$1"),args[i].replaceAll("[A-z\\-0-9]*:([A-z\\-0-9]*)","$1"));
                    }
                    Effect.getInstance().run(builder.build(),"TEMP");
                } catch(IllegalArgumentException e) {
                    sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /fx <particle/block/firework/dragon/crystal/lazer/status> [w] <x> <y> <z> [Arguments:Value]");
                } catch (Throwable throwable) {
                    switch (args[1].toLowerCase(Locale.ROOT)) {
                        case "particle":
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /fx particle [w] <x> <y> <z> [particle/<dx/dy/dz>/speed/count]");
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Arguments should be formatted as \"Argument:Value\". Example: \"particle:FLAME\" ");
                            break;
                        case "block":
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /fx block [w] <x> <y> <z> [block]");
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Arguments should be formatted as \"Argument:Value\". Example: \"block:STONE\" ");
                            break;
                        case "firework":
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /fx firework [w] <x> <y> <z> [shape/<r/g/b>/life/flicker/trail/fade]");
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Arguments should be formatted as \"Argument:Value\". Example: \"shape:SMALL_BALL\" ");
                            break;
                        case "dragon":
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Dragon effect is currently unavailable.");
                            break;
                        case "crystal":
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /fx crystal [w] <x> <y> <z> [<TargetX/TargetY/TargetZ>]");
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Arguments should be formatted as \"Argument:Value\". Example: \"targetX:0.0\" ");
                            break;
                        case "lazer":
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Lazer effect is currently unavailable.");
                            break;
                        case "status":
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /fx status [w] <x> <y> <z> [potion/time/strength/global/<areaX/areaY/areaZ>]");
                            sender.sendMessage(chatPrefix + ChatColor.RED + "Arguments should be formatted as \"Argument:Value\". Example: \"potion:INVISIBILITY\" ");
                            break;
                    }
                    throwable.printStackTrace();
                }
            } else {
                sender.sendMessage(chatPrefix + ChatColor.RED + "Proper usage: /fx <particle/block/firework/dragon/crystal/lazer/status> [w] <x> <y> <z> [Arguments:Value]");
                sender.sendMessage(chatPrefix + ChatColor.RED + "Arguments should be formatted as \"Argument:Value\". Example: \"block:STONE\" ");
            }
        return true;
    }
}

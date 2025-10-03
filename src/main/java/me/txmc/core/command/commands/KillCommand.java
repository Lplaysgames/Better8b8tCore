package me.txmc.core.command.commands;

import me.txmc.core.Main;
import me.txmc.core.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.txmc.core.util.GlobalUtils.sendMessage;

/**
 * Kill command that allows players to kill themselves or others (with permission)
 * Code ported from Kill Plugin
 * @author Libalpm (MindComplexity)
 * @since 10/2/2025
 */
public class KillCommand extends BaseCommand {
    
    private final Main plugin;
    
    public KillCommand(Main plugin) {
        super(
                "kill",
                "/kill [player]",
                new String[]{"minecraft.command.kill", "8b8tcore.command.kill.others"},
                "Kill yourself or another player",
                new String[]{"<player>::Kill another player"}
        );
        this.plugin = plugin;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sendMessage(sender, "&cYou must specify a player name when using this command from console!");
                return;
            }
            
            if (!hasPermission(sender, "minecraft.command.kill")) {
                sendNoPermission(sender);
                return;
            }
            
            killPlayer(player, player);
            return;
        }
        
        if (!hasPermission(sender, "8b8tcore.command.kill.others")) {
            sendNoPermission(sender);
            return;
        }
        
        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        
        if (target == null) {
            sendMessage(sender, "&cPlayer '&e" + targetName + "&c' is not online!");
            return;
        }
        
        killPlayer(sender, target);
    }
    
    private void killPlayer(CommandSender sender, Player target) {
        target.setHealth(0);
        
        if (sender.equals(target)) {
            sendMessage(sender, "&6You have killed yourself!");
        } else {
            sendMessage(sender, "&6You have killed &e" + target.getName() + "&6!");
            sendMessage(target, "&cYou have been killed by &e" + sender.getName() + "&c!");
        }
    }
    
    private boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission) || sender.isOp() || sender.hasPermission("*");
    }
}

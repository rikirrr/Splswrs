package org.ynovka.ru.spellswar.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class startgameCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        // /startgame
        if (strings.length == 1) {
            return List.of(
                "short",
                "medium",
                "long"
            );
        }
        if (strings.length == 2) {
            return List.of(
                    "random",
                    "select"
            );
        }


        return null;
    }
}

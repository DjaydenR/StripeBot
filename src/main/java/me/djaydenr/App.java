package me.djaydenr;


import io.github.cdimascio.dotenv.Dotenv;
import me.djaydenr.commands.BetalingStarten;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.security.auth.login.LoginException;

public class App {

    public static void main(String[] args) throws LoginException, InterruptedException {
        Dotenv dotenv = Dotenv.load();
        JDA jda = JDABuilder.createDefault(dotenv.get("discord-bot-token"))
                .addEventListeners(new BetalingStarten())
                .build();

        jda.upsertCommand("betaling", "Maak een betaling")
                .addOption(OptionType.STRING, "product", "De naam van het product", true)
                .addOption(OptionType.INTEGER, "prijs", "De prijs zijn in euro centen", true)
                .queue();

    }

}
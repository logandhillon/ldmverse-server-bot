package net.ldm.ldmverse_server_bot;

import net.ldm.ldmverse_server_bot.bot.init.BotHandler;
import net.ldm.ldmverse_server_bot.bot.json.BotConfig;
import net.ldm.ldmverse_server_bot.core.crash.CrashExceptionHandler;
import net.ldm.ldmverse_server_bot.core.resource.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.Scanner;

public class Main {
    public static BotConfig BOT_CONFIG;
    private static final Scanner IN = new Scanner(System.in);
    private static final Logger LOG = LoggerContext.getContext().getLogger(Main.class);
    public static final String TITLE = "The LDMVerse's Dedicated Bot";

    public static void main(String[] args) {
        LOG.info("Starting application");

        Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler());

        try {
            BOT_CONFIG = FileUtils.readJson("bot.json", BotConfig.class);
        } catch (Exception e) {
            System.out.println("It seems you don't have your config setup. Let's do that!");
            System.out.print("Bot token? ");
            String token = IN.nextLine();

            BOT_CONFIG = new BotConfig(token);

            if (!FileUtils.saveJson("bot.json", BOT_CONFIG)) {
                LOG.fatal("Something went wrong while writing the bot config to bot.json");
                System.exit(-1);
            }
            LOG.info("Bot config created, file saved");
        }

        if (BOT_CONFIG.token == null || BOT_CONFIG.token.isEmpty()) {
            LOG.error("Bot token is missing or corrupt.");
            System.out.println("Your bot token is missing or corrupt. Please paste it below.");
            BOT_CONFIG.token = IN.nextLine();
            FileUtils.saveJson("bot.json", BOT_CONFIG);
        }

        BotHandler.start();
    }
}
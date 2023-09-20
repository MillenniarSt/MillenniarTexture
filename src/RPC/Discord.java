package io.github.MillenniarSt.MillenniarTexture.RPC;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

/*            //////////////////////////////////
 *           //								    //
 *           //    //          //                //
 *           //    ////      ////    //////      //
 *           //	  //  //  //  //  //            //
 *           //	  //    //    //  //            //
 *           //	  //          //    //////      //
 *           //    //          //          //	//
 *           //    //          //          //    //
 *           //    //          //  ////////      //
 *           //                                  //
 *             /////// Millenniar Studios ///////
 */

public class Discord {

    private static String discordID = "1153675177096388710";
    private static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.details = "Making Minecraft Texture Pack";
        discordRichPresence.largeImageKey = "icon";
        discordRichPresence.state = null;
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}

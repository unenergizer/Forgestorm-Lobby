package com.forgestorm.lobby.constants;

import org.bukkit.ChatColor;

/*********************************************************************************
 *
 * OWNER: Robert Andrew Brown & Joseph Rugh
 * PROGRAMMER: Robert Andrew Brown & Joseph Rugh
 * PROJECT: forgestorm-lobby
 * DATE: 6/21/2017
 * _______________________________________________________________________________
 *
 * Copyright © 2017 ForgeStorm.com. All Rights Reserved.
 *
 * No part of this project and/or code and/or source code and/or source may be 
 * reproduced, distributed, or transmitted in any form or by any means, 
 * including photocopying, recording, or other electronic or mechanical methods, 
 * without the prior written permission of the owner.
 */

public enum LobbyMessages {

    PLAYER_WELCOME_1("&e&lForgeStorm: &r&lRPG MINIGAME SERVER %s"),
    PLAYER_WELCOME_2("&7&nhttp://www.ForgeStorm.com/"),
    PLAYER_WELCOME_3("&c/help &e/mainmenu &a/settings &b/playtime &d/lobby");

    private final String message;

    //Constructor
    LobbyMessages(String message) {
        this.message = color(message);
    }

    /**
     * Sends a string representation of the enumerator item.
     */
    public String toString() {
        return message;
    }

    /**
     * Converts special characters in text into Minecraft client color codes.
     * <p>
     * This will give the messages color.
     * @param msg The message that needs to have its color codes converted.
     * @return Returns a colored message!
     */
    private static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
package com.forgestorm.lobby.display;

import com.forgestorm.lobby.Lobby;
import com.forgestorm.spigotcore.constants.FilePaths;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameTipAnnouncer {

	//Setup instance variables.
	private final Lobby plugin;

	private String filePath;

	//Configuration file.
	private File configFile;
	private FileConfiguration gameTipConfig;

	//An array of strings.
	private List<String> tips;
	private int tipDisplayed = 0;
	private int numberOfTips;

	//Setup regeneration variables.
	private int gameTipTime = 60 * 4;			//Time it takes for a message to show up. 60 seconds * 15 (15 minutes)

	//Setup GameTipManager.
	public GameTipAnnouncer(Lobby plugin) {
		this.plugin = plugin;

		filePath = FilePaths.GAME_TIP_ANNOUNCER.toString();

		//If fishing configuration does not exist, create it. Otherwise lets load the config.
		if(!(new File(filePath)).exists()){
			createConfig();
		} else {
			//lets load the configuration file.
			configFile = new File(filePath);
			gameTipConfig =  YamlConfiguration.loadConfiguration(configFile);
		}

		//Load messages form configuration.
		loadMessages();

		//Start the timer used to reset blocks.
		startTipMessages();
	}

	/**
	 * This starts the thread that will loop over and over displaying tips and
	 * other useful information to the player.
	 */
	private void startTipMessages() {

		numberOfTips = tips.size();

		//Start a repeating task.
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {

				String gameTip = tips.get(tipDisplayed);

				//Show the tip.
				sendPlayerTips(ChatColor.YELLOW + "" + ChatColor.BOLD + "Tip" 
						+ ChatColor.YELLOW + " #"
						+ Integer.toString(tipDisplayed + 1)
						+ ChatColor.DARK_GRAY + ChatColor.BOLD + ": " 
						+ ChatColor.WHITE + gameTip  
						+ ChatColor.DARK_GRAY + ".");

				//Setup to display the next tip.
				if ((tipDisplayed + 1) == numberOfTips) {
					//Reset the tip count.  All tips have been displayed.
					tipDisplayed = 0;
				} else {
					//Increment the tip count to display the next tip.
					tipDisplayed++;
				}
			}
		}, 0, gameTipTime * 20);
	}

	/**
	 * Sends the tip directly to the player.
	 * @param message The message to send to the player.
	 */
	private void sendPlayerTips(String message) {
		for (Player players : Bukkit.getOnlinePlayers()) {
			
			//Prevent the player from getting tips if they are in a tutorial.
			if (!(plugin.getSpigotCore().getProfileManager().getProfile(players).isInTutorial())) {
				//Send Message
				players.sendMessage(message);

				//Play Sound
				players.playSound(players.getEyeLocation(), Sound.UI_BUTTON_CLICK, .5F, .2f);
			}
		}
	}

	/**
	 * Load all messages from configuration file.
	 * 
	 * @return A list of all tips (messages) in the configuration file.
	 */
	private void loadMessages() {
		configFile = new File(filePath);
		gameTipConfig =  YamlConfiguration.loadConfiguration(configFile);
		tips = gameTipConfig.getStringList("GameTips");
	}

	/**
	 * This creates the configuration file that has the EXP leveling requirements.
	 */
	private void createConfig() {

		configFile = new File(filePath);
		gameTipConfig =  YamlConfiguration.loadConfiguration(configFile);

		//Loop through and create each level for fishing.
		gameTipConfig.set("GameTips", "GameTips");
		gameTipConfig.set("GameTips", "This is a test message.");

		try {
			gameTipConfig.save(configFile);	//Save the file.
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}

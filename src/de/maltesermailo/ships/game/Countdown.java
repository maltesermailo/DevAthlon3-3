package de.maltesermailo.ships.game;

import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import de.maltesermailo.ships.ShipsPlugin;

public class Countdown {
	
	private static int countTime = 61;
	
	private static BukkitTask task; 
	
	public static void start(Callable<Void> callable) {
		if(Countdown.task != null) {
			return;
		}
		
		Countdown.countTime = 61;
		
		Countdown.task = Bukkit.getScheduler().runTaskTimer(ShipsPlugin.instance(), () -> {
			Countdown.countTime--;
			
			Bukkit.getOnlinePlayers().forEach(p -> p.setLevel(countTime));
			
			if(Countdown.countTime <= 0) {
				Countdown.stop();
				
				if(Bukkit.getOnlinePlayers().size() < 2) {
					Bukkit.broadcastMessage(ShipsPlugin.instance().getPrefix() +
							"§7Da zu wenige Spieler online sind, wird der Countdown neugestart.");
					
					Countdown.start(callable);
				} else {
					try {
						callable.call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if(Countdown.countTime == 30 || Countdown.countTime == 15 || Countdown.countTime <= 10) {
				Bukkit.broadcastMessage(String.format("§8[§cShips§8] §7Noch §6%d Sekunde%s", Countdown.countTime,
						Countdown.countTime == 1 ? "" : "n"));
			}
		}, 20L, 20L);
	}

	public static void stop() {
		if(Countdown.task != null) {
			Countdown.task.cancel();
			
			Countdown.task = null;
		}
	}
	
	public static void setCountTime(int countTime) {
		Countdown.countTime = countTime;
	}

}

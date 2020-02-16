package red.jad.tnt5050;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TNT5050 extends JavaPlugin implements Listener {
	@EventHandler
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		// Chance
		if(Math.random() > getConfig().getDouble("chance")) return;
		
		// Variables
		World w = e.getBlock().getWorld();
		Location l = e.getBlock().getLocation().add(0.5, 0, 0.5);
		
		// Effects
		w.playSound(l, Sound.ENTITY_TNT_PRIMED, 1.0F, 1.0F);
		w.spawnParticle(Particle.CLOUD, l, 10, 0, 0.5, 0, 0.1);
		
		// TNT Spawning
		TNTPrimed tnt = (TNTPrimed) w.spawn(l, TNTPrimed.class);
		tnt.setCustomNameVisible(true);
		
		/// Strength
		Random r = new Random();
		int max = getConfig().getInt("strengthMax");
		int min = getConfig().getInt("strengthMin");
		int strength = r.nextInt((max - min) + 1) + min;
		tnt.setFuseTicks(strength * getConfig().getInt("fuseMultiplier"));
		tnt.setCustomName(strength + "");
	}
	
	@EventHandler
	public void onExplode(ExplosionPrimeEvent e) {
		if(e.getEntity().getType() == EntityType.PRIMED_TNT) {
			TNTPrimed tnt =  (TNTPrimed) e.getEntity();
			if(tnt.getCustomName() != null) e.setRadius(Integer.parseInt(tnt.getCustomName()));
		}
	}
}

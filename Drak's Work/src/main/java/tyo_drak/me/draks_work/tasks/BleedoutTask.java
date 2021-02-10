package tyo_drak.me.draks_work.tasks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BleedoutTask extends BukkitRunnable {

    private LivingEntity victim;

    public BleedoutTask(LivingEntity victim){
        this.victim = victim;
    }

    private BukkitTask task;

    @Override
    public void run() {
            if(victim.getHealth() > 1) {
                victim.setHealth(victim.getHealth()-1);
            } else {
                victim.setHealth(0);
                cancel();
            }
    }

}

package tyo_drak.draksnightmare.misc;

import org.bukkit.Server;

import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class DraksTime {

    //<editor-fold defaultstate="" desc="TIME">
    public static boolean hasPassedSince(int seconds, long initialTime) {
        return getTimeRemaining(seconds, initialTime) <= 0;
    }

    public static String getFormattedTimeRemaining(int seconds, Long timePrevious) {
        return formatTime(getTimeRemaining(seconds, timePrevious));
    }

    public static String formatTime(long time) {
        String formattedTime;
        if (time >= 3600) {
            formattedTime = time / 3600 + " hora(s), ";
            formattedTime = formattedTime + (time % 3600) / 60 + " minuto(s) e ";
            formattedTime = formattedTime + time % 60 + " segundo(s)";
        } else if (time >= 60) {
            formattedTime = time / 60 + " minuto(s) e ";
            formattedTime = formattedTime + time % 60 + " segundo(s)";
        } else if (time > 0) {
            formattedTime = time + " segundos";
        } else {
            formattedTime = "0 segundos";
        }
        return formattedTime;
    }

    public static long getTimeRemaining(long seconds, long timePrevious) {
        return seconds - (getTimeSeconds() - timePrevious);
    }

    public static long getTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }
    //</editor-fold>

    public static boolean isDayOnWorld() {
        Server server = getServer();
        long time = Objects.requireNonNull(server.getWorld("world")).getTime();
        return (time > 0 && time < 13000);
    }
}

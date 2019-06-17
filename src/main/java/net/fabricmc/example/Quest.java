package net.fabricmc.example;

import net.minecraft.entity.player.PlayerEntity;

public abstract class Quest {
    static void run() {}

    public void locationGoal(PlayerEntity player, int x, int y, int z, int dist) {
        double xDist = 0;
        double yDist = 0;
        double zDist = 0;
        do {
            xDist = Math.abs(player.x - x);
            yDist = Math.abs(player.y - y);
            zDist = Math.abs(player.z - z);
            System.out.println("Location here.");
        } while (xDist > dist || yDist > dist || zDist > dist);
        System.out.println("Location here.");
    }
}


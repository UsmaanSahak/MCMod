package net.fabricmc.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Map;

public class IntroQuest extends Quest {
    MinecraftClient client = MinecraftClient.getInstance();
    PlayerEntity player = client.player;
    IntroQuest(Map<String,String> args) {
        System.out.println("Intro Quest!");
        if (args.get("introduced") == "no") {
            //get player's location
            //wait until they get into certain location bounding box
            locationGoal(player,0,0,0,10);
        } if (args.get("foundEnemy") == "no") {
        } if (args.get("inTheWild") == "no") {

        }
    }
}

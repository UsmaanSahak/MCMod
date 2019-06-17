package net.fabricmc.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.*;

public class GameStory {
    Map<String,String> adv = new HashMap<>();
    Map<String,Runnable> questList = new HashMap<>();
    Map<String,String> args = new HashMap<>();

    GameStory() {
        System.out.println("GameStory initialized.");
        //Initialize questList with all quests.
        questList.put("IntroQuest",() -> new IntroQuest(args));
        //questList.put("GoVillageQuest",GoVillageQuest::new);
        //read and initialize adventure Map object.
        String[] line;
        String l;
        int argNum;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Adventure"));
            line = reader.readLine().split(",");


            while (line != null) { //<type>, <status>, <name>, [<lines of arguments>]
                System.out.println(line.toString());
                if (line[0] == "quest" && line[1] == "inProgress") {
                    argNum = Integer.parseInt(line[3]);
                    args.clear();
                    for (int i = 0; i < argNum; i++) {
                        String[] arg = reader.readLine().split(",");
                        args.put(arg[0],arg[1]);
                    }
                    System.out.println("Starting Quest!");
                    questList.get(line[2]).run();
                }

                l = reader.readLine();
                if(l != null) {
                    line = l.split(",");
                } else {
                    line = null;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}


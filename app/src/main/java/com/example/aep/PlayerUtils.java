package com.example.aep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerUtils {
    public static Player convertHashMapToPlayer(Map<String, Object> map) {
        String email = (String) map.get("email");
        String full_name = (String) map.get("full_name");
        String password = (String) map.get("password");
        String user_type = (String) map.get("user_type");

        return new Player(email, full_name, password, user_type);
    }
    public static List<Player> convertHashMapListToPlayerList(List<HashMap<String, Object>> hashMapList) {
        List<Player> playerList = new ArrayList<>();

        for (HashMap<String, Object> hashMap : hashMapList) {
            Player player = convertHashMapToPlayer(hashMap);
            playerList.add(player);
        }

        return playerList;
    }
}

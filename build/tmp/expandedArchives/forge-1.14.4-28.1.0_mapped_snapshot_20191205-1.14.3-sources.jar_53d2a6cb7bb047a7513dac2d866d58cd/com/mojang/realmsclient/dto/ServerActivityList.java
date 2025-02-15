package com.mojang.realmsclient.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ServerActivityList extends ValueObject {
   public long periodInMillis;
   public List<ServerActivity> serverActivities = new ArrayList<>();

   public static ServerActivityList parse(String p_parse_0_) {
      ServerActivityList serveractivitylist = new ServerActivityList();
      JsonParser jsonparser = new JsonParser();

      try {
         JsonElement jsonelement = jsonparser.parse(p_parse_0_);
         JsonObject jsonobject = jsonelement.getAsJsonObject();
         serveractivitylist.periodInMillis = JsonUtils.func_225169_a("periodInMillis", jsonobject, -1L);
         JsonElement jsonelement1 = jsonobject.get("playerActivityDto");
         if (jsonelement1 != null && jsonelement1.isJsonArray()) {
            for(JsonElement jsonelement2 : jsonelement1.getAsJsonArray()) {
               ServerActivity serveractivity = ServerActivity.parse(jsonelement2.getAsJsonObject());
               serveractivitylist.serverActivities.add(serveractivity);
            }
         }
      } catch (Exception var10) {
         ;
      }

      return serveractivitylist;
   }
}
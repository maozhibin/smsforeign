package com.cunyun.smsforeign.common;

import java.util.*;

/**
 * 加权算法
 */
public class WeightRandom {

    /**
     * 加权随机算法
     * @param serverWeigthMap
     * @return
     */
    public static String weightRandom(Map<String,Integer> serverWeigthMap)
    {
        if(serverWeigthMap.size()==0){
            return "";
        }
        //重新建立一個map,避免出現由於服務器上線和下線導致的並發問題
        Map<String,Integer> serverMap  = new HashMap<String,Integer>();
        serverMap.putAll(serverWeigthMap);
        //獲取ip列表list
        Set<String> keySet = serverMap.keySet();
        Iterator<String> it = keySet.iterator();

        List<String> serverList = new ArrayList<String>();

        while (it.hasNext()) {
            String server = it.next();
            Integer weight = serverMap.get(server);
            for (int i = 0; i < weight; i++) {
                serverList.add(server);
            }
        }
        Random random = new Random();
        int randomPos = random.nextInt(serverList.size());

        String server = serverList.get(randomPos);
        return server;
    }




    public static void main(String[] args) {
        Map<String,Integer> serverWeigthMap = new HashMap<>();
//        serverWeigthMap.put("192.168.1.12", 1);
//        serverWeigthMap.put("192.168.1.13", 1);
//        serverWeigthMap.put("192.168.1.14", 2);
//        serverWeigthMap.put("192.168.1.15", 2);
//        serverWeigthMap.put("192.168.1.16", 3);
//        serverWeigthMap.put("192.168.1.17", 3);
//        serverWeigthMap.put("192.168.1.18", 1);
//        serverWeigthMap.put("192.168.1.19", 100);
        String serverIp = weightRandom(serverWeigthMap);
        System.out.println(serverIp);
    }
}

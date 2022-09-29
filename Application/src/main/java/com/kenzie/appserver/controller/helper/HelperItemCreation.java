package com.kenzie.appserver.controller.helper;

import com.kenzie.appserver.service.model.Item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class HelperItemCreation {
    public static void main(String[] args){
        List<Item> results = createSampleSongList();
        System.out.println(results);

    }
    // Static method returns a List of SongResponses for the sample
    public static List<Item> createSampleSongList() {
        // Get the parsed out song data
        ArrayList<String[]> songData = getSongLinesFromCSV();

        // Turn it into SongResponses
        List<Item> results = new ArrayList<>();

        int i = 0;

        for(String[] entry : songData){
            if(i != 0){
                String itemId = entry[0];
                itemId = itemId.replaceAll("^\"|\"$", "");

                String description = entry[1];
                description = description.replaceAll("^\"|\"$", "");

                String currentQty = entry[2];
                currentQty = currentQty.replaceAll("^\"|\"$", "");

                String reorderQty = entry[3];
                reorderQty = reorderQty.replaceAll("^\"|\"$", "");

                String qtyTrigger = entry[4];
                qtyTrigger = qtyTrigger.replaceAll("^\"|\"$", "");

                String orderDate = entry[5];
                orderDate = orderDate.replaceAll("^\"|\"$", "");

                Item item = new Item(itemId,
                        description,
                        currentQty,
                        reorderQty,
                        qtyTrigger,
                        orderDate);

                results.add(item);
            }
            i++;
        }

        // Return it
        return results;
    }

    // Helper methods
    private static ArrayList<String[]> getSongLinesFromCSV() {
        ArrayList<String[]> results = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Dan Work\\Desktop\\kenzieATA\\ata-capstone-project-DanJSun\\Application\\src\\main\\java\\com\\kenzie\\appserver\\controller\\helper\\results.csv"));

            String line;
            while ((line = reader.readLine()) != null){
                results.add(line.split(","));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}

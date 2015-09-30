package uk.ac.ncl.cc.weka;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 14/07/15.
 */

public class ReadJSONWriteToFile {

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            FileInputStream fis = new FileInputStream(new File("tweets_sample.json"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            FileOutputStream fos = new FileOutputStream(new File("test.txt"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            int count = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                String text = (String) jsonObject.get("text");
                String lang = (String) jsonObject.get("lang");
                if (lang.equals("pt")) {
                    count++;
                    bw.write(text.replaceAll("[\\t\\n\\r]+", " "));
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
            System.out.println("count :" + count);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

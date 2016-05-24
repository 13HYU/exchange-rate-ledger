package ledger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ExchangeRate {
   String currency;
   public int year;
   public String month;
   public String date;
   public float value;
   public float won;
   public float dollar;
      
   ExchangeRate(String c, int y, int m, int d){
      this.currency = c;
      this.year = y;
       
       if(m<10)
          this.month = "0"+Integer.toString(m); 
       else
          this.month = Integer.toString(m);
       
       if(d<10)
          this.date = "0"+Integer.toString(d);
       else
          this.date = Integer.toString(d);
       
       String ymd = year+month+date;
      
      JSONParser parser = new JSONParser();
   
      
         try {
            FileReader f = new FileReader("C:\\Download\\"+ymd+".json");
 
            Object obj = parser.parse(f);
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject listObject = (JSONObject) jsonObject.get("list");
            
            JSONArray jarray = (JSONArray) listObject.get("resources");
      
      
            for(int i=0; i<jarray.size(); i++){
               JSONObject resourceObject = (JSONObject)jarray.get(i);
            
               JSONObject rObject = (JSONObject)resourceObject.get("resource");
            
               JSONObject fieldObject = (JSONObject)rObject.get("fields");
            
                if(fieldObject.get("symbol").toString().equals(new String(currency+"=X"))){
                  value = Float.parseFloat(fieldObject.get("price").toString());
                }
                
                else if(fieldObject.get("symbol").toString().equals("KRW=X")){
                  won = Float.parseFloat(fieldObject.get("price").toString());
                }
                else
                   continue;
            }
      
       
         } 
         catch (FileNotFoundException e1) {
            e1.printStackTrace();
         } 
         catch (IOException e2) {
            e2.printStackTrace();
         } 
         catch (ParseException e3) {
            e3.printStackTrace();
         }
 
      
   }
}
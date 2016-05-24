package ledger;

import java.net.*;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import java.io.*;


public class UrlDownload {
    public int year;
    public String month;
    public String date;
    
    public UrlDownload(int y, int m, int d){
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
          
           try {

               Thread.sleep(500);
               try{
                  FileInputStream fis = new FileInputStream("C:\\Download\\"+ymd+".json");
               }
               catch (FileNotFoundException e) {
                  URL u = new URL("http://finance.yahoo.com/connection/currency-converter-cache?date="+ymd);

                  File filePath = new File("C:\\Download\\");

                  File fileDir = filePath.getParentFile();

                  filePath.mkdirs();

                  FileOutputStream fos = new FileOutputStream("C:\\Download\\"+ymd+"_.json");

                  InputStream is = u.openStream();

                  byte[] buf = new byte[1024];

                  double len = 0;

                  double tmp = 0;

                  double percent = 0;

                  while ((len = is.read(buf)) > 0) {

                     tmp += len / 1024 / 1024;
                     percent = tmp / 1229 * 100;
                     fos.write(buf, 0, (int) len);

                  }

                  fos.close();

                  is.close();

                  Runtime rt = Runtime.getRuntime();

            
                  int modifyline = 1;
                  BufferedReader in = new BufferedReader(new FileReader("C:\\Download\\"+ymd+"_.json"));
                  PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Download\\"+ymd+".json")));
                  String line;
                  int count = 1;
                  while((line = in.readLine())!=null){
                     if(count==modifyline){
                        out.println(line.replace("/**/YAHOO.Finance.CurrencyConverter.addConversionRates(",""));   
                     }
                     else if(line.equals(");"))
                  line.replace(");", "");
                     else
                        out.println(line);
               count++;
                  }
                  in.close();
                  out.close();
            

               //System.out.println(e.getMessage());
               } catch (IOException e) {
                   System.out.println(e.getMessage());
               }

               
               
                              
               

           } catch (Exception e) {

               System.out.println("다운로드 오류입니다. 나중에 다시 받아보세요.");

           }
    }
       
}
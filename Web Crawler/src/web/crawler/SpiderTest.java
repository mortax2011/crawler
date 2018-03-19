/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.crawler;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.util.Collections.list;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mohamedmortada
 */

    public class SpiderTest
{
    /**
     * This is our test. It creates a spider (which creates spider legs) and crawls the web.
     * 
     * @param args
     *            - not used
     */
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
     Atomicvariables var =new   Atomicvariables(); 
     //  System.out.println("enter number of threads you want");
       // Scanner in = new Scanner(System.in);
    //    int numberofthreads = in.nextInt();
    
            List<String> urls=new LinkedList<String>();
       int numberofthreads=0;
       try(BufferedReader br = new BufferedReader(new FileReader("start.txt"))) {
    StringBuilder sb = new StringBuilder();
    String line = br.readLine();

   if (line != null) {
        numberofthreads=Integer.parseInt(line);
               line = br.readLine();
   }
    while (line != null) {
      
       
        urls.add(line);
        line = br.readLine();
    }
  
}
      
        ExecutorService executor = Executors.newFixedThreadPool(numberofthreads);  
      
        for (int i = 0; i <numberofthreads ; i++) {  
            Runnable worker = new Spiders( urls.get(i),var);  
            executor.execute(worker);//calling execute method of ExecutorService  
          }
        executor.shutdown();  
        while (!executor.isTerminated()) {   }  
        System.out.println("Finished all threads");  
    }  
        
        
    
       
    }




    class Spiders implements Runnable{
     String urllink ;
 
     Atomicvariables var;
        public Spiders(String url,Atomicvariables x)
     {
         this.var=x ;
         this.urllink=url;
      
     } 

	public void run () {
        Spider spider = new Spider(var);
         try {
             spider.searchofthreads(urllink);
         } catch (Exception ex) {
             Logger.getLogger(Spiders.class.getName()).log(Level.SEVERE, null, ex);
         }
	}
    
    }

    


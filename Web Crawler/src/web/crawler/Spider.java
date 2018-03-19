/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author mohamedmortada
 */
public class Spider {
 // Fields
 private  Atomicvariables pagesvisited_pagecount;
    private List<String> pagesToVisit = new LinkedList<String>();
    /**
     * @param args the command line arguments
     */
  public Spider(Atomicvariables x)
  {
      pagesvisited_pagecount=x;

  }
    private String nextUrl() throws Exception
    {
        String nextUrl;
        do
        {
            nextUrl = this.pagesToVisit.remove(0);
        } while(this.pagesvisited_pagecount.getvisited().contains(nextUrl));
            int x=  this.pagesvisited_pagecount.getvisited().size();
                  this.pagesvisited_pagecount.getvisited().add(nextUrl);
                     int y=  this.pagesvisited_pagecount.getvisited().size();
                     if(y>x){
                  pagesvisited_pagecount.increment();
                      savelistvisited( this.pagesvisited_pagecount.getvisited());                
                     }
        return nextUrl;
    }
 
    public void searchofthreads(String urlx) throws Exception
    {

        while(pagesvisited_pagecount.countervalue() < 20)
        {
            String currentUrl;
            SpiderLeg leg = new SpiderLeg();
            if(this.pagesToVisit.isEmpty())
            {
                 currentUrl = urlx;
                if( this.pagesvisited_pagecount.getvisited().contains(currentUrl))
                 break;
                 int x=  this.pagesvisited_pagecount.getvisited().size();
                  this.pagesvisited_pagecount.getvisited().add(urlx);
                     int y=  this.pagesvisited_pagecount.getvisited().size();
                     if(y>x){
                  
                  pagesvisited_pagecount.increment();
                  savelistvisited( this.pagesvisited_pagecount.getvisited());
                     }
            }
            else
            {
                currentUrl = this.nextUrl();
            }
            URL curl=new URL(currentUrl);
            
            if(checkRobots(curl))
            { leg.crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in
                                   // SpiderLeg
           
            this.pagesToVisit.addAll(leg.getLinks());
               savelisttovisit( pagesToVisit);
            }
          
        }
        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesvisited_pagecount.getvisited().size()));
    }
  
    
    public static void savelistvisited(Set<String> list)throws Exception {
      BufferedWriter writer =  new BufferedWriter(new FileWriter("visited.txt"));
      for (String s:list) {
         writer.write(s);
         writer.newLine();
      }
      
      writer.close();
   }

     public static void savelisttovisit(List<String> list)throws Exception {
      BufferedWriter writer =  new BufferedWriter(new FileWriter("tovisit"+Thread.currentThread().getId()+".txt"));
      for (String s:list) {
         writer.write(s);
         writer.newLine();
      }
      
      writer.close();
   }
     	static boolean checkRobots(URL url) throws InterruptedException, MalformedURLException, IOException{
		//getting the hostname
		String link=url.toString();
		int slashes = link.indexOf("//") + 2;
                String root;
                  String extension;
		try{ root = "https://"+link.substring(slashes,link.indexOf('/', slashes));}
                catch(Exception x)
                {
                     root = "https://"+link.substring(slashes);
                }
              try{
                  extension=link.substring(link.indexOf('/', slashes));
              }
              catch(Exception x)
              {
                  extension="";
              }
		System.out.println("Host "+root);
		System.out.println("Link "+link);

		int delay = 0;
		boolean crawlDelay=false;

		URL robot;
		try{
			robot = new URL(root+"/robots.txt");
		} catch (MalformedURLException e){
			return false;
		}

		System.out.println("Robots File: "+robot.toString());
		System.out.println("-------------------------------------");

		//looking at the robots.txt
		try{
			BufferedReader robotstxt = new BufferedReader(new InputStreamReader(robot.openStream())); 

			String line="";
			while(null != (line = robotstxt.readLine())){  //what if it is moved?

				//System.out.println("ROBOTS: "+line);
                                if(line.startsWith("User-agent: *")){
                                    line = robotstxt.readLine();
                                    while(!line.startsWith("User-agent:")){
				if(line.startsWith("Disallow")){
					//System.out.println("ROBOTS: "+line);
					line = line.substring(10, line.length()).trim();
                                        if(line.startsWith("/*/"))
                                            line = line.substring(2, line.length()).trim();
					//System.out.println("---- "+line);
                                    String pattern = "(.*)"+line;
                  //          extension.replaceAll("/","//");
      // Create a Pattern object
                                      Pattern r = Pattern.compile(pattern);
                                            Matcher m = r.matcher(extension);
					if( m.find()){
						System.out.println("ROBOTS: "+line);
						System.out.println("*STATUS: CANNOT CRAWL!");
						return false;
					}
				}
                                                   if(null != (line = robotstxt.readLine()))
                                                       break;
                                    }
                                    }
				if(line.startsWith("Crawl-delay")){
					System.out.println("ROBOTS: "+line);
					crawlDelay=true;
					delay = Integer.parseInt(line.substring(13, line.length()).trim());
					System.out.println("*STATUS: CRAWL DELAY FOUND: "+delay +" SEC");
				}
			}
			robotstxt.close();
		} catch (IOException e) { // no robots.txt
			System.out.println("*STATUS: NO ROBOTS. SAFE TO CRAWL");
			return true;
		}

		if(crawlDelay==true){
			System.out.println("*STATUS: DEALYING CRAWL BY: " + delay +" SEC");
			Thread.sleep(delay * 1000);
		}
		else{
			System.out.println("*STATUS: DEALYING CRAWL BY: 5 SEC");
			Thread.sleep(5 * 1000);
		}

		System.out.println("*STATUS: SAFE TO CRAWL");
		return true;

	}

}

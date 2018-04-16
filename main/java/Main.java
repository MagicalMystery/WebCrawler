import com.opencsv.CSVWriter;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.FileWriter;
import java.util.HashMap;

/**
 * Created by chimera on 9/16/17.
 */
public class Main {
     static HashMap<Integer,Integer> status_code =  new HashMap<Integer,Integer>();
     static HashMap<String, Integer> file_size =  new HashMap<String, Integer>();
     static HashMap<String,Integer> content_type = new HashMap<String, Integer>();
     static int count_ok = 0;
     static int countn_ok =0;

    public static void main(String[] args) throws Exception {
        int numberOfCrawlers = 7;
        CrawlConfig config = new CrawlConfig();
        config.setUserAgentString("usc.edu");
        config.setCrawlStorageFolder("data/crawl");
        config.setMaxDepthOfCrawling(16);
        config.setMaxPagesToFetch(20000);
//        config.setMaxPagesToFetch(200);

        config.setPolitenessDelay(500);


/* Instantiate the controller for this crawl.*/
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        robotstxtConfig.setIgnoreUADiscrimination(true);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        CSVWriter csvWriter = null;

        csvWriter = new CSVWriter(new FileWriter("fetch_usatoday.csv"));
        String[] row = new String[]{"URL","Status-Code"};
        csvWriter.writeNext(row);
        csvWriter.close();

        csvWriter = new CSVWriter(new FileWriter("visit_usatoday.csv"));
        String[] row1 = new String[]{"URL","Size","No. of Out-Going Links", "Content Type"};
        csvWriter.writeNext(row1);
        csvWriter.close();

        csvWriter = new CSVWriter(new FileWriter("urls_usatoday.csv"));
        String[] row2 = new String[]{"URL","Website-Resite"};
        csvWriter.writeNext(row2);
        csvWriter.close();


        file_size.put("lessThanOneKb",0);
        file_size.put("lessThanTenKb",0);
        file_size.put("lessThan100Kb",0);
        file_size.put("lessThanOneMb",0);
        file_size.put("moreThanOneMb",0);


        controller.addSeed("https://www.usatoday.com/");

                controller.start(mySpider.class,5);
//        System.out.println(Arrays.asList(hm));
        for (Integer statuscode: status_code.keySet()){

            String key =statuscode.toString();
            String value = status_code.get(statuscode).toString();
            System.out.println(key + " " + value);


        }

        for (String less : file_size.keySet()){

            String key =less.toString();
            String value = file_size.get(less).toString();
            System.out.println(key + " " + value);


        }

        for (String type : content_type.keySet()){

            try {

                String key = type.toString();
                String value = content_type.get(type).toString();
                System.out.println(key + " " + value);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }


        }
        System.out.println("OKCount"+count_ok);
        System.out.println("NotOKCount"+ countn_ok);



    }
}

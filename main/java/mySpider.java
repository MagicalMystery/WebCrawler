import com.opencsv.CSVWriter;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by chimera on 9/17/17.
 */
public class mySpider  extends WebCrawler {

    private final static Pattern FILTERS =
            Pattern.compile(".*?((\\.(html|doc|gif|jpg|png|jpeg|bmp))|[^\\.\\/]+\\/?)$");

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.cnn.com/". In this case, we didn't need the
     * referring Page parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {


        boolean allowedType= referringPage.getContentType().matches(".*?\\b(html|doc|gif|jpg|png|jpeg|bmp)\\b.*");


        String href = url.getURL().toLowerCase();

        String type;

        Boolean inWebsite = href.matches("^[^\\:\\/]*:\\/*[\\w\\.\\-]*?usatoday\\.com.*");
        if (inWebsite == true) {
             type = "OK";
             Main.count_ok++;
        }else{
             type = "N_OK";
             Main.countn_ok++;
        }

        CSVWriter csvWriter = null;
        try {
            csvWriter = new CSVWriter(new FileWriter("urls_usatoday.csv",true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] row2 = new String[]{String.valueOf(url), String.valueOf(type)};
        csvWriter.writeNext(row2);
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allowedType
                && href.matches("^[^\\:\\/]*:\\/*[\\w\\.\\-]*?usatoday\\.com.*");
    }



    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        String contentType_url = page.getContentType();
        int content = page.getContentData().length;

//        System.err.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            CSVWriter csvWriter = null;
            try {
                csvWriter = new CSVWriter(new FileWriter("visit_usatoday.csv",true));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] row1 = new String[]{String.valueOf(url), String.valueOf(content),String.valueOf(links.size()),String.valueOf(contentType_url)};
            csvWriter.writeNext(row1);
            try {
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        System.out.println(contentType_url);
        if(Main.content_type.containsKey(contentType_url)){
            Main.content_type.put(contentType_url,Main.content_type.get(contentType_url)+1);


        }
        else {
            Main.content_type.put(contentType_url,1);
        }



        if(content < 1024)
            Main.file_size.put("lessThanOneKb",Main.file_size.get("lessThanOneKb")+1);
        else if(content >1024 && content < 10240)
            Main.file_size.put("lessThanTenKb",Main.file_size.get("lessThanTenKb")+1);
        else if(content > 10240 &&  content <102400)
            Main.file_size.put("lessThan100Kb",Main.file_size.get("lessThan100Kb")+1);
        else if(content> 102400 && content < 1048576)
            Main.file_size.put("lessThanOneMb",Main.file_size.get("lessThanOneMb")+1);
        else if(content>= 1048576)
            Main.file_size.put("moreThanOneMb",Main.file_size.get("moreThanOneMb")+1);


    }

    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
//        System.out.printf(statusCode+" "+webUrl);

        CSVWriter csvWriter = null;
        try {
            csvWriter = new CSVWriter(new FileWriter("fetch_usatoday.csv",true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] row = new String[]{String.valueOf(webUrl), String.valueOf(statusCode)};
        csvWriter.writeNext(row);
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Main.status_code.containsKey(statusCode)){
            Main.status_code.put(statusCode,Main.status_code.get(statusCode)+1);


        }
        else {
                Main.status_code.put(statusCode,1);
            }





    }
}

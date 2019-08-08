package business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import entity.Product;
import entity.Results;
import entity.Total;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import utils.VatCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class WebScraper {
    public static void main(String[] args) {

        String baseUrl = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html" ;
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        // this example doesn't need javascript and disabling Javascript makes the page load faster in general
        client.getOptions().setJavaScriptEnabled(false);
        Pattern pat = Pattern.compile("\\£(-?\\d+.\\d+)?.*");
        VatCalculator vatCalc = new VatCalculator();
        double sum = 0.0;
        double price = 0.0;

        try {

            HtmlPage page = client.getPage(baseUrl);
            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//li[@class='gridItem']/div") ;
            List<Product> productList = new ArrayList();

            if(items.isEmpty()){
                System.out.println("No items found !");
            }else {
                System.out.println("Items found !");
                for (int i = 0; i <items.size()-3; i++) {

                        String productUrl = ((HtmlAnchor)items.get(i).getFirstByXPath("//li[" + (i+1) + "]//div[not(@valign='top')][@class='productNameAndPromotions']//h3/a")).getHrefAttribute().replace("../","");
                        // access product description link
                        HtmlPage  productPage = client.getPage( "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/" + productUrl);

                        HtmlElement htmlProductDetail = productPage.getDocumentElement();
                        HtmlElement htmlProductTitle =  htmlProductDetail.getFirstByXPath("//div[@class='productTitleDescriptionContainer']");
                        // get the nutrition table
                        HtmlTable  htmlNutritionTable = htmlProductDetail.getFirstByXPath("//div//div//table");
                        // get price per unit
                        HtmlElement htmlPrice =  htmlProductDetail.getFirstByXPath("//div[@class='pricing']//p[@class='pricePerUnit']");
                        HtmlElement htmlDescription =  htmlProductDetail.getFirstByXPath("//div[@class='productText']");

                        Product productItem = new Product();

                        productItem.setTitle(htmlProductTitle.asText());

                        if (htmlNutritionTable != null) {

                            productItem.setKcal_per_100g(new Integer(htmlNutritionTable.getCellAt(2,1).asText().replace("kcal","")));
                        }

                        // Extract only the numerical values from the price informations
                        Matcher mat = pat.matcher(htmlPrice.asText());
                        while( mat.find()) {
                            price = new Double(mat.group(1));
                            productItem.setUnit_price(price);
                            sum += price;
                        }

                        if(htmlDescription != null) {
                            productItem.setDescription(htmlDescription.asText());
                        }

                        productList.add(productItem);

                    }

                    ObjectMapper mapper = new ObjectMapper();
                    Total totalCost = new Total(sum, vatCalc.getVatAmountFromGross(sum));
                    Results results = new Results(productList, totalCost);
                    String jsonString = mapper.writeValueAsString(results) ;
                    System.out.println(jsonString);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

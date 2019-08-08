import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;


public class WebScraperTests {
    private WebClient webClient;
    private String baseUrl;

    @Before
    public void init() throws Exception {
        webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        baseUrl = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
    }

    @After
    public void close() throws Exception {
        webClient.close();
    }

    @Test
    public void checkIfSelectedSectionIsOk()
            throws Exception {

        HtmlPage page = webClient.getPage(baseUrl);
        Assert.assertEquals(
                "Berries, cherries & currants | Sainsbury's",
                page.getTitleText());
    }

    @Test
    public void checkProductsExists() throws Exception {

        HtmlPage page = webClient.getPage(baseUrl);
        //get list of all products
        final List<HtmlElement>  items = (List<HtmlElement>) page.getByXPath("//li[@class='gridItem']/div");
        //check if the list of products is not null
        Assert.assertNotNull(items);
    }

    @Test
    public void checkLinkToDescriptionExists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl);
        Assert.assertEquals(page.getWebResponse().getStatusCode(), 200);
        final List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[not(@valign='top')][@class='productNameAndPromotions']//h3/a") ;
        for (HtmlElement  item:items) {
            Assert.assertFalse(item.asText().isEmpty());
        }
    }
}

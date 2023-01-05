package stepdefinitions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import pageobjects.AddCustomerPage;
import pageobjects.LoginPage;
import pageobjects.SearchCustomerPage;

public class Steps extends BaseClass{

	@Before
	public void setup() throws IOException
	{
		
	logger=Logger.getLogger("Cucumber BDD Project");
	PropertyConfigurator.configure("log4j.properties");	
	
	//Reading CONFIG properties file
	configProp = new Properties();
	FileInputStream configPropfile = new FileInputStream("config.properties");
	configProp.load(configPropfile);
	
	String br = configProp.getProperty("browser");
	
	if(br.equals("chrome"))
	{
	//OPEN Chrome
	System.setProperty("webdriver.chrome.driver",configProp.getProperty("chromepath"));
	driver=new ChromeDriver();
	}//OPEN FireFox
	else if (br.equals("firefox")) {
		System.setProperty("webdriver.gecko.driver",configProp.getProperty("firefoxpath"));
		driver = new FirefoxDriver();
	}//OPEN Internet Explorer
	else if (br.equals("ie")) {
		System.setProperty("webdriver.ie.driver",configProp.getProperty("iepath"));
		driver = new InternetExplorerDriver();
	}

	//Print LOG Files	
	logger.info("******Launching Browser******");
	}
	
	@Given("User Launch Chrome browser")
	public void user_Launch_Chrome_browser() {
		
		
		lp = new LoginPage(driver);  
	}

	@When("User opens URL {string}")
	public void user_opens_URL(String url) {
		logger.info("******Opening URL******");
		driver.get(url);
		driver.manage().window().maximize();
	}
//********************* LOGIN STEP DEFINITIONS ************************
	@When("User enters Email as {string} and Password as {string}")
	public void user_enters_Email_as_and_Password_as(String email, String password) {
		logger.info("******Providing Login Details******");
		lp.setUserName(email);
		lp.setPassword(password);
	}

	@When("Click on Login")
	public void click_on_Login() {
		logger.info("******Started Login******");
		lp.clickLogin();
	}

	@Then("Page Title should be {string}")
	public void page_Title_should_be(String title) {
		if (driver.getPageSource().contains("Login was unsuccessful.")) {
			driver.close();
			Assert.assertTrue(false);
			logger.info("******Login FAILED******");
		} else {
			Assert.assertEquals(title,driver.getTitle());
			logger.info("******Login PASSED******");
		}	
	}

	@When("User click on Log out link")
	public void user_click_on_Log_out_link() throws InterruptedException {
		lp.clickLogout();
		logger.info("******Logout from Application*****");
		Thread.sleep(3000);
	}

	@Then("close browser")
	public void close_browser() {
		logger.info("******Closing Browser******");
		driver.quit();
	}
	
//***************ADD NEW CUSTOMER STEP DEFINITIONS ****************************
@Then("User can view Dashboad")
public void user_can_view_Dashboad() {
	addcust =new AddCustomerPage(driver);
	
	Assert.assertEquals("Dashboard / nopCommerce administration", addcust.getPageTitle());
}

@When("User click on customers Menu")
public void user_click_on_customers_Menu() throws InterruptedException {
    addcust.clickOnCustomersMenu();
    Thread.sleep(3000);
}

@When("click on customers Menu Item")
public void click_on_customers_Menu_Item() {
	
    addcust.clickOnCustomersMenuItem();
}

@When("click on Add new button")
public void click_on_Add_new_button() throws InterruptedException {
    addcust.clickOnAddnew();
    Thread.sleep(2000);
}

@Then("User can view Add new customer page")
public void user_can_view_Add_new_customer_page() {
   Assert.assertEquals("Add a new customer / nopCommerce administration", addcust.getPageTitle());
}

@When("User enter customer info")
public void user_enter_customer_info() throws InterruptedException {
	
	logger.info("******Adding New Customer******");
	String email=randomstring()+"@gmail.com";
	addcust.setEmail(email);
	addcust.setPassword("test123");
	addcust.setCustomerRoles("Guest");
	Thread.sleep(3000);

	addcust.setManagerOfVendor("Vendor 2");
	addcust.setGender("Male");
	addcust.setFirstName("Amol");
	addcust.setLastName("Lanke");
	addcust.setDob("7/05/1985"); // Format: D/MM/YYY
	addcust.setCompanyName("Infosys");
	addcust.setAdminContent("Demo Cucumber BDD.........");
}

@When("click on Save button")
public void click_on_Save_button() throws InterruptedException {
    addcust.clickOnSave();
    Thread.sleep(3000);
    logger.info("******Saving the New Customer Data******");
}

@Then("User can view confirmation message {string}")
public void user_can_view_confirmation_message(String message) {
  Assert.assertTrue(driver.findElement(By.tagName("body")).getText()
		  .contains(message));
}

//****************** SEARCH CUSTOMER By EMAIL ID **********************
@When("Enter customer EMail")
public void enter_customer_EMail() {
	logger.info("******Search Customer By EMAIL******");
	srchcust =new SearchCustomerPage(driver);
	srchcust.setEmail("james_pan@nopCommerce.com");
}

@When("Click on search button")
public void click_on_search_button() throws InterruptedException {
	srchcust.clickSearch();
	Thread.sleep(3000);
}

@Then("User should found Email in the Search table")
public void user_should_found_Email_in_the_Search_table() {
	boolean status = srchcust.searchCustomerByEmail("james_pan@nopCommerce.com");
	Assert.assertEquals(true, status);
}

//*******************SEARCH CUSTOMER By FIRST & LAST NAME **********************
	@When("Enter customer FirstName")
	public void enter_customer_FirstName() {
		logger.info("******Search Customer By FIRST & LAST NAME******");
		srchcust =new SearchCustomerPage(driver);
		srchcust.setFirstName("Victoria");
	}

	@When("Enter customer LastName")
	public void enter_customer_LastName() {
		srchcust.setLastName("Terces");
	}

	@Then("User should found Name in the Search table")
	public void user_should_found_Name_in_the_Search_table() {
		boolean status=srchcust.searchCustomerByName("Victoria Terces");
		Assert.assertEquals(true, status);
	}

}

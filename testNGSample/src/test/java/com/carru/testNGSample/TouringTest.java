package com.carru.testNGSample;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TouringTest {
	public WebDriver driver;

	// Get label of the back button
	protected String getBackBtnLabel() {
		try {
			return driver.findElement(By.xpath("//div[@class='item active']/a[@class='goBack']")).getText();
		} catch(NoSuchElementException e) {
			// Current page doesn't have a back button
			return "";
		}
	}
	
	@AfterMethod
	public void waitAnimations() {
		// Wait for animations to finish
		(new WebDriverWait(driver, 4)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='item active']")));
	}

	@Parameters({ "age" })
	@Test(description = "Step 1: Age")
	public void step1Age(String age) {
		// Check label
		String expectedBackLabel = "";
		Assert.assertEquals(getBackBtnLabel(), expectedBackLabel);
		
		// Enter data
		driver.findElement(By.name("age")).sendKeys(age);
		
		// Click next
		driver.findElement(By.xpath("//div[@class='itemContent driver']/input[@class='next']")).click();
	}

	@Parameters({ "bonus", "age" })
	@Test(dependsOnMethods = { "step1Age" }, alwaysRun = true, description = "Step 2: Bonus")
	public void step2Bonus(String bonus, String age) {
		// Check label
		String expectedBackLabel = "Le conducteur a " + age + " ans";
		Assert.assertEquals(getBackBtnLabel(), expectedBackLabel);
		
		// Enter data
		driver.findElement(By.xpath("//button[@value='" + bonus + "']")).click();
	}

	@Parameters({ "postalCode", "bonus" })
	@Test(dependsOnMethods = { "step2Bonus" }, alwaysRun = true, description = "Step 3: Portal code")
	public void step3PostalCode(String postalCode, String bonus) {
		// Check label
		String expectedBackLabel = "Le bonus-malus est de " + bonus;
		Assert.assertEquals(getBackBtnLabel(), expectedBackLabel);
		
		// Enter data
		driver.findElement(By.name("postalCode")).sendKeys(postalCode);
		
		// Click next
		driver.findElement(By.xpath("//div[@class='itemContent zipCode']/input[@class='next']")).click();
	}

	@Parameters({ "carManufacturer", "postalCode" })
	@Test(dependsOnMethods = { "step3PostalCode" }, alwaysRun = true, description = "Step 4: Car manufacturers")
	public void step4Car(String carManufacturer, String postalCode) {
		// Check label
		String expectedBackLabel = "Le code postal est " + postalCode;
		Assert.assertEquals(getBackBtnLabel(), expectedBackLabel);
		
		// Click all brands
		driver.findElement(By.xpath("//a[@class='allbrands']")).click();		
		
		// Click brand
		driver.findElement(By.xpath("//button[@class='TACK']")).click();		
	}

	@Parameters({ "value", "carManufacturer" })
	@Test(dependsOnMethods = { "step4Car" }, alwaysRun = true, description = "Step 5: Value")
	public void step5Value(String value, String carManufacturer) {
		// Check label
		String expectedBackLabel = "Le véhicule est une " + carManufacturer;
		Assert.assertEquals(getBackBtnLabel(), expectedBackLabel);

		// Enter data
		driver.findElement(By.name("priceVat")).sendKeys(value);
		
		// Click next
		driver.findElement(By.xpath("//div[@class='itemContent VAT']/input[@class='next']")).click();
	}

	@Parameters({ "profile", "value" })
	@Test(dependsOnMethods = { "step5Value" }, alwaysRun = true, description = "Step 6: Profile")
	public void step6Profile(String profile, String value) {
		// Check label
		String expectedBackLabel = "Le véhicule à une valeur catalogue de " + value + " €";
		Assert.assertEquals(getBackBtnLabel(), expectedBackLabel);
		
		// Enter data
		driver.findElement(By.xpath("//input[@name='" + profile + "']")).click();
	}

	@Parameters({ "email", "profile" })
	@Test(dependsOnMethods = { "step6Profile" }, alwaysRun = true, description = "Step 7: Email")
	public void step7Email(String email, String profile) {
		// Get label of profile
		String profileText = driver.findElement(By.xpath("//div[@class='itemContent KM']/div[@class='btnBlock']/input[@name='" + profile + "']")).getAttribute("value");
		
		// Check label
		String expectedBackLabel = "Le conducteur roule " + profileText;
		Assert.assertEquals(getBackBtnLabel(), expectedBackLabel);
		
		// Enter data
		driver.findElement(By.name("driver_email")).sendKeys(email);
		
		// Click next
		driver.findElement(By.xpath("//input[@class='btn next get-tarif']")).click();
	}

	@Parameters({ "priceRC", "priceRC_PO", "priceRC_FO" })
	@Test(dependsOnMethods = { "step7Email" }, alwaysRun = true, description = "Step 8: Results")
	public void step8Prices(String expectedPriceRC, String expectedPriceRC_PO, String expectedPriceRC_FO) {
		// Check prices
		String priceRC = driver.findElement(By.xpath("//div[@id='formuleRC']/h4/span[@class='price']")).getText();
		String priceRC_PO = driver.findElement(By.xpath("//div[@id='formuleRC_PO']/h4/span[@class='price']")).getText();
		String priceRC_FO = driver.findElement(By.xpath("//div[@id='formuleRC_FO']/h4/span[@class='price']")).getText();
		Assert.assertEquals(priceRC, expectedPriceRC);
		Assert.assertEquals(priceRC_PO, expectedPriceRC_PO);
		Assert.assertEquals(priceRC_FO, expectedPriceRC_FO);
	}

	@Parameters({ "url" })
	@BeforeSuite(alwaysRun = true)
	public void setupBeforeSuite(String url) {
		driver = new FirefoxDriver();
		driver.get(url);
	}

	@AfterSuite(alwaysRun = true)
	public void setupAfterSuite() {
		driver.quit();
	}

}

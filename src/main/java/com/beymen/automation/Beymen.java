package com.beymen.automation;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;  
import org.openqa.selenium.support.ui.Select;


public class Beymen {
	
    static Logger logger = Logger.getLogger(Beymen.class.getName());

	
	 public static void main(String[] args) throws InterruptedException, IOException { 
		System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");  
		WebDriver driver = new ChromeDriver();
		
		String baseUrl = "https://www.beymen.com/";
		String keyword1 = "þort";
		String keyword2 = "gömlek";
		
		// open web site
		driver.get(baseUrl);
		driver.manage().window().maximize();
		
		//click cookie button
		driver.findElement(By.id("onetrust-accept-btn-handler")).click();  
		//select gender
		driver.findElement(By.id("genderWomanButton")).click();
		
		//search product
		WebElement searcbox=driver.findElement(By.xpath("/html/body/header/div/div/div[2]/div/div/div/input"));
		searcbox.sendKeys(keyword1);
		Thread.sleep(1000);
		searcbox.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), keyword2);
		searcbox.sendKeys(Keys.ENTER);
		
		//click product
		Thread.sleep(10000);
		List<WebElement> products = driver.findElements(By.className("m-productCard__photo"));
		products.get(1).findElement(By.tagName("a")).click();
		
		//select size
		Thread.sleep(10000);
		driver.findElements(By.className("m-variation__item")).get(0).click();
		//get product detail
		String productName=driver.findElement(By.className("o-productDetail__description")).getText();
		String productPrice=driver.findElement(By.id("priceNew")).getText();
		FileUtils.writeStringToFile(new File("data/data.txt"), productName + " = " + productPrice, Charset.forName("UTF-8"));

		//add basket
		driver.findElement(By.id("addBasket")).click();
		Thread.sleep(5000);
		// go to basket
		driver.findElement(By.xpath("/html/body/header/div/div/div[3]/div/a[3]")).click();

		Thread.sleep(10000);
		
		//price check
		String basketProductName=driver.findElement(By.className("m-basket__productInfoName")).getText();
		String basketProductPrice=driver.findElement(By.className("m-productPrice__salePrice")).getText();
		if(productPrice.equalsIgnoreCase(basketProductPrice)){
			logger.info("Ürün fiyatý eþit. Ürün : " + productName + " Fiyat : " + productPrice + " Sepet : " + basketProductPrice);
		}
		else{
			logger.error("Ürün fiyatý eþit deðil. Ürün : " + productName + " Fiyat : " + productPrice + " Sepet : " + basketProductPrice);
		}
		//change count
		Select drpCount = new Select(driver.findElement(By.id("quantitySelect0-key-0")));
		drpCount.selectByValue("2");

		driver.findElement(By.id("removeCartItemBtn0-key-0")).click();
		Thread.sleep(1000);

		WebElement emptyMessage=driver.findElement(By.className("m-empty__messageTitle"));
		if(emptyMessage.isDisplayed()) {
			logger.info("Sepet boþ!");
		}
		
		driver.quit();
	 }

}

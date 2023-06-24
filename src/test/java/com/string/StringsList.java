package com.string;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

public class StringsList extends BaseTest{
//	public static void main(String[] args) {
	private WebDriver driver=null;
	@Test(priority=1)
	@Story("story")
	@Description("description")
	@Severity(SeverityLevel.BLOCKER)
	public void test() throws InterruptedException {
//	Scanner sc = new Scanner(System.in);
		System.out.println("Enter the price of the books to display");
	//	int k = sc.nextInt();
		Thread.sleep(3000);
	this.driver=getWebDriver("chrome",WEB_DRIVER.LOGIN_DRIVER_TEST);
		Thread.sleep(3000);

		driver.get("http://demowebshop.tricentis.com/");

		/**
		 * @author bandi
		 * @see http://demowebshop.tricentis.com/ THis code is to check whether the
		 *      products are sorted in alphabetical order or not
		 */
		// books click
		driver.findElement(By.xpath("//a[contains(text(),'Books')]")).click();

		Select select = new Select(driver.findElement(By.xpath("//select[@id='products-orderby']")));
		select.selectByVisibleText("Name: A to Z");

		List<WebElement> bookName = driver.findElements(By.xpath("//h2[@class='product-title']"));
		List<String> beforeSorting = bookName.stream().map(i -> i.getText()).collect(Collectors.toList());
		List<String> afterSorting = bookName.stream().map(i -> i.getText()).sorted().collect(Collectors.toList());
		if (beforeSorting.equals(afterSorting)) {
			System.out.println("PRODUCTS ARE SORTED...");
		} else {
			System.out.println("PRODUCTS ARE NOT SORTED...");

		}

		List<WebElement> prices = driver.findElements(By.xpath("//span[@class='price actual-price']"));
		Map<String, Double> map = new HashMap<String, Double>();

		for (int i = 0; i < bookName.size(); i++) {
			String book = bookName.get(i).getText();
			double bookPrice = Double.parseDouble(prices.get(i).getText());
			map.put(book, bookPrice);
		}
		map.forEach((t, p) -> System.out.println(t + " " + p+"rs"));

		/**
		 * @Using the input value from the user to display the books of cost greater
		 *        than user requirement
		 */

		System.out.println("products greater than " + 10);
		map.entrySet().stream().filter(i -> i.getValue() > 10).forEach(System.out::println);
		driver.quit();
	}
}

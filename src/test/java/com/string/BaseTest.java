package com.string;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	private static final Logger logger = Logger.getLogger(BaseTest.class.getName());
	private static Map<WEB_DRIVER, WebDriver> webDriverPool = new Hashtable<WEB_DRIVER, WebDriver>();

	protected enum WEB_DRIVER {
		LOGIN_DRIVER_TEST
	}
	protected synchronized WebDriver getWebDriver(String browser, WEB_DRIVER webDriver) {
		logger.info("Starting of method getWebDriver");

		WebDriver driver = webDriverPool.get(webDriver);

		String osPath = System.getProperty("os.name");

		// Use existing driver
		if (driver != null) {
			logger.debug("Using existing web driver " + webDriver);
			return driver;

		}

		if (osPath.contains("Linux")) {

			if (browser.equalsIgnoreCase("Firefox")) {
				WebDriverManager.firefoxdriver().setup();
				FirefoxOptions options = new FirefoxOptions();
				options.setHeadless(true);
				options.addArguments("--no-sandbox");
				driver = new FirefoxDriver(options);
			} else if (browser.equalsIgnoreCase("Chrome")) {
				WebDriverManager.chromedriver().setup();

				ChromeOptions options = new ChromeOptions();

				options.addArguments("enable-automation");
				options.addArguments("--headless");
				options.addArguments("--no-sandbox");
				// options.addArguments("--disable-extensions");
				options.addArguments("--dns-prefetch-disable");
				options.addArguments("--disable-gpu");
				options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
				options.addArguments("--remote-allow-origins=*");
				options.setHeadless(true);
				options.addArguments("--headless"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
				options.addArguments("--window-size=1920,1080");
				options.setPageLoadStrategy(PageLoadStrategy.EAGER);// del
				options.addArguments("--disable-browser-side-navigation"); // del
				options.addArguments("--disable-dev-shm-usage"); // del
				options.addArguments("--disable-gpu");
				options.addArguments("--no-sandbox");

				//options.addArguments("load-extension=extension_2_3_164.crx");

				// options.setBinary("/opt/google/chrome/google-chrome");
				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("profile.default_content_settings.popups", 0);
				options.setExperimentalOption("prefs", prefs);

				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setBrowserName("CHROME");
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				capabilities.setCapability("applicationCacheEnabled", "true");

				driver = new ChromeDriver(options);

			}
		}

		else {

			if (browser.equalsIgnoreCase("Chrome")) {
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("enable-automation");
				// options.addArguments("--headless");
				// options.addArguments("--no-sandbox");
				// options.addArguments("--disable-extensions");
				options.addArguments("--dns-prefetch-disable");
				options.addArguments("--disable-gpu");
				options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
				//options.addExtensions(new File("extension_2_3_164.crx"));
				options.addArguments("allow-file-access-from-files");
				options.addArguments("use-fake-device-for-media-stream");
				options.addArguments("use-fake-ui-for-media-stream");
				options.addArguments("--remote-allow-origins=*");


				driver = new ChromeDriver(options);

			} else if (browser.equalsIgnoreCase("Firefox")) {
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();

			} else if (browser.equalsIgnoreCase("Chromium")) {
				WebDriverManager.chromiumdriver().setup();
				driver = new EdgeDriver();

			} else if (browser.equalsIgnoreCase("IEDriverServer")) {
				WebDriverManager.iedriver().setup();
				driver = new InternetExplorerDriver();

			}
		}

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
	//	driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		//driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));

		logger.info("***** Driver Successfully Created ****** " + driver.getTitle());

		logger.info("End of method getWebDriver");

		webDriverPool.put(webDriver, driver);
		return driver;
	}

}

package com.crossover.e2e;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;


public class GMailTest {
    private WebDriver driver;
    private Properties properties = new Properties();

    @BeforeMethod
	public void setUp() throws Exception {
        
        properties.load(new FileReader(new File("src/test/resources/test.properties")));
        //Dont Change below line. Set this value in test.properties file incase you need to change it..
        System.setProperty("webdriver.chrome.driver",properties.getProperty("webdriver.chrome.driver") );
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterMethod
	public void tearDown() throws Exception {
        driver.quit();
    }

    /*
     * Please focus on completing the task
     * 
     */
    @Test
    public void testSendEmail() throws Exception {
        driver.get("https://mail.google.com/");
       
        
        WebElement userElement = driver.findElement(By.id("identifierId"));
        userElement.sendKeys(properties.getProperty("username"));

        driver.findElement(By.id("identifierNext")).click();

        Thread.sleep(1000);

        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys(properties.getProperty("password"));
        driver.findElement(By.id("passwordNext")).click();

        Thread.sleep(1000);

        WebElement composeElement = driver.findElement(By.xpath("//*[@role='button' and (.)='Compose']"));
        composeElement.click();

        driver.findElement(By.name("to")).clear();
        driver.findElement(By.name("to")).sendKeys(String.format("%s@gmail.com", properties.getProperty("username")));
        
        // emailSubject and emailbody to be used in this unit test.
        String emailSubject = properties.getProperty("email.subject");
        String emailBody = properties.getProperty("email.body"); 
        driver.findElement(By.name("subjectbox")).clear();
        driver.findElement(By.name("subjectbox")).sendKeys(emailSubject);
        driver.findElement(By.xpath("(//*[@aria-label='Message Body'])[2]")).clear();
        driver.findElement(By.xpath("(//*[@aria-label='Message Body'])[2]")).sendKeys(emailBody);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@data-tooltip='More options']")).click();
        Actions act= new Actions(driver);
        WebElement labels=driver.findElement(By.xpath("//*[@class='J-Ph Gi J-N']"));
        act.moveToElement(labels).build().perform();
        WebElement social=driver.findElement(By.xpath("(//*[@class='J-LC-Jo J-J5-Ji'] )[1]"));
        act.moveToElement(social).build().perform();
        social.click();
        WebElement apply=driver.findElement(By.xpath("//*[text()='Apply']"));
        act.moveToElement(apply).build().perform();
        apply.click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@role='button' and text()='Send']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@aria-label='Social']")).click();
        Thread.sleep(1000);
        
        String mailFrom=driver.findElement(By.xpath("(//*[@class='zF' and @name='me'])[2]")).getText();
        Assert.assertEquals("me", mailFrom,"Mail not received");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//*[@title='Not starred'])[4]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//*[@class='zF' and @name='me'])[2]")).click();
        Thread.sleep(2000);
        String subject=driver.findElement(By.xpath("//*[@class='hP']")).getText();
        String body=driver.findElement(By.xpath("//*[@class='a3s aXjCH ']/div[1]")).getText();
        Assert.assertEquals(emailSubject.trim(), subject.trim(),"Subject Dont Match");
        Assert.assertEquals(emailBody.trim(), body.trim(),"Body Dont match");
        Thread.sleep(1000);
    }
}

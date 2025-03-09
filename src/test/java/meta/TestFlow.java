package meta;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Attachment;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import utils.Utility;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

public class TestFlow {
    protected static final String SITE = Utility.getServer().getPath();
    public static final String VALUES_SPLITTER = ";";

    private final String PATH_TO_LOGGER_CONFIG = System.getProperty("user.dir") + File.separator + "config.xml";
    private long beginTime;
    protected static Logger logger = LoggerFactory.getLogger("main_log");

    @BeforeSuite
    @Parameters({"critical"})
    public void startSuite() {
        RestAssured.baseURI = SITE;
        Assert.assertTrue(initLogger());
        logger.info("SUITE STARTED!");
        beginTime = System.currentTimeMillis();
    }

    @AfterSuite(alwaysRun = true)
    public void stopSuite(ITestContext testContext) {
         logger.info("SUITE FINISHED!");
    }

    private boolean initLogger() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(PATH_TO_LOGGER_CONFIG);

            return true;
        } catch (JoranException je) {
            System.err.println("Cant initialize logger! \n" + je.getMessage());
            je.printStackTrace();
            return false;
        }
    }

    @Attachment(value = "{0}", type = "text/plain")
    protected String saveTextLog (String message, String value){
        return message;
    }
}

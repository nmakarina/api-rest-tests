package meta;

import api.BaseApiTest;
import io.qameta.allure.Attachment;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class API_TestListener extends BaseApiTest implements ITestListener {

    @Attachment(value = "{0}", type = "text/plain")
    public String saveTextLog(String message, String value) {
        return message;
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        saveTextLog("test started", "Старт теста");
        System.out.println(iTestResult.getName() + " is starting");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        saveTextLog("test ended success", "Тест завершен успешно");
        System.out.println(iTestResult.getName() + " ended success");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        saveTextLog("test failure", "Ошибка");
        System.out.println(iTestResult.getName() + " is failure");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        saveTextLog("test skipped", "Тест пропущен");
        System.out.println(iTestResult.getName() + " was skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        saveTextLog("test failured but witin success percentage", "test failured but witin success percentage");
        System.out.println(iTestResult.getName() + " is failured but witin success percentage");
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        saveTextLog("test start", "Старт теста");
        System.out.println(iTestContext.getName() + " is starting");
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        saveTextLog("test finish", "Окончание теста");
        System.out.println(iTestContext.getName() + " is finished");
    }
}

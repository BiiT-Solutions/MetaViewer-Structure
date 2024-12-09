package com.biit.metaviewer;

import com.biit.metaviewer.logger.MetaViewerLogger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final String SCREENSHOT_TYPE = ".png";

    @Override
    public void onTestStart(ITestResult result) {
        MetaViewerLogger.info(this.getClass().getName(), "Test '" + result.getMethod().getMethodName() + "' from '" + result.getTestClass().getName() + "' has started.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        MetaViewerLogger.info(this.getClass().getName(), "Test '" + result.getMethod().getMethodName() + "' from '" + result.getTestClass().getName() + "' has finished.");
    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        MetaViewerLogger.info(this.getClass().getName(), "Starting tests from '" + context.getName() + "'.");
    }

    @Override
    public void onFinish(ITestContext context) {
        MetaViewerLogger.info(this.getClass().getName(), "Tests finished from '" + context.getName() + "'.");
    }
}

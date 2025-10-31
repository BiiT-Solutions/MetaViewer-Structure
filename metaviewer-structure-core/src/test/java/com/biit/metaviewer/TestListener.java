package com.biit.metaviewer;

/*-
 * #%L
 * MetaViewer Structure (Core)
 * %%
 * Copyright (C) 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.metaviewer.logger.MetaViewerLogger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

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

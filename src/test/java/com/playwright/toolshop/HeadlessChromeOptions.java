package com.playwright.toolshop;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

import static com.playwright.toolshop.testresources.Resources.BROWSER_LAUNCH_OPTION;
import static com.playwright.toolshop.testresources.Resources.HEADLESS;

public class HeadlessChromeOptions implements OptionsFactory {

    @Override
    public Options getOptions() {
        return new Options().
                setHeadless(HEADLESS).
                setLaunchOptions(new BrowserType.LaunchOptions()
                        .setArgs(BROWSER_LAUNCH_OPTION))
                        .setTestIdAttribute("data-test");
    }
}

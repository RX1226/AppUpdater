package com.github.rx1226.appupdater;

public interface Local {
    interface Language{
        String TW = "tw";
        String ZH = "zh";
        String EN = "en";
    }
    interface Version{
        String TW = "目前版本";
        String ZH = "当前版本";
        String EN = "Current Version";
    }
    interface ErrorMseeage{
        String NETWORK_NOT_AVAILABLE = "NETWORK_NOT_AVAILABLE";
        String CONNECT_ERROR = "CONNECT_ERROR";
        String APP_PAGE_ERROR = "APP_PAGE_ERROR";
        String PARSE_ERROR = "PARSE_ERROR";
        String UPDATE_VARIES_BY_DEVICE = "UPDATE_VARIES_BY_DEVICE";
    }
}

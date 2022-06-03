package com.purchase.service;

import java.util.Date;

public interface SettingService {
    Date getDeadline();
    Boolean setDeadline(String deadline);
    String getDeadlineString();
}

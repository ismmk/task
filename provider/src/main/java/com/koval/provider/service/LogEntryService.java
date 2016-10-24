package com.koval.provider.service;

import com.koval.provider.dao.LogEntryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Volodymyr Kovalenko
 */
@Service
public class LogEntryService {
    @Autowired
    private LogEntryDao logEntryDao;

    public Long countByTypeAndClient(String type, String client) {
        return logEntryDao.countByTypeAndClient(type, client);
    }
}

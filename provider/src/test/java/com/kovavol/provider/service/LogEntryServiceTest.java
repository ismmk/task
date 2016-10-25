package com.kovavol.provider.service;

import com.koval.provider.dao.LogEntryDao;
import com.koval.provider.service.LogEntryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Volodymyr Kovalenko
 */
@RunWith(MockitoJUnitRunner.class)
public class LogEntryServiceTest {
    @Mock
    private LogEntryDao logEntryDao;

    @InjectMocks
    private LogEntryService logEntryService = new LogEntryService();

    @Test
    public void testRepositoryArgumentsForCountByTypeAndClient_1(){
        logEntryService.countByTypeAndClient("type1", "client1");

        verify(logEntryDao).countByTypeAndClient(eq("type1"), eq("client1"));
    }

    @Test
    public void testCountByTypeAndClientResult_1() {
        when(logEntryDao.countByTypeAndClient(eq("type1"), eq("client1"))).thenReturn(2L);

        assertEquals(new Long(2), logEntryService.countByTypeAndClient("type1", "client1"));
    }
}


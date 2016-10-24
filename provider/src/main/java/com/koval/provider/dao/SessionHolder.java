package com.koval.provider.dao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Volodymyr Kovalenko
 */
@Component
public class SessionHolder {
    @Value("${cassandra.node}")
    private String cnode;
    @Value("${cassandra.port:9042}")
    private int cport;
    private Session session;
    private Cluster cluster;

    @PostConstruct
    private void connect() throws InterruptedException {
        while (true) {
            try {
                cluster = Cluster.builder().addContactPoint(cnode).withPort(cport).build();
                session = cluster.connect();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(5000);
            }
        }
    }
    public Session getSession(){
        return session;
    }
    @PreDestroy
    private void close() {
        cluster.close();
    }
}

package com.koval.provider.dao;

import com.datastax.driver.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * Created by Volodymyr Kovalenko
 */
@Repository
public class LogEntryDao {

    private static final String COUNT_BY_TYPE_AND_CLIENT = "select count(*) from access_log.entry where entry_type = :type and client_id=:client ";

    @Autowired
    private SessionHolder sessionHolder;

    public Long countByTypeAndClient(String type, String client)  {
        PreparedStatement st = sessionHolder.getSession().prepare(COUNT_BY_TYPE_AND_CLIENT);
        BoundStatement bound = st.bind()
                .setString("type", type)
                .setString("client", client);

        ResultSet rs = sessionHolder.getSession().execute(bound);

        return rs.iterator().next().getLong(0);
    }

}


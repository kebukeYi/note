package com.google.guice.model;

import com.google.guice.service.DatabaseTransactionLog;
import com.google.guice.service.TransactionLog;
import com.google.guice.service.lmpl.DatabaseTransactionLoglmpl;
import com.google.guice.service.lmpl.MySqlDatabaseTransactionLoglmpl;
import com.google.inject.AbstractModule;

/**
 * @Author : fang.com
 * @CreatTime : 2021-02-02 09:49
 * @Description :
 * @Version :  0.0.1
 */
public class BillingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TransactionLog.class).to(DatabaseTransactionLoglmpl.class);
        bind(DatabaseTransactionLog.class).to(MySqlDatabaseTransactionLoglmpl.class);
    }
}

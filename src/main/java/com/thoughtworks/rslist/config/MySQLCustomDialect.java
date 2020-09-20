package com.thoughtworks.rslist.config;

import org.hibernate.dialect.MySQL57Dialect;

public class MySQLCustomDialect extends MySQL57Dialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin";
    }
}

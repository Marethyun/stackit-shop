package org.stackit.shop.database;

import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface PackagesDAO {

    @SqlQuery()
    List<Package> getAll();
}

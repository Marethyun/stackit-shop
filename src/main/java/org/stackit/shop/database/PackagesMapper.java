package org.stackit.shop.database;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.stackit.shop.Package;
import org.stackit.shop.PackageUID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class PackagesMapper implements RowMapper<Package> {
    @Override
    public Package map(ResultSet rs, StatementContext ctx) throws PackageUID.MalformedUIDException, SQLException {

        Package pkg = new Package();

        pkg.setId(rs.getInt("id"));
        pkg.setUid(new PackageUID(rs.getString("uid")));
        pkg.setPlayerUUID(UUID.fromString(rs.getString("player_uuid")));

        String commands = rs.getString("commands");

        ArrayList<String> splitted = new ArrayList<>(Arrays.asList(commands.split(";")));

        pkg.setCommands(splitted);
        pkg.setSlotsNumber(rs.getInt("slotnumber"));
        pkg.setName(rs.getString("name"));

        long claimed_millis = rs.getLong("claimed_time");

        Date date;
        // If the row was null, the connector returns 0.
        if (claimed_millis == 0){
            date = null;
        } else {
            date = new Date(claimed_millis);
        }

        pkg.setClaimed(date);

        return pkg;
    }

    public Package mapEntity(Package p){
        return null;
    }
}

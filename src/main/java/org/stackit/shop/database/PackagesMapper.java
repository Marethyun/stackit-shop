package org.stackit.shop.database;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.stackit.shop.Package;
import org.stackit.shop.PackageUID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PackagesMapper implements RowMapper<Package> {
    @Override
    public Package map(ResultSet rs, StatementContext ctx) throws PackageUID.MalformedUIDException, SQLException {
        Package pkg = new Package();

        pkg.setId(rs.getInt("id"));
        pkg.setUid(rs.getString("uid"));
        pkg.setPlayer_uuid(rs.getString("player_uuid"));
        pkg.setCommands(rs.getString("commands"));
        pkg.setSlotsnumber(rs.getInt("slotsnumber"));
        pkg.setName(rs.getString("name"));
        pkg.setClaimed_time(rs.getLong("claimed_time"));

        return pkg;

    }

    public static Package mapEntity(Package pkg){

        pkg.setPackageUID(new PackageUID(pkg.getUid()));
        pkg.setPlayerUUID(UUID.fromString(pkg.getPlayer_uuid()));

        String commands = pkg.getCommands();

        ArrayList<String> splitted = new ArrayList<>(Arrays.asList(commands.split(";")));

        pkg.setCommandsList(splitted);

        long claimed_millis = pkg.getClaimed_time();

        Date date;
        // If the row was null, the connector returns 0.
        if (claimed_millis == 0){
            date = null;
        } else {
            date = new Date(claimed_millis);
        }

        pkg.setClaimedDate(date);

        return pkg;
    }

    public static List<Package> mapEntityList(List<Package> list){
        LinkedList<Package> packages = new LinkedList<>();

        list.forEach(p -> packages.add(mapEntity(p)));

        return packages;
    }
}

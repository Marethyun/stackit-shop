package org.stackit.shop.api;

import io.noctin.configuration.JsonConfiguration;
import io.noctin.events.Before;
import io.noctin.events.Listener;
import io.noctin.events.Proxy;
import io.noctin.events.Trigger;
import io.noctin.http.EndPoint;
import io.noctin.http.HttpGetEvent;
import io.noctin.http.HttpPutEvent;
import io.noctin.network.http.server.HTTPStatus;
import io.noctin.network.http.server.renderer.JsonHeaders;
import io.noctin.network.http.server.renderer.RestEngine;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.stackit.StackItLogger;
import org.stackit.api.AuthProxy;
import org.stackit.api.ContentType;
import org.stackit.api.JsonRequest;
import org.stackit.shop.Package;
import org.stackit.shop.StackItShop;
import org.stackit.shop.StackItShopContainer;
import org.stackit.shop.database.PackagesMapper;
import org.stackit.shop.database.PackagesQueries;
import org.stackit.shop.database.Tables;

import java.util.LinkedList;
import java.util.List;

public class PackagesListener extends StackItShopContainer implements Listener {

    private StackItLogger logger = pluginInstance.logger();
    private Jdbi jdbi = pluginInstance.getJdbi();

    public PackagesListener(StackItShop pluginInstance) {
        super(pluginInstance);
    }

    @Trigger
    @EndPoint("/shop/packages") @Proxy(AuthProxy.class) @Before(ContentType.class)
    public void get(HttpGetEvent e){

        JsonConfiguration configuration = new JsonConfiguration();
        JsonHeaders headers = new JsonHeaders();

        try (Handle handle = jdbi.open()) {
            List<Package> unFilledPackages = handle.createQuery(PackagesQueries.GET_ALL.getQuery()).map(new PackagesMapper()).list();

            List<Package> packages = PackagesMapper.mapEntityList(unFilledPackages);

            LinkedList<JsonConfiguration> pkgs = new LinkedList<>();

            for (Package p : packages) {
                JsonConfiguration c = new JsonConfiguration();

                c.set("uid", p.getUid());
                c.set("player_uuid", p.getPlayer_uuid());
                c.set("commands", p.getCommandsList());
                c.set("slotnumber", p.getSlotsnumber());
                c.set("name", p.getName());

                boolean claimed = p.isClaimed();

                c.set("claimed", claimed);

                if (claimed){
                    c.set("claimed_time", p.getClaimed_time());
                }

                pkgs.add(c);
            }

            configuration.set("count", pkgs.size());
            configuration.set("packages", pkgs);
        } catch (Exception ex){
            headers.status(HTTPStatus.INTERNAL_SERVER_ERROR);
            headers.message(ex.getMessage());

            ex.printStackTrace();
        }

        e.render(new RestEngine(configuration, headers).render());
    }

    @Trigger
    @EndPoint("/shop/packages") @Proxy({AuthProxy.class, JsonRequest.class}) @Before(ContentType.class)
    public void put(HttpPutEvent e){

    }
}

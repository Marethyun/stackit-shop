package org.stackit.shop;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import io.noctin.configuration.YamlConfiguration;
import io.noctin.events.EntityHandler;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jdbi.v3.core.ConnectionException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.stackit.*;
import org.stackit.shop.api.PackagesListener;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

public class StackItShop extends StackItBundle {

    public static final String PREFIX = ChatColor.AQUA + "[StackIt-Shop] ";
    public static final String ENABLING_MESSAGE = "Successfully enabled StackIt-Shop bundle !";

    public static final String CONFIGURATION_FILENAME = "StackIt-Shop.yml";

    private Bundler bundler;
    private EntityHandler handler;
    private StackItLogger logger = new StackItLogger(getServer().getConsoleSender(), PREFIX);

    private YamlConfiguration configuration;

    private Jdbi jdbi;

    public StackItShop() {
        super("StackIt Shop");
    }

    @Override
    public void onLoad() {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();

        try {

            this.author = getDescription().getAuthors().get(0);
            this.description = getDescription().getDescription();
            this.version = getDescription().getVersion();

            this.bundler = StackIt.bundler();
            this.bundler.registerBundle(this);
            this.handler = bundler.getEventHandler(this);

            File config = this.resolveFile(CONFIGURATION_FILENAME);

            this.configuration = new YamlConfiguration(FileUtils.readFileToString(config, Charset.defaultCharset()));

            String username = configuration.getString(ConfigNodes.DATABASE_USERNAME.getNode());
            String password = configuration.getString(ConfigNodes.DATABASE_PASSWORD.getNode());

            dataSource.setServerName(configuration.getString(ConfigNodes.DATABASE_HOST.getNode()));
            dataSource.setPort(configuration.getInt(ConfigNodes.DATABASE_PORT.getNode()));
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setDatabaseName(configuration.getString(ConfigNodes.DATABASE_NAME.getNode()));

            this.jdbi = Jdbi.create(dataSource);

            this.jdbi.installPlugin(new SqlObjectPlugin());

            try (Handle handle = jdbi.open()){
                String s = handle.createQuery("SELECT 'Hello, World!'").mapTo(String.class).list().get(0);
                if (s != null){
                    logger.success(String.format("Bundle successfully connected to the database ! (retrieved '%s')", s));
                }
            }

            bundler.registerCommandOption(this, new PackagesCommand(this));

        } catch (Exception e) {
            logger.error("An exception occurred white loading the bundle..");

            if (e instanceof ConnectionException) {
                logger.error(String.format("Unable to connect to database with datasource %s", dataSource.getURL()));
            }

            e.printStackTrace();

            logger.warn("The plugin should stay enabled, but could not work properly");
        }
    }

    @Override
    public void onEnable() {
        this.handler.attach(new PackagesListener(this));
    }

    @Override
    public void onDisable() {

    }

    /**
     * Resolve a file: if it doesn't exists in datafolder, just create it from the template in path
     * Returns the file content anyway
     * @param fileName The file name
     */
    private File resolveFile(String fileName) {
        File file = new File(this.getDataFolder(), fileName);

        if (!file.exists()){
            try (InputStream stream = getClass().getResourceAsStream('/' + fileName)){
                String content = "";

                int i;

                while ((i = stream.read()) != -1){
                    content += (char) i;
                }

                FileUtils.writeStringToFile(file, content, Charset.defaultCharset());
            } catch (Exception e){
                e.printStackTrace();
                throw new StackItException(e);
            }
        }

        return file;
    }

    public Jdbi getJdbi() {
        return jdbi;
    }

    public StackItLogger logger() {
        return logger;
    }
}

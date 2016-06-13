package com.booking.replication.util;

import joptsimple.OptionSet;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bdevetak on 01/12/15.
 */
public class StartupParameters {

    private String  configPath;
    private String  schema;
    private String  applier;
    private String  binlogFileName;
    private Long    binlogPosition;
    private String  lastBinlogFileName;
    private Integer shard;
    private boolean deltaTables;
    private boolean initialSnapshot;
    private String  hbaseNamespace;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StartupParameters.class);

    public StartupParameters(OptionSet o) {

        // use delta tables
        deltaTables = o.has("delta");

        // initial snapshot mode
        initialSnapshot  = o.has("initial-snapshot");

        // schema
        if (o.hasArgument("schema")) {
            schema = o.valueOf("schema").toString();
            // shards can be specified in config file as ${schema_name}${shard_id}
            String maybeNumber = schema.replaceAll("[A-Za-z]", "");
            shard = StringUtils.isNotBlank(maybeNumber) ? Integer.parseInt(maybeNumber) : 0;
            schema = schema.replaceAll("[0-9]","");
        }
        else {
            schema = "test";
        }

        // shard_id can also be explicity passed as cmd argument - that will overide config file setting
        if (o.hasArgument("shard")) {
            shard = (Integer) o.valueOf("shard");
        }

        // config-path
        configPath = (String) o.valueOf("config-path");

        // applier, defaults to STDOUT
        applier = (String) o.valueOf("applier");

        // setup hbase namespace
        hbaseNamespace = (String) o.valueOf("hbase-namespace");

        // Start binlog filename
        binlogFileName = (String) o.valueOf("binlog-filename");

        // Start binlog position
        binlogPosition = (Long) o.valueOf("binlog-position");

        // Last binlog filename
        lastBinlogFileName = (String) o.valueOf("last-binlog-filename");

        System.out.println("----------------------------------------------");
        System.out.println("Parsed params:           ");
        System.out.println("\tconfig-path:           " + configPath);
        System.out.println("\tschema:                " + schema);
        System.out.println("\tapplier:               " + applier);
        System.out.println("\tbinlog-filename:       " + binlogFileName);
        System.out.println("\tposition:              " + binlogPosition);
        System.out.println("\tlast-binlog-filename:  " + lastBinlogFileName);
        System.out.println("\tinitial-snapshot:      " + initialSnapshot);
        System.out.println("\thbase-namespace:       " + hbaseNamespace);
        System.out.println("----------------------------------------------\n");

    }

    public String getConfigPath() {
        return configPath;
    }

    public String getSchema() {
        return schema;
    }

    public String getApplier() {
        return applier;
    }

    public String getBinlogFileName() {
        return binlogFileName;
    }

    public String getLastBinlogFileName() {
        return lastBinlogFileName;
    }

    public Long getBinlogPosition() {
        return binlogPosition;
    }

    public Integer getShard() {
        return shard;
    }

    public boolean isDeltaTables() {
        return deltaTables;
    }

    public boolean isInitialSnapshot() {
        return initialSnapshot;
    }

    public String getHbaseNamespace() {
        return hbaseNamespace;
    }
}

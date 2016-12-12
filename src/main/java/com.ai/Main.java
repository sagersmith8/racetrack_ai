package com.ai;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

public class Main {
    public static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String ... args) throws IOException {
        SpringApplication.run(Main.class, args);
        logger.debug("Hi there");
        logger.info("Info message");
        logger.warn("Warning message");
        logger.error("Error message");
    }

    /**
     * Takes the options for the program to run
     * @param args program arguments
     * @return returns an option set
     */
    public OptionSet getOptions(String ... args) {
        OptionParser parser = new OptionParser();
        parser.accepts("racetrack").withRequiredArg().ofType(String.class).defaultsTo("l_track");
        parser.accepts("model").withRequiredArg().ofType(String.class).defaultsTo("stop");
        parser.accepts("learner").withRequiredArg().ofType(String.class).defaultsTo("both");
        parser.accepts("result_loc").withRequiredArg().ofType(String.class).defaultsTo("/results");
        parser.accepts("sample_loc").withRequiredArg().ofType(String.class).defaultsTo("/sample_runs");
        parser.accepts("sample_run").withRequiredArg().ofType(Boolean.class).defaultsTo(Boolean.FALSE);
        return parser.parse(args);
    }
}

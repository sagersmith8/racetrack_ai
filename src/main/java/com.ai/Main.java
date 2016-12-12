package com.ai;

import com.ai.alg.RacetrackLearner;
import com.ai.alg.SARSA;
import com.ai.alg.ValueIteration;
import com.ai.sim.Collision;
import com.ai.sim.CollisionModel;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String ... args) throws Exception {
        SpringApplication.run(Main.class, args);
        OptionSet options = getOptions(args);
        run(options);
    }

    /**
     * Sets up logging options for sample run or test run
     *
     * @param options Options to check
     */
    private static void handleSampleRun(OptionSet options) {
        if (options.has("sample-run")) {
            logger.setLevel(Level.DEBUG);
            logger.debug("Beginning Sample Run...");
        } else {
            logger.setLevel(Level.WARN);
            logger.debug("Beginning Test Run...");
        }
    }

    /**
     * Creates policy tests for each of the given racetracks and collision models
     *
     * @param options to check for num tests of
     * @param racetracks racetracks to setup policy testers for
     * @param collisionModels collision model to test with
     * @return the list of needed policy testers
     */
    private static Map<Racetrack, Map<CollisionModel, PolicyTester>> getPolicyTesters(OptionSet options, List<Racetrack> racetracks, List<CollisionModel> collisionModels) {
        Map<Racetrack, Map<CollisionModel, PolicyTester>> policyTesters = new HashMap<>();

        for(Racetrack raceTrack : racetracks) {
            if (!policyTesters.containsKey(raceTrack)) {
                policyTesters.put(raceTrack, new HashMap<>());
            }

            for (CollisionModel collisionModel: collisionModels) {
                logger.debug("Adding policy tester for "+ raceTrack + " and "+ collisionModel);
                Map<CollisionModel, PolicyTester> policyTesterList = policyTesters.get(raceTrack);
                policyTesterList.put(collisionModel, new PolicyTester(raceTrack, collisionModel, (Integer) options.valueOf("num-tests")));
            }
        }

        return policyTesters;
    }

    /**
     * Gets the needed collision models from the options
     *
     * @param options options to check for a model in
     * @return the needed collision models
     */
    private static List<CollisionModel> getCollisionModels(OptionSet options) {
        List<CollisionModel> collisionModels = new ArrayList<>();
        if (options.hasArgument("model")) {
            switch (options.valueOf("model").toString()) {
                case "stop":
                    logger.debug("Setting stop collision model for policy testers...");
                    collisionModels.add(Collision.STOP);
                    break;
                case "restart":
                    logger.debug("Setting restart collision model for policy testers");
                    collisionModels.add(Collision.RESTART);
                default:
                    logger.error("Unrecognized value for model " + options.valueOf("model").toString() + "expected <restart> or <stop>");
                    logger.error("Throwing runtime exception...");
                    new RuntimeException("Unrecognized argument");
            }
        } else {
            collisionModels = Arrays.asList(Collision.STOP, Collision.RESTART);
        }

        return collisionModels;
    }

    /**
     * Gets the needed racetrack learners for a racetrack
     *
     * @param options options to check for learners in
     * @param racetracks racetracks to build learners for
     *
     * @return the needed racetrack learners for testing
     */
    private static Map<Racetrack, Map<CollisionModel, List<RacetrackLearner>>> getRaceTrackLearners(OptionSet options, List<Racetrack> racetracks, List<CollisionModel> collisonModels) {
        Map<Racetrack, Map<CollisionModel, List<RacetrackLearner>>> learners = new HashMap<>();
        if (options.hasArgument("learner")) {
            String learnerName = options.valueOf("learner").toString();
            switch (learnerName) {
                case "sarsa":
                    logger.debug("Adding Sarsa to tester...");
                    for (Racetrack racetrack : racetracks) {
                        Map<CollisionModel, List<RacetrackLearner>> collisionMap = new HashMap<>();
                        learners.put(racetrack, collisionMap);
                        for (CollisionModel collisionModel: collisonModels) {
                            collisionMap.put(collisionModel, Arrays.asList(new SARSA(racetrack, collisionModel)));
                        }
                    }
                    break;
                case "value-iteration":
                    logger.debug("Adding Value iteration to tester...");
                    for (Racetrack racetrack : racetracks) {
                        Map<CollisionModel, List<RacetrackLearner>> collisionMap = new HashMap<>();
                        learners.put(racetrack, collisionMap);
                        for (CollisionModel collisionModel: collisonModels) {
                            collisionMap.put(collisionModel, Arrays.asList(new ValueIteration(racetrack, collisionModel)));
                        }
                    }
                    break;
                default:
                    logger.error("Value not recognized: " + learnerName + ". Expected <sarsa> or <value-iteration>...");
                    logger.error("Throwing runtime exception...");
                    new RuntimeException("Learner name not recognized");
            }
        } else {
            for (Racetrack racetrack : racetracks) {
                Map<CollisionModel, List<RacetrackLearner>> collisionMap = new HashMap<>();
                learners.put(racetrack, collisionMap);
                for (CollisionModel collisionModel: collisonModels) {
                    collisionMap.put(collisionModel, Arrays.asList(new SARSA(racetrack, collisionModel), new ValueIteration(racetrack, collisionModel)));
                }
            }
        }

        return learners;
    }

    /**
     * Executes the tests determined by the options
     *
     * @param options the options that configure the tests
     * @throws IOException caused by racetrack file not existing
     */
    private static void run(OptionSet options) throws Exception {
        handleSampleRun(options);
        List<Racetrack> racetracks = getRaceTracks(options);
        List<CollisionModel> collisionModels = getCollisionModels(options);
        Map<Racetrack, Map<CollisionModel, List<RacetrackLearner>>> learners = getRaceTrackLearners(options, racetracks, collisionModels);
        Map<Racetrack, Map<CollisionModel, PolicyTester>> policyTesters = getPolicyTesters(options, racetracks, collisionModels);


        if (options.has("no-thread")) {
            nonThreadedRun(learners, policyTesters, (Integer) options.valueOf("max-iteration"));
        } else {
            multiThreadedRun(learners, policyTesters, (Integer) options.valueOf("max-iteration"));
        }
    }

    /**
     * Gets the racetracks specified by the options
     *
     * @param options the options to check for racetracks in
     *
     * @return the specified racetracks
     * @throws IOException if the racetracks cannot be found
     */
    private static List<Racetrack> getRaceTracks(OptionSet options) throws IOException {
        List<Racetrack> racetracks = new ArrayList<>();

        Map<String, Racetrack> racetrackRegistry = new HashMap<>();
        racetrackRegistry.put("l_track", Racetrack.fromFile("l_track.txt").withName("l_track"));
        racetrackRegistry.put("r_track", Racetrack.fromFile("r_track.txt").withName("r_track"));
        racetrackRegistry.put("o_track", Racetrack.fromFile("o_track.txt").withName("o_track"));
        racetrackRegistry.put("small_l_track", Racetrack.fromFile("small_l_track.txt").withName("small_l_track"));

        if (options.hasArgument("racetrack")) {
            if (options.valueOf("racetrack").toString().equals("all")) {
                logger.debug("Adding every racetrack in the registry...");
                racetracks.addAll(racetrackRegistry.values());
            } else {
                logger.debug("Adding the specified racetrack: " + racetrackRegistry.get(options.valueOf("racetrack").toString())+" ...");
                racetracks.add(racetrackRegistry.get(options.valueOf("racetrack").toString()));
            }
        } else {
            racetracks.add(racetrackRegistry.get("small_l_track"));
            logger.debug("Adding only the small_l_track...");
        }

        return racetracks;
    }

    /**
     * Runs a non threaded run
     *
     * @param learners to test
     * @param policyTesters to test against
     * @param maxIteration to blow up after
     */
    private static void nonThreadedRun(Map<Racetrack, Map<CollisionModel, List<RacetrackLearner>>> learners, Map<Racetrack, Map<CollisionModel, PolicyTester>> policyTesters, Integer maxIteration) {
        logger.debug("Starting a non threaded run...");
        Set<RacetrackLearner> activeLearners = new HashSet<>();
        for (Map.Entry<Racetrack, Map<CollisionModel, List<RacetrackLearner>>> raceTrackEntry : learners.entrySet()) {
            for (Map.Entry<CollisionModel, List<RacetrackLearner>> collisionEntry : raceTrackEntry.getValue().entrySet()) {
                activeLearners.addAll(collisionEntry.getValue());
            }
        }

        while (!activeLearners.isEmpty()) {
            for (Map.Entry<Racetrack, Map<CollisionModel, List<RacetrackLearner>>> raceTrackEntry : learners.entrySet()) {
                logger.debug(raceTrackEntry.getKey());
                for (Map.Entry<CollisionModel, List<RacetrackLearner>> collisionEntry : raceTrackEntry.getValue().entrySet()) {
                    logger.debug(collisionEntry.getKey());
                    for (RacetrackLearner learner : collisionEntry.getValue()) {
                        logger.debug(learner);
                        if (!activeLearners.contains(learner)) {
                            continue;
                        }
                        learner.next();
                        logger.debug("Finished next...");
                        Policy policy = learner.getPolicy();
                        PolicyTester policyTester = policyTesters.get(raceTrackEntry.getKey()).get(collisionEntry.getKey());
                        Result result = policyTester.testPolicy(policy);
                        logger.debug(
                                "Result: " + result.getMean() +
                                        " with confidence of: " + result.getConfidence() +
                                        " variance: " + result.getVariance() +
                                        " for Learner :" + learner +
                                        " on iteration: " + learner.getIterationCount() +
                                        " using the policy: " + policyTester.collisionModel()
                        );

                        if (learner.getIterationCount() >= maxIteration) {
                            logger.error("Max iteration count exceeded for: " + learner + "removing policy testers...");
                            activeLearners.remove(learner);
                        } else if (learner.finished()) {
                            logger.info(learner + " finished! Removing policy testers...");
                            activeLearners.remove(learner);
                        }
                    }
                }
            }
        }
    }

    private static List<Result> runLearner(RacetrackLearner learner, PolicyTester tester, Integer maxIterations) {
        List<Result> results = new ArrayList<>();
        logger.debug("Starting "+learner+ " "+tester+ "...");
        while (!learner.finished() && learner.getIterationCount() <= maxIterations) {
            logger.debug("Current iteration: "+learner.getIterationCount()+ "...");
            learner.next();
            Policy policy = learner.getPolicy();
            results.add(tester.testPolicy(policy));
        }
        logger.debug("Finished "+learner+ " "+tester+ "...");

        if (learner.finished()) {
            logger.debug(learner + " finished!");
        } else {
            logger.debug(learner + "did not terminate");
        }
        return results;
    }

    /**
     * Runs a multi-threaded run of the racetrack runner
     *
     * @param learners to test
     * @param policyTesters to test with
     * @param maxIteration finish if the max iteration count is run
     *
     * @throws Exception thrown by the thread pool
     */
    private static void multiThreadedRun(Map<Racetrack, Map<CollisionModel, List<RacetrackLearner>>> learners, Map<Racetrack, Map<CollisionModel, PolicyTester>> policyTesters, Integer maxIteration) throws Exception{
        ExecutorService executor = Executors.newWorkStealingPool();
        List<Callable<List<Result>>> callables = new ArrayList<>();
        for (Map.Entry<Racetrack, Map<CollisionModel, List<RacetrackLearner>>> entry : learners.entrySet()) {
            for (Map.Entry<CollisionModel, List<RacetrackLearner>> collisionEntry : entry.getValue().entrySet()) {
                for (RacetrackLearner learner : collisionEntry.getValue()) {
                    callables.add(() -> runLearner(learner, policyTesters.get(entry.getKey()).get(collisionEntry.getKey()), maxIteration));
                }
            }
        }

        logger.debug("About to create futures...");
        executor.invokeAll(callables).stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }).forEach(logger::debug);
    }

    /**
     * Takes the options for the program to run
     * @param args program arguments
     * @return returns an option set
     */
    private static OptionSet getOptions(String ... args) {
        OptionParser parser = new OptionParser();
        parser.accepts("racetrack").withOptionalArg().ofType(String.class);
        parser.accepts("model").withOptionalArg().ofType(String.class);
        parser.accepts("learner").withOptionalArg().ofType(String.class);
        parser.accepts("max-iteration").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.MAX_VALUE);
        parser.accepts("no-thread");
        parser.accepts("num-tests").withRequiredArg().ofType(Integer.class).defaultsTo(20);
        parser.accepts("result-loc").withRequiredArg().ofType(String.class).defaultsTo("/results");
        parser.accepts("sample-loc").withRequiredArg().ofType(String.class).defaultsTo("/sample_runs");
        parser.accepts("sample-run");
        return parser.parse(args);
    }
}

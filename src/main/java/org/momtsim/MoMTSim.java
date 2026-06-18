package org.momtsim;

import org.momtsim.base.Transaction;
import org.momtsim.parameters.Parameters;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Public facade for embedding MoMTSim in another Java application.
 */
public final class MoMTSim {
    private static final String DEFAULT_PROPERTIES = "MoMTSim.properties";
    private static final int DEFAULT_QUEUE_DEPTH = 200_000;

    private final Parameters parameters;
    private final int queueDepth;
    private final String workerName;

    private MoMTSim(Parameters parameters, int queueDepth, String workerName) {
        this.parameters = Objects.requireNonNull(parameters, "parameters");
        if (queueDepth < 1) {
            throw new IllegalArgumentException("queueDepth must be greater than zero");
        }
        this.queueDepth = queueDepth;
        this.workerName = Objects.requireNonNull(workerName, "workerName");
    }

    public static MoMTSim fromDefaultProperties() {
        return fromProperties(DEFAULT_PROPERTIES);
    }

    public static MoMTSim fromProperties(String propertiesFile) {
        return fromParameters(new Parameters(propertiesFile));
    }

    public static MoMTSim fromProperties(Path propertiesFile) {
        return fromProperties(propertiesFile.toString());
    }

    public static MoMTSim fromParameters(Parameters parameters) {
        return new MoMTSim(parameters, DEFAULT_QUEUE_DEPTH, IteratingMoMTSim.DEFAULT_WORKER_NAME);
    }

    public MoMTSim withQueueDepth(int queueDepth) {
        return new MoMTSim(parameters, queueDepth, workerName);
    }

    public MoMTSim withWorkerName(String workerName) {
        return new MoMTSim(parameters, queueDepth, workerName);
    }

    public IteratingMoMTSim iterator() {
        IteratingMoMTSim simulation = new IteratingMoMTSim(parameters, queueDepth, workerName);
        simulation.run();
        return simulation;
    }

    public Stream<Transaction> stream() {
        final IteratingMoMTSim simulation = iterator();
        Spliterator<Transaction> spliterator = Spliterators.spliteratorUnknownSize(
                simulation,
                Spliterator.ORDERED | Spliterator.NONNULL
        );
        return StreamSupport.stream(spliterator, false)
                .onClose(() -> abortQuietly(simulation));
    }

    public List<Transaction> runToList() {
        final List<Transaction> transactions = new ArrayList<>();
        forEachTransaction(transactions::add);
        return transactions;
    }

    public void forEachTransaction(Consumer<? super Transaction> action) {
        Objects.requireNonNull(action, "action");
        try (Stream<Transaction> transactions = stream()) {
            transactions.forEach(action);
        }
    }

    public Parameters getParameters() {
        return parameters;
    }

    private static void abortQuietly(IteratingMoMTSim simulation) {
        try {
            if (simulation.hasNext()) {
                simulation.abort();
            }
        } catch (IllegalStateException ignored) {
            // The worker may have completed between hasNext() and abort().
        }
    }
}

package org.momtsim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.momtsim.base.Transaction;
import org.momtsim.parameters.Parameters;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoMTSimTest {

    @Test
    void rejectsInvalidQueueDepth() {
        Parameters parameters = new Parameters("MoMTSim.properties");

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> MoMTSim.fromParameters(parameters).withQueueDepth(0)
        );
    }

    @Test
    void streamsTransactionsFromPropertiesFile() throws Exception {
        Path path = Paths.get(getClass().getResource("/MoMTSim.properties").toURI());

        List<Transaction> transactions;
        try (Stream<Transaction> stream = MoMTSim.fromProperties(path).stream()) {
            transactions = stream.limit(3).collect(Collectors.toList());
        }

        Assertions.assertFalse(transactions.isEmpty());
    }
}

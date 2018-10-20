package de.drippinger.fakegen;

import de.drippinger.fakegen.types.JavaBaseInterfaceTypes;
import de.drippinger.fakegen.types.JavaBaseTypes;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@State(value = Scope.Benchmark)
public class DataFillerBenchmark {

    private TestDataFiller tdf = new TestDataFiller();

    private Random random = new Random();

    @Benchmark
    public void fill_java_base_types_with_tdf(Blackhole blackhole) {
        JavaBaseTypes randomFilledInstance = tdf.createRandomFilledInstance(JavaBaseTypes.class);

        randomFilledInstance.hasNoNullValues();

        blackhole.consume(randomFilledInstance);
    }

    @Benchmark
    public void fill_java_base_interface_types_with_tdf(Blackhole blackhole) {
        JavaBaseInterfaceTypes randomFilledInstance = tdf.createRandomFilledInstance(JavaBaseInterfaceTypes.class);

        randomFilledInstance.hasNoNullValues();

        blackhole.consume(randomFilledInstance);
    }

    @Benchmark
    public void fill_java_base_types_regular(Blackhole blackhole) {
        JavaBaseTypes baseTypes = new JavaBaseTypes();

        baseTypes.setSomeBigInteger(BigInteger.valueOf(random.nextLong()));
        baseTypes.setSomeDate(new Date());
        baseTypes.setSomeList(new ArrayList());
        baseTypes.setSomeLocalDate(LocalDate.now());
        baseTypes.setSomeLocalDateTime(LocalDateTime.now());
        baseTypes.setSomeLocalTime(LocalTime.now());
        baseTypes.setSomeMap(new HashMap());
        baseTypes.setSomeSet(new HashSet());

        baseTypes.hasNoNullValues();

        blackhole.consume(baseTypes);
    }

}

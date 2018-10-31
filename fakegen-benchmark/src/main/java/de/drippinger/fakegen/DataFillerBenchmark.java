package de.drippinger.fakegen;

import de.drippinger.fakegen.types.JavaBaseTypes;
import org.mockito.Mockito;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.infra.Blackhole;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.Mockito.when;

@Fork(2)
public class DataFillerBenchmark {

    @Benchmark
    public void testdatafiller_baseline(Blackhole blackhole) {
        TestDataFiller tdf = new TestDataFiller();

        blackhole.consume(tdf);
    }

    @Benchmark
    public void fill_java_base_types_with_tdf(Blackhole blackhole) {
        TestDataFiller tdf = new TestDataFiller();

        JavaBaseTypes randomFilledInstance = tdf.createRandomFilledInstance(JavaBaseTypes.class);

        randomFilledInstance.hasNoNullValues();

        blackhole.consume(randomFilledInstance);
    }

    @Benchmark
    public void fill_mockito_mockito(Blackhole blackhole) {
        Random random = new Random();

        JavaBaseTypes mock = Mockito.mock(JavaBaseTypes.class);

        when(mock.getSomeBigInteger()).thenReturn(BigInteger.valueOf(random.nextLong()));
        when(mock.getSomeBigInteger()).thenReturn(BigInteger.valueOf(random.nextLong()));
        when(mock.getSomeDate()).thenReturn(new Date());
        when(mock.getSomeList()).thenReturn(new ArrayList());
        when(mock.getSomeLocalDate()).thenReturn(LocalDate.now());
        when(mock.getSomeLocalDateTime()).thenReturn(LocalDateTime.now());
        when(mock.getSomeLocalTime()).thenReturn(LocalTime.now());
        when(mock.getSomeMap()).thenReturn(new HashMap());
        when(mock.getSomeSet()).thenReturn(new HashSet());

        blackhole.consume(mock);
    }

    @Benchmark
    public void fill_java_base_types_regular(Blackhole blackhole) {
        Random random = new Random();

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

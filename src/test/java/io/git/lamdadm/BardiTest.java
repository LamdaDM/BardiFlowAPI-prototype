package io.git.lamdadm;

import io.git.lamdadm.services.BalanceProjectingService;
import io.git.lamdadm.services.InsightService;
import io.git.lamdadm.services.mock.MockProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class BardiTest {

    InsightService insightService = new InsightService(
        new MockProfileService(),
        new BalanceProjectingService()
    );

    @Test
    void testBalanceProjection() {

        var res = insightService
            .projectBalance(
                "",
                LocalDate.ofEpochDay(LocalDate.now().toEpochDay() + 365)
            ).blockingGet();

        var data = res.data();

        Assertions.assertEquals(53.00, data);
    }
}

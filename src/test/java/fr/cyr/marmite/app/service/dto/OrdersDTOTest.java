package fr.cyr.marmite.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.cyr.marmite.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdersTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orders.class);
        Orders orders1 = new Orders();
        orders1.setId(1L);
        Orders orders2 = new Orders();
        assertThat(orders1).isNotEqualTo(orders2);
        orders2.setId(orders1.getId());
        assertThat(orders1).isEqualTo(orders2);
        orders2.setId(2L);
        assertThat(orders1).isNotEqualTo(orders2);
        orders1.setId(null);
        assertThat(orders1).isNotEqualTo(orders2);
    }
}

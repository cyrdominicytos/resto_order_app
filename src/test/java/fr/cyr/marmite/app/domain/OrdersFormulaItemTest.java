package fr.cyr.marmite.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.cyr.marmite.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdersFormulaItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersFormulaItem.class);
        OrdersFormulaItem ordersFormulaItem1 = new OrdersFormulaItem();
        ordersFormulaItem1.setId(1L);
        OrdersFormulaItem ordersFormulaItem2 = new OrdersFormulaItem();
        ordersFormulaItem2.setId(ordersFormulaItem1.getId());
        assertThat(ordersFormulaItem1).isEqualTo(ordersFormulaItem2);
        ordersFormulaItem2.setId(2L);
        assertThat(ordersFormulaItem1).isNotEqualTo(ordersFormulaItem2);
        ordersFormulaItem1.setId(null);
        assertThat(ordersFormulaItem1).isNotEqualTo(ordersFormulaItem2);
    }
}

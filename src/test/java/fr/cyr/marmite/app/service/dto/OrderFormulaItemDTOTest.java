package fr.cyr.marmite.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.cyr.marmite.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderFormulaItemTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderFormulaItem.class);
        OrderFormulaItem orderFormulaItem1 = new OrderFormulaItem();
        orderFormulaItem1.setId(1L);
        OrderFormulaItem orderFormulaItem2 = new OrderFormulaItem();
        assertThat(orderFormulaItem1).isNotEqualTo(orderFormulaItem2);
        orderFormulaItem2.setId(orderFormulaItem1.getId());
        assertThat(orderFormulaItem1).isEqualTo(orderFormulaItem2);
        orderFormulaItem2.setId(2L);
        assertThat(orderFormulaItem1).isNotEqualTo(orderFormulaItem2);
        orderFormulaItem1.setId(null);
        assertThat(orderFormulaItem1).isNotEqualTo(orderFormulaItem2);
    }
}

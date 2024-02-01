package fr.cyr.marmite.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.cyr.marmite.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdersProductItemTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersProductItem.class);
        OrdersProductItem ordersProductItem1 = new OrdersProductItem();
        ordersProductItem1.setId(1L);
        OrdersProductItem ordersProductItem2 = new OrdersProductItem();
        assertThat(ordersProductItem1).isNotEqualTo(ordersProductItem2);
        ordersProductItem2.setId(ordersProductItem1.getId());
        assertThat(ordersProductItem1).isEqualTo(ordersProductItem2);
        ordersProductItem2.setId(2L);
        assertThat(ordersProductItem1).isNotEqualTo(ordersProductItem2);
        ordersProductItem1.setId(null);
        assertThat(ordersProductItem1).isNotEqualTo(ordersProductItem2);
    }
}

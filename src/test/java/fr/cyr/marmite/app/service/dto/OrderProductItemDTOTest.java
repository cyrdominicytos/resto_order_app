package fr.cyr.marmite.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.cyr.marmite.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderProductItemTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderProductItem.class);
        OrderProductItem orderProductItem1 = new OrderProductItem();
        orderProductItem1.setId(1L);
        OrderProductItem orderProductItem2 = new OrderProductItem();
        assertThat(orderProductItem1).isNotEqualTo(orderProductItem2);
        orderProductItem2.setId(orderProductItem1.getId());
        assertThat(orderProductItem1).isEqualTo(orderProductItem2);
        orderProductItem2.setId(2L);
        assertThat(orderProductItem1).isNotEqualTo(orderProductItem2);
        orderProductItem1.setId(null);
        assertThat(orderProductItem1).isNotEqualTo(orderProductItem2);
    }
}

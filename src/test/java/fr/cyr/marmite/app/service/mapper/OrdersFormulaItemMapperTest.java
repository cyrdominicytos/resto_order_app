package fr.cyr.marmite.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdersFormulaItemMapperTest {

    private OrdersFormulaItemMapper ordersFormulaItemMapper;

    @BeforeEach
    public void setUp() {
        ordersFormulaItemMapper = new OrdersFormulaItemMapperImpl();
    }
}

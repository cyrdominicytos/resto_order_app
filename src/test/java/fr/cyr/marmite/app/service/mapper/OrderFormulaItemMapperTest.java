package fr.cyr.marmite.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderFormulaItemMapperTest {

    private OrderFormulaItemMapper orderFormulaItemMapper;

    @BeforeEach
    public void setUp() {
        orderFormulaItemMapper = new OrderFormulaItemMapperImpl();
    }
}

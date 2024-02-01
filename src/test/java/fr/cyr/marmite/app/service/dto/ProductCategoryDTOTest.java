package fr.cyr.marmite.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.cyr.marmite.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductCategoryTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCategory.class);
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setId(1L);
        ProductCategory productCategory2 = new ProductCategory();
        assertThat(productCategory1).isNotEqualTo(productCategory2);
        productCategory2.setId(productCategory1.getId());
        assertThat(productCategory1).isEqualTo(productCategory2);
        productCategory2.setId(2L);
        assertThat(productCategory1).isNotEqualTo(productCategory2);
        productCategory1.setId(null);
        assertThat(productCategory1).isNotEqualTo(productCategory2);
    }
}

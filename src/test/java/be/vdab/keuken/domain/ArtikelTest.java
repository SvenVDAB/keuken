package be.vdab.keuken.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class ArtikelTest {
    private Artikel artikel1, artikel2;

    @BeforeEach
    void beforeEach() {
        artikel1 = new FoodArtikel("testFood", BigDecimal.ONE, BigDecimal.TEN, 5);
        artikel2 = new NonFoodArtikel("testNonFood", BigDecimal.ONE, BigDecimal.TEN, 5);
    }

    @Test
    void verhoogVerkoopprijs() {
        artikel1.verhoogVerkoopprijs(BigDecimal.ONE);
        assertThat(artikel1.getVerkoopprijs()).isEqualByComparingTo(BigDecimal.valueOf(11));
        artikel2.verhoogVerkoopprijs(BigDecimal.TEN);
        assertThat(artikel2.getVerkoopprijs()).isEqualByComparingTo(BigDecimal.valueOf(20));
    }

    @Test
    void verhoogVerkoopprijsMetNullMislukt() {
        assertThatNullPointerException().isThrownBy(
                () -> artikel1.verhoogVerkoopprijs(null)

        );
        assertThatNullPointerException().isThrownBy(
                () -> artikel2.verhoogVerkoopprijs(null)

        );
    }

    @Test
    void verhoogVerkoopprijsMet0Mislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> artikel1.verhoogVerkoopprijs(BigDecimal.ZERO)
        );
        assertThatIllegalArgumentException().isThrownBy(
                () -> artikel2.verhoogVerkoopprijs(BigDecimal.ZERO)
        );
    }

    @Test
    void verhoogVerkoopprijsMetNegatiefBedragMislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> artikel1.verhoogVerkoopprijs(BigDecimal.valueOf(-1))
        );
        assertThatIllegalArgumentException().isThrownBy(
                () -> artikel2.verhoogVerkoopprijs(BigDecimal.valueOf(-1))
        );
    }
}

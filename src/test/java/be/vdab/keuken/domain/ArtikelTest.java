package be.vdab.keuken.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class ArtikelTest {
    private Artikel artikel1;

    @BeforeEach
    void beforeEach() {
        artikel1 = new Artikel("test", BigDecimal.ONE, BigDecimal.TEN);
    }

    @Test
    void verhoogVerkoopprijs() {
        artikel1.verhoogVerkoopprijs(BigDecimal.ONE);
        assertThat(artikel1.getVerkoopprijs()).isEqualByComparingTo(BigDecimal.valueOf(11));
    }

    @Test
    void verhoogVerkoopprijsMetNullMislukt() {
        assertThatNullPointerException().isThrownBy(
                () -> artikel1.verhoogVerkoopprijs(null)
        );
    }

    @Test
    void verhoogVerkoopprijsMet0Mislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> artikel1.verhoogVerkoopprijs(BigDecimal.ZERO)
        );
    }

    @Test
    void verhoogVerkoopprijsMetNegatiefBedragMislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> artikel1.verhoogVerkoopprijs(BigDecimal.valueOf(-1))
        );
    }
}

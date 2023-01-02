package be.vdab.keuken.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class ArtikelgroepTest {
    private Artikel artikel1, artikel2;
    private Artikelgroep artikelgroep1, artikelgroep2;
    @BeforeEach
    void beforeEach() {
        artikelgroep1 = new Artikelgroep("test");
        artikelgroep2 = new Artikelgroep("test2");
        artikel1 = new FoodArtikel("testa1", BigDecimal.ONE, BigDecimal.TEN, artikelgroep1, 2);
        artikel2 = new NonFoodArtikel("testa2", BigDecimal.ONE, BigDecimal.TEN, null, 2);
    }

    @Test
    void artikel1BehoortTotArtikelgroep1() {
        assertThat(artikel1.getArtikelgroep()).isEqualTo(artikelgroep1);
        assertThat(artikelgroep1.getArtikels()).containsOnly(artikel1);
    }

    @Test
    void add() {
        assertThat(artikelgroep2.add(artikel2)).isTrue();
        assertThat(artikelgroep2.getArtikels()).containsOnly(artikel2);
        assertThat(artikel2.getArtikelgroep()).isEqualTo(artikelgroep2);
    }

    @Test
    void nullAlsArtikelToevoegenMislukt() {
        assertThatNullPointerException().isThrownBy(() -> artikelgroep1.add(null));
    }
}

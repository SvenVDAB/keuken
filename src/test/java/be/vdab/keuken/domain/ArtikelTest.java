package be.vdab.keuken.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class ArtikelTest {
    private Artikel artikel1, artikel2;
    private Artikelgroep artikelgroep1, artikelgroep2;

    @BeforeEach
    void beforeEach() {
        artikelgroep1 = new Artikelgroep("test");
        artikelgroep2 = new Artikelgroep("test2");
        artikel1 = new FoodArtikel("testFood", BigDecimal.ONE, BigDecimal.TEN, artikelgroep1, 5);
        artikel2 = new NonFoodArtikel("testNonFood", BigDecimal.ONE, BigDecimal.TEN, null, 2);
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
    void artikel1KomtVoorInArtikelGroep1() {
        assertThat(artikel1.getArtikelgroep()).isEqualTo(artikelgroep1);
        assertThat(artikelgroep1.getArtikels()).contains(artikel1);
    }

    @Test
    void setArtikelGroep() {
        artikel2.setArtikelgroep(artikelgroep2);
        assertThat(artikel2.getArtikelgroep()).isEqualTo(artikelgroep2);
        assertThat(artikelgroep2.getArtikels()).containsOnly(artikel2);
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

    @Test
    void meerdereArtikelsKunnenTotDezelfdeArtikelGroepBehoren() {
        artikel2.setArtikelgroep(artikelgroep1);
        assertThat(artikelgroep1.getArtikels()).containsOnly(artikel1, artikel2);
    }

    @Test
    void nullAlsArtikelGroepInDeSetterMislukt() {
        assertThatNullPointerException().isThrownBy(
                () -> artikel1.setArtikelgroep(null)
        );
    }
}

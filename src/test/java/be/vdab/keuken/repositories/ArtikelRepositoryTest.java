package be.vdab.keuken.repositories;

import be.vdab.keuken.domain.Artikel;
import be.vdab.keuken.domain.FoodArtikel;
import be.vdab.keuken.domain.NonFoodArtikel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Sql("/insertArtikel.sql")
@Import(ArtikelRepository.class)
public class ArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final ArtikelRepository repository;
    private static final String ARTIKELS = "artikels";

    public ArtikelRepositoryTest(ArtikelRepository repository) {
        this.repository = repository;
    }

    private long idVanTestFoodArtikel() {
        return jdbcTemplate.queryForObject(
                "select id from artikels where naam = 'testFood'", Long.class
        );
    }

    private long idVanTestNonFoodArtikel() {
        return jdbcTemplate.queryForObject(
                "select id from artikels where naam = 'testNonFood'", Long.class
        );
    }

    @Test
    void findFoodArtikelById() {
        assertThat(repository.findById(idVanTestFoodArtikel()))
                .containsInstanceOf(FoodArtikel.class)
                .hasValueSatisfying(
                        artikel -> assertThat(artikel.getNaam()).isEqualTo("testFood")
                );
    }

    @Test
    void findNonFoodArtikelById() {
        assertThat(repository.findById(idVanTestNonFoodArtikel()))
                .containsInstanceOf(NonFoodArtikel.class)
                .hasValueSatisfying(
                        artikel -> assertThat(artikel.getNaam()).isEqualTo("testNonFood")
                );
    }

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isEmpty();
    }

    @Test
    void createFoodArtikel() {
        var foodArtikel = new FoodArtikel("testFood2", BigDecimal.ONE, BigDecimal.TEN, 3);
        repository.create(foodArtikel);
        assertThat(foodArtikel.getId()).isPositive();
        assertThat(countRowsInTableWhere(ARTIKELS,
                "id=" + foodArtikel.getId())).isOne();
    }

    @Test
    void createNonFoodArtikel() {
        var nonFoodArtikel = new NonFoodArtikel("testNonFood2", BigDecimal.ONE, BigDecimal.TEN, 2);
        repository.create(nonFoodArtikel);
        assertThat(nonFoodArtikel.getId()).isPositive();
        assertThat(countRowsInTableWhere(ARTIKELS,
                "id=" + nonFoodArtikel.getId())).isOne();
    }

    @Test
    void findByNaamContains() {
        var artikels = repository.findByNaamContains("es");
        assertThat(artikels)
                .hasSize(countRowsInTableWhere(ARTIKELS, "naam like '%es%'"))
                .allSatisfy(
                        artikel -> assertThat(artikel.getNaam()).containsIgnoringCase("es"))
                .extracting(Artikel::getNaam)
                .isSortedAccordingTo(String::compareToIgnoreCase);
    }

    @Test
    void verhoogAlleVerkoopPrijzen() {
        assertThat(repository.verhoogAlleVerkoopPrijzen(BigDecimal.TEN))
                .isEqualTo(countRowsInTable(ARTIKELS));
        assertThat(countRowsInTableWhere(ARTIKELS,
                "verkoopprijs = 130 and id = " + idVanTestFoodArtikel()))
                .isOne();
        assertThat(countRowsInTableWhere(ARTIKELS,
                "verkoopprijs = 150 and id = " + idVanTestNonFoodArtikel()))
                .isOne();
    }
}

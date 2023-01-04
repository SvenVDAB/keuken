package be.vdab.keuken.repositories;

import be.vdab.keuken.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Sql({"/insertArtikelGroep.sql", "/insertArtikel.sql"})
@Import(ArtikelRepository.class)
public class ArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final ArtikelRepository repository;
    private final EntityManager manager;
    private static final String ARTIKELS = "artikels";

    public ArtikelRepositoryTest(ArtikelRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
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
        var artikelgroep = new Artikelgroep("testF");
        manager.persist(artikelgroep);
        var foodArtikel = new FoodArtikel("testFood2", BigDecimal.ONE, BigDecimal.TEN,
                artikelgroep, 3);

        repository.create(foodArtikel);
        ///manager.flush();
        assertThat(foodArtikel.getId()).isPositive();
        assertThat(countRowsInTableWhere(ARTIKELS,
                "id=" + foodArtikel.getId())).isOne();
        assertThat(artikelgroep.getArtikels().contains(foodArtikel)).isTrue();
    }

    @Test
    void createNonFoodArtikel() {
        var artikelgroep = new Artikelgroep("testNF");
        manager.persist(artikelgroep);
        var nonFoodArtikel = new NonFoodArtikel("testNonFood2", BigDecimal.ONE, BigDecimal.TEN,
                artikelgroep, 2);
        repository.create(nonFoodArtikel);
        ///manager.flush();
        assertThat(nonFoodArtikel.getId()).isPositive();
        assertThat(countRowsInTableWhere(ARTIKELS,
                "id=" + nonFoodArtikel.getId())).isOne();
        assertThat(artikelgroep.getArtikels().contains(nonFoodArtikel)).isTrue();
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
        System.out.println("\ntest findByNaamContains part 2");
        artikels = repository.findByNaamContains("");
        manager.clear();
        assertThat(artikels)
                .extracting(Artikel::getArtikelgroep)
                .extracting(Artikelgroep::getNaam)
                .isNotNull();
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

    @Test
    void kortingenLezen() {
        assertThat(repository.findById(idVanTestFoodArtikel()))
                .hasValueSatisfying(
                        artikel -> assertThat(artikel.getKortingen())
                                .containsOnly(new Korting(10, BigDecimal.valueOf(5)))
                );
        assertThat(repository.findById(idVanTestNonFoodArtikel()))
                .hasValueSatisfying(
                        artikel -> assertThat(artikel.getKortingen())
                                .containsOnly(new Korting(50, BigDecimal.valueOf(10)))
                );
    }

    @Test
    void artikelgroepLazyLoaded() {
        assertThat(repository.findById(idVanTestFoodArtikel()))
                .hasValueSatisfying(
                        artikel -> assertThat(artikel.getArtikelgroep().getNaam()).isEqualTo("test")
                );
    }
}

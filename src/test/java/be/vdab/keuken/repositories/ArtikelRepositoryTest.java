package be.vdab.keuken.repositories;

import be.vdab.keuken.domain.Artikel;
import org.junit.jupiter.api.BeforeEach;
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
    private Artikel artikel;

    public ArtikelRepositoryTest(ArtikelRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void beforeEach() {
        artikel = new Artikel("test", BigDecimal.ONE, BigDecimal.TEN);
    }

    private long idVanTestArtikel() {
        return jdbcTemplate.queryForObject(
                "select id from artikels where naam = 'test'", Long.class
        );
    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestArtikel()))
                .hasValueSatisfying(
                        artikel -> assertThat(artikel.getNaam()).isEqualTo("test")
                );
    }

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isEmpty();
    }

    @Test
    void create() {
        repository.create(artikel);
        assertThat(artikel.getId()).isPositive();
        assertThat(countRowsInTableWhere(ARTIKELS,
                "id=" + artikel.getId())).isOne();
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
                "verkoopprijs = 130 and id = " + idVanTestArtikel()))
                .isOne();
    }
}

package be.vdab.keuken.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Sql("/insertArtikel.sql")
@Import(ArtikelRepository.class)
public class ArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final ArtikelRepository repository;

    public ArtikelRepositoryTest(ArtikelRepository repository) {
        this.repository = repository;
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
}

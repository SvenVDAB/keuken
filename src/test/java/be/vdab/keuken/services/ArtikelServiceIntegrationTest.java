package be.vdab.keuken.services;

import be.vdab.keuken.repositories.ArtikelRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Import({ArtikelService.class, ArtikelRepository.class})
@Sql("/insertArtikel.sql")
class ArtikelServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String ARTIKELS = "artikels";
    private final ArtikelService service;
    private final EntityManager manager;

    public ArtikelServiceIntegrationTest(ArtikelService service, EntityManager manager) {
        this.service = service;
        this.manager = manager;
    }

    private long idVanTestArtikel() {
        return jdbcTemplate.queryForObject(
                "select id from artikels where naam = 'test'", Long.class
        );
    }

    @Test
    void verhoogVerkoopprijs() {
        var id = idVanTestArtikel();
        service.verhoogVerkoopprijs(id, BigDecimal.TEN);
        manager.flush();
        assertThat(countRowsInTableWhere(ARTIKELS, "verkoopprijs = 130 and id = " + id)).isOne();
    }
}

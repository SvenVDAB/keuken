package be.vdab.keuken.repositories;

import be.vdab.keuken.domain.Artikel;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class ArtikelRepository {

    private final EntityManager manager;

    public ArtikelRepository(EntityManager manager) {
        this.manager = manager;
    }

    public Optional<Artikel> findById(long id) {
        return Optional.ofNullable(manager.find(Artikel.class, id));
    }

    public void create(Artikel artikel) {
        manager.persist(artikel);
    }

    public List<Artikel> findByNaamContains(String woord) {
        return manager.createNamedQuery("Artikel.findByNaamContains", Artikel.class)
                .setParameter("woord", '%' + woord + '%')
                .setHint("javax.persistence.loadgraph", manager.createEntityGraph(Artikel.MET_ARTIKELGROEP))
                .getResultList();
    }

    public int verhoogAlleVerkoopPrijzen(BigDecimal prijsverhoging) {
        return manager.createNamedQuery("Artikel.verhoogAlleVerkoopPrijzen")
                .setParameter("prijsverhoging", prijsverhoging)
                .executeUpdate();
    }
}

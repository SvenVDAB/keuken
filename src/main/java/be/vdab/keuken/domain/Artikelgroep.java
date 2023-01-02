package be.vdab.keuken.domain;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "artikelgroepen")
public class Artikelgroep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;
    @OneToMany(mappedBy = "artikelgroep")
    @OrderBy("naam")
    private Set<Artikel> artikels;

    protected Artikelgroep() {
    }

    public Artikelgroep(String naam) {
        this.naam = naam;
        this.artikels = new LinkedHashSet<>();
    }

    public String getNaam() {
        return naam;
    }

    public Set<Artikel> getArtikels() {
        return Collections.unmodifiableSet(artikels);
    }

    public boolean add(Artikel artikel) {
        var toegevoegd = artikels.add(artikel);
        var oudeArtikelgroep = artikel.getArtikelgroep();
        if (oudeArtikelgroep != null && oudeArtikelgroep != this) {
            oudeArtikelgroep.artikels.remove(artikel);
        }

        if (this != oudeArtikelgroep) {
            artikel.setArtikelgroep(this);
        }
        return toegevoegd;
    }
}

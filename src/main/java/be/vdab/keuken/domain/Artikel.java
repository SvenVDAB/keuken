package be.vdab.keuken.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "artikels")
@DiscriminatorColumn(name = "soort")
public abstract class Artikel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;
    private BigDecimal aankoopprijs;
    private BigDecimal verkoopprijs;

    @ElementCollection
    @CollectionTable(name = "kortingen",
    joinColumns = @JoinColumn(name = "artikelId"))
    @OrderBy("vanafAantal")
    private Set<Korting> kortingen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artikelgroepId")
    private Artikelgroep artikelgroep;

    protected Artikel() {
    }

    public Artikel(String naam, BigDecimal aankoopprijs, BigDecimal verkoopprijs, Artikelgroep artikelgroep) {
        this.naam = naam;
        this.aankoopprijs = aankoopprijs;
        this.verkoopprijs = verkoopprijs;
        this.kortingen = new LinkedHashSet<>();
        if (artikelgroep != null) {
            setArtikelgroep(artikelgroep);
        }
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public BigDecimal getAankoopprijs() {
        return aankoopprijs;
    }

    public BigDecimal getVerkoopprijs() {
        return verkoopprijs;
    }

    public Set<Korting> getKortingen() {
        return Collections.unmodifiableSet(kortingen);
    }

    public void verhoogVerkoopprijs(BigDecimal bedrag) {
        if (bedrag.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
        verkoopprijs = verkoopprijs.add(bedrag).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Artikel artikel &&
                naam.equalsIgnoreCase(artikel.getNaam());
    }
    @Override
    public int hashCode() {
        return naam == null ? 0 : naam.toLowerCase().hashCode();
    }

    public Artikelgroep getArtikelgroep() {
        return artikelgroep;
    }

    public void setArtikelgroep(Artikelgroep artikelgroep) {
        if (!artikelgroep.getArtikels().contains(this)) {
            artikelgroep.add(this);
        }
        this.artikelgroep = artikelgroep;
    }
}

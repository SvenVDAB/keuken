package be.vdab.keuken.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("F")
public class FoodArtikel extends Artikel {
    private int houdbaarheid;

    protected FoodArtikel() {
    }

    public FoodArtikel(String naam, BigDecimal aankoopprijs, BigDecimal verkoopprijs, int houdbaarheid) {
        super(naam, aankoopprijs, verkoopprijs);
        this.houdbaarheid = houdbaarheid;
    }

    public int getHoudbaarheid() {
        return houdbaarheid;
    }
}

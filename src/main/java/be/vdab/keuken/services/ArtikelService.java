package be.vdab.keuken.services;

import be.vdab.keuken.exceptions.ArtikelNietGevondenException;
import be.vdab.keuken.repositories.ArtikelRepository;

import java.math.BigDecimal;

public class ArtikelService {
    private final ArtikelRepository artikelRepository;

    public ArtikelService(ArtikelRepository artikelRepository) {
        this.artikelRepository = artikelRepository;
    }

    public void verhoogVerkoopprijs(long id, BigDecimal bedrag) {
        artikelRepository.findById(id)
                .orElseThrow(ArtikelNietGevondenException::new)
                .verhoogVerkoopprijs(bedrag);
    }
}

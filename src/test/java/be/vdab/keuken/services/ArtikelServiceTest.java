package be.vdab.keuken.services;

import be.vdab.keuken.domain.Artikel;
import be.vdab.keuken.exceptions.ArtikelNietGevondenException;
import be.vdab.keuken.repositories.ArtikelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArtikelServiceTest {
    private ArtikelService service;
    @Mock
    private ArtikelRepository repository;
    private Artikel artikel;

    @BeforeEach
    void beforeEach() {
        service = new ArtikelService(repository);
        artikel = new Artikel("test", BigDecimal.ONE, BigDecimal.TEN);
    }

    @Test
    void verhoogVerkoopprijs() {
        when(repository.findById(1)).thenReturn(Optional.of(artikel));
        service.verhoogVerkoopprijs(1, BigDecimal.ONE);
        assertThat(artikel.getVerkoopprijs()).isEqualByComparingTo("11");
        verify(repository).findById(1);
    }

    @Test
    void verhoogVerkoopprijsVoorOnbestaandArtikel() {
        assertThatExceptionOfType(ArtikelNietGevondenException.class).isThrownBy(
                () -> service.verhoogVerkoopprijs(-1, BigDecimal.ONE)
        );
        verify(repository).findById(-1);
    }
}

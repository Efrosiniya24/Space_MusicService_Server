package by.space.mediacontent.artist.application.service;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;

import java.util.List;

public interface ArtistService {
    /**
     * method for creation artist
     *
     * @param artistCreateDto data for creation artist
     * @return saving artist
     */
    ArtistCreateDto createArtist(ArtistCreateDto artistCreateDto);

    ArtistCreateDto updateArtist(Long id, ArtistCreateDto artistCreateDto);

    List<ArtistCreateDto> getAllArtists();

    List<ArtistCreateDto> searchArtists(String query);

    ArtistCreateDto getArtistById(Long id);

    /**
     * Помечает исполнителя удалённым (не показывается в каталоге).
     * Если есть связанные треки или альбомы — исключение.
     */
    void deleteArtist(Long id);

    /**
     * Импорт каталога: вернуть существующего неудалённого артиста с таким именем (без учёта регистра)
     * или создать нового. Если имя из тегов пустое — используется {@code fallbackName} или имя из конфигурации.
     */
    ArtistCreateDto ensureArtistForImport(String nameFromTags, String fallbackName);
}

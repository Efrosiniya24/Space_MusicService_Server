package by.space.mediacontent.content.service;

import by.space.mediacontent.content.dto.AlbumCreateDto;
import by.space.mediacontent.content.dto.AlbumPatchDto;
import by.space.mediacontent.content.dto.AlbumResponseDto;

import java.util.List;

public interface AlbumService {

    AlbumResponseDto createAlbumForArtist(Long artistId, AlbumCreateDto request);

    List<AlbumResponseDto> listAlbumsByArtist(Long artistId);

    void removeAlbumFromArtist(Long artistId, Long albumId);

    void restoreAlbumForArtist(Long artistId, Long albumId);

    void finalizeRemoveAlbumFromArtist(Long artistId, Long albumId);

    AlbumResponseDto addTrackToAlbum(Long artistId, Long albumId, Long trackId);

    AlbumResponseDto patchAlbum(Long artistId, Long albumId, AlbumPatchDto body);
}

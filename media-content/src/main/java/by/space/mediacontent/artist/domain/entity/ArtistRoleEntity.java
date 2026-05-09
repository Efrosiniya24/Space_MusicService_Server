package by.space.mediacontent.artist.domain.entity;

import by.space.mediacontent.artist.domain.enums.ArtistRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "artist_role")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "artist_id", nullable = false)
    private Long artistId;

    @Column(name = "role_name", nullable = false, length = 120)
    @Enumerated(EnumType.STRING)
    private ArtistRole roleName;
}

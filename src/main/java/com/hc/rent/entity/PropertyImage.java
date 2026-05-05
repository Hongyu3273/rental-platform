package com.hc.rent.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "property_images")
public class PropertyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many images belong to one property
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    // S3 image URL
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    // Display order (1 = first, 2 = second...)
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    // Whether this is the cover/thumbnail image
    @Column(name = "is_cover", nullable = false)
    private Boolean isCover = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
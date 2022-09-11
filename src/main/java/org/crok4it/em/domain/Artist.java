package org.crok4it.em.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "t_artist")
public class Artist extends BaseEntity{

    @Column(name="c_name", nullable = false, length = 128)
    private String name;
    @Column(name="c_phone", nullable = false, length = 32, unique = true)
    private String phone;
    @Column(name="c_city", nullable = false)
    private String city;
    @Column(name="c_facebook_link", unique = true, length = 512)
    private String facebookLink;
    @Column(name="c_image_link", unique = true, length = 512)
    private String imageLink;
    @Column(name="c_website_link", unique = true, length = 512)
    private String websiteLink;
    @Column(name="c_seeking_venue")
    private Boolean seekingVenue;
    @Column(name="c_seeking_description", length = 512)
    private String seekingDescription;

    @Builder
    public Artist(String id, String name, String phone, String city, String facebookLink, String imageLink,
                  String websiteLink, Boolean seekingVenue, String seekingDescription) {
        super(id);
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.facebookLink = facebookLink;
        this.imageLink = imageLink;
        this.websiteLink = websiteLink;
        this.seekingVenue = seekingVenue;
        this.seekingDescription = seekingDescription;
    }
}

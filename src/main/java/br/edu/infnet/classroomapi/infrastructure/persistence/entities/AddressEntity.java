package br.edu.infnet.classroomapi.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {
    
    @Column(name = "address_street", length = 200)
    private String street;
    
    @Column(name = "address_number", length = 20)
    private String number;
    
    @Column(name = "address_complement", length = 100)
    private String complement;
    
    @Column(name = "address_neighborhood", length = 100)
    private String neighborhood;
    
    @Column(name = "address_city", length = 100)
    private String city;
    
    @Column(name = "address_state", length = 50)
    private String state;
    
    @Column(name = "address_zip_code", length = 20)
    private String zipCode;
    
    @Column(name = "address_country", length = 50)
    private String country;
}
package br.edu.infnet.classroomapi.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public static Address of(String street, String number, String complement, 
                           String neighborhood, String city, String state, 
                           String zipCode, String country) {
        return new Address(street, number, complement, neighborhood, 
                          city, state, zipCode, country);
    }

    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder();
        
        if (street != null && !street.trim().isEmpty()) {
            fullAddress.append(street);
            if (number != null && !number.trim().isEmpty()) {
                fullAddress.append(", ").append(number);
            }
        }
        
        if (complement != null && !complement.trim().isEmpty()) {
            fullAddress.append(", ").append(complement);
        }
        
        if (neighborhood != null && !neighborhood.trim().isEmpty()) {
            fullAddress.append(" - ").append(neighborhood);
        }
        
        if (city != null && !city.trim().isEmpty()) {
            fullAddress.append(", ").append(city);
        }
        
        if (state != null && !state.trim().isEmpty()) {
            fullAddress.append(" - ").append(state);
        }
        
        if (zipCode != null && !zipCode.trim().isEmpty()) {
            fullAddress.append(", ").append(zipCode);
        }
        
        if (country != null && !country.trim().isEmpty()) {
            fullAddress.append(" - ").append(country);
        }
        
        return fullAddress.toString();
    }
}
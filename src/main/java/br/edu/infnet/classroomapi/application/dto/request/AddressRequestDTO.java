package br.edu.infnet.classroomapi.application.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {

    @Size(max = 200, message = "Street must not exceed 200 characters")
    private String street;

    @Size(max = 20, message = "Number must not exceed 20 characters")
    private String number;

    @Size(max = 100, message = "Complement must not exceed 100 characters")
    private String complement;

    @Size(max = 100, message = "Neighborhood must not exceed 100 characters")
    private String neighborhood;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 50, message = "State must not exceed 50 characters")
    private String state;

    @Size(max = 20, message = "Zip code must not exceed 20 characters")
    private String zipCode;

    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country;
}
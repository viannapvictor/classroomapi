package br.edu.infnet.classroomapi.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Address Entity Tests")
class AddressTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create address with valid data")
        void shouldCreateAddressWithValidData() {
            String street = "Rua das Flores";
            String number = "123";
            String complement = "Apto 45";
            String neighborhood = "Centro";
            String city = "São Paulo";
            String state = "SP";
            String zipCode = "01234-567";
            String country = "Brasil";

            Address address = new Address(street, number, complement, neighborhood, city, state, zipCode, country);

            assertNotNull(address);
            assertEquals(street, address.getStreet());
            assertEquals(number, address.getNumber());
            assertEquals(complement, address.getComplement());
            assertEquals(neighborhood, address.getNeighborhood());
            assertEquals(city, address.getCity());
            assertEquals(state, address.getState());
            assertEquals(zipCode, address.getZipCode());
            assertEquals(country, address.getCountry());
        }

        @Test
        @DisplayName("Should create address with minimal data")
        void shouldCreateAddressWithMinimalData() {
            Address address = new Address("Rua A", "100", null, "Centro", "SP", "SP", "12345-678", "Brasil");

            assertNotNull(address);
            assertEquals("Rua A", address.getStreet());
            assertEquals("100", address.getNumber());
            assertNull(address.getComplement());
            assertEquals("Centro", address.getNeighborhood());
            assertEquals("SP", address.getCity());
            assertEquals("SP", address.getState());
            assertEquals("12345-678", address.getZipCode());
            assertEquals("Brasil", address.getCountry());
        }

        @Test
        @DisplayName("Should create address using static factory method")
        void shouldCreateAddressUsingStaticFactoryMethod() {
            Address address = Address.of("Rua das Rosas", "456", "Casa", "Jardim", "Rio de Janeiro", "RJ", "20000-000", "Brasil");

            assertNotNull(address);
            assertEquals("Rua das Rosas", address.getStreet());
            assertEquals("456", address.getNumber());
            assertEquals("Casa", address.getComplement());
            assertEquals("Jardim", address.getNeighborhood());
            assertEquals("Rio de Janeiro", address.getCity());
            assertEquals("RJ", address.getState());
            assertEquals("20000-000", address.getZipCode());
            assertEquals("Brasil", address.getCountry());
        }
    }

    @Nested
    @DisplayName("Full Address Method Tests")
    class FullAddressMethodTests {

        @Test
        @DisplayName("Should return full formatted address")
        void shouldReturnFullFormattedAddress() {
            Address address = new Address("Rua das Flores", "123", "Apto 45", "Centro", "São Paulo", "SP", "01234-567", "Brasil");

            String fullAddress = address.getFullAddress();

            assertNotNull(fullAddress);
            assertTrue(fullAddress.contains("Rua das Flores"));
            assertTrue(fullAddress.contains("123"));
            assertTrue(fullAddress.contains("Apto 45"));
            assertTrue(fullAddress.contains("Centro"));
            assertTrue(fullAddress.contains("São Paulo"));
            assertTrue(fullAddress.contains("SP"));
            assertTrue(fullAddress.contains("01234-567"));
            assertTrue(fullAddress.contains("Brasil"));
        }

        @Test
        @DisplayName("Should handle null complement in full address")
        void shouldHandleNullComplementInFullAddress() {
            Address address = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");

            String fullAddress = address.getFullAddress();

            assertNotNull(fullAddress);
            assertFalse(fullAddress.contains("null"));
        }

        @Test
        @DisplayName("Should handle empty fields gracefully")
        void shouldHandleEmptyFieldsGracefully() {
            Address address = new Address("Rua A", "", "", "Centro", "São Paulo", "SP", "", "");

            String fullAddress = address.getFullAddress();

            assertNotNull(fullAddress);
            assertTrue(fullAddress.contains("Rua A"));
            assertTrue(fullAddress.contains("Centro"));
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal for same address data")
        void shouldBeEqualForSameAddressData() {
            Address address1 = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
            Address address2 = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");

            assertEquals(address1, address2);
            assertEquals(address1.hashCode(), address2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different street")
        void shouldNotBeEqualForDifferentStreet() {
            Address address1 = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
            Address address2 = new Address("Rua das Rosas", "456", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");

            assertNotEquals(address1, address2);
        }

        @Test
        @DisplayName("Should not be equal for different neighborhood")
        void shouldNotBeEqualForDifferentNeighborhood() {
            Address address1 = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
            Address address2 = new Address("Rua das Flores", "123", null, "Bela Vista", "São Paulo", "SP", "01234-567", "Brasil");

            assertNotEquals(address1, address2);
        }

        @Test
        @DisplayName("Should not be equal for different city")
        void shouldNotBeEqualForDifferentCity() {
            Address address1 = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
            Address address2 = new Address("Rua das Flores", "123", null, "Centro", "Rio de Janeiro", "RJ", "01234-567", "Brasil");

            assertNotEquals(address1, address2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            Address address = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");

            assertNotEquals(address, null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            Address address = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
            String string = "Rua das Flores, 123";

            assertNotEquals(address, string);
        }
    }
}
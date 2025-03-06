package org.example.api.hotel.util;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import org.example.api.hotel.dto.request.AddressRequestDto;
import org.example.api.hotel.dto.request.ArrivalTimeRequestDto;
import org.example.api.hotel.dto.request.ContactRequestDto;
import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.dto.response.AddressResponseDto;
import org.example.api.hotel.dto.response.ArrivalTimeResponseDto;
import org.example.api.hotel.dto.response.ContactResponseDto;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.dto.response.ShortHotelInfoResponseDto;
import org.example.api.hotel.model.Address;
import org.example.api.hotel.model.Amenity;
import org.example.api.hotel.model.ArrivalTime;
import org.example.api.hotel.model.Contact;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.HotelAmenity;
import org.example.api.hotel.repository.ContactRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class TestDataBuilderUtil {
    private static final Faker faker = new Faker();

    public static Hotel generateHotelWithRelations(ContactRepository contactRepository) {
        var contact = contactRepository.save(generateValidContact());

        return Hotel.builder()
                .name(faker.company().name())
                .description(faker.lorem().sentence(10))
                .brand(faker.company().industry())
                .address(generateAddress())
                .contact(contact)
                .arrivalTime(generateArrivalTime())
                .hotelAmenities(generateHotelAmenities())
                .build();
    }

    public static Address generateAddress() {
        return Address.builder()
                .houseNumber(faker.number().numberBetween(1, 200))
                .street(faker.address().streetName())
                .city(faker.address().city())
                .country(faker.address().country())
                .postCode(faker.numerify("#####"))
                .build();
    }

    public static ArrivalTime generateArrivalTime() {
        return ArrivalTime.builder()
                .checkIn(LocalTime.of(faker.number().numberBetween(12, 16), 0))
                .checkOut(LocalTime.of(faker.number().numberBetween(10, 14), 0))
                .build();
    }

    public static List<HotelAmenity> generateHotelAmenities() {
        return generateAmenities().stream()
                .map(amenity -> HotelAmenity.builder().amenity(amenity).build())
                .collect(Collectors.toList());
    }

    public static List<Amenity> generateAmenities() {
        return List.of(
                Amenity.builder()
                        .name(faker.commerce().productName())
                        .build(),
                Amenity.builder()
                        .name(faker.commerce().productName())
                        .build()
        );
    }

    public static Contact generateValidContact() {
        return Contact.builder()
                .email(faker.internet().emailAddress())
                .phone(generateValidPhoneNumber())
                .build();
    }

    public static CreateHotelRequestDto generateCreateHotelRequest() {
        return CreateHotelRequestDto.builder()
                .name(faker.company().name())
                .description(faker.lorem().sentence())
                .brand(faker.company().industry())
                .address(generateAddressRequest())
                .contacts(generateContactRequest())
                .arrivalTime(generateArrivalTimeRequest())
                .build();
    }

    public static AddressRequestDto generateAddressRequest() {
        return AddressRequestDto.builder()
                .houseNumber(faker.number().numberBetween(1, 999))
                .street(faker.address().streetName())
                .city(faker.address().city())
                .country(faker.address().country())
                .postCode(faker.number().digits(6))
                .build();
    }

    public static ContactRequestDto generateContactRequest() {
        return ContactRequestDto.builder()
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().cellPhone())
                .build();
    }

    public static ArrivalTimeRequestDto generateArrivalTimeRequest() {
        return ArrivalTimeRequestDto.builder()
                .checkIn("14:00")
                .checkOut("12:00")
                .build();
    }

    public static Hotel generateHotel() {
        return Hotel.builder()
                .id(UUID.randomUUID())
                .name(faker.company().name())
                .description(faker.lorem().sentence())
                .brand(faker.company().industry())
                .address(Address.builder()
                        .houseNumber(faker.number().numberBetween(1, 200))
                        .street(faker.address().streetName())
                        .city(faker.address().city())
                        .country(faker.address().country())
                        .postCode(faker.number().digits(6))
                        .build())
                .contact(Contact.builder()
                        .email(faker.internet().emailAddress())
                        .phone(faker.phoneNumber().cellPhone())
                        .build())
                .arrivalTime(ArrivalTime.builder()
                        .checkIn(LocalTime.of(faker.number().numberBetween(12, 16), 0))
                        .checkOut(LocalTime.of(faker.number().numberBetween(10, 14), 0))
                        .build())
                .hotelAmenities(generateHotelAmenities())
                .build();
    }

    public static ShortHotelInfoResponseDto generateShortHotelInfoResponse() {
        return new ShortHotelInfoResponseDto(
                UUID.randomUUID(),
                faker.company().name(),
                faker.lorem().sentence(),
                faker.address().fullAddress(),
                faker.phoneNumber().cellPhone()
        );
    }

    public static FullHotelInfoResponseDto generateFullHotelInfoResponse() {
        return new FullHotelInfoResponseDto(
                UUID.randomUUID(),
                faker.company().name(),
                faker.lorem().sentence(),
                faker.company().industry(),
                generateAddressResponse(),
                generateContactResponse(),
                generateArrivalTimeResponse(),
                List.of("WiFi", "Pool", "Parking")
        );
    }

    public static AddressResponseDto generateAddressResponse() {
        return new AddressResponseDto(
                faker.number().numberBetween(1, 999),
                faker.address().streetAddress(),
                faker.address().city(),
                faker.address().country(),
                faker.number().digits(6)
        );
    }

    public static ContactResponseDto generateContactResponse() {
        return new ContactResponseDto(
                faker.internet().emailAddress(),
                faker.phoneNumber().cellPhone()
        );
    }

    public static ArrivalTimeResponseDto generateArrivalTimeResponse() {
        return new ArrivalTimeResponseDto(
                "14:00",
                "12:00"
        );
    }

    public static List<String> generateBrands() {
        return IntStream.range(0, 5)
                .mapToObj(i -> faker.company().name())
                .toList();
    }

    private static String generateValidPhoneNumber() {
        return "+1" + faker.number().digits(9);
    }
}
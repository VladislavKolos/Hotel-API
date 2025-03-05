package org.example.api.hotel.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.dto.request.HotelFilterRequestDto;
import org.example.api.hotel.dto.request.HotelSearchRequestDto;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.dto.response.ShortHotelInfoResponseDto;
import org.example.api.hotel.exception.EntityHotelNotFoundException;
import org.example.api.hotel.exception.EntityHotelSaveException;
import org.example.api.hotel.mapper.HotelMapper;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.enums.HotelGroupingParameter;
import org.example.api.hotel.repository.ContactRepository;
import org.example.api.hotel.repository.HotelAmenityRepository;
import org.example.api.hotel.repository.HotelRepository;
import org.example.api.hotel.repository.specification.HotelSpecification;
import org.example.api.hotel.service.HotelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelMapper hotelMapper;
    private final HotelRepository hotelRepository;
    private final ContactRepository contactRepository;
    private final HotelAmenityRepository hotelAmenityRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ShortHotelInfoResponseDto> getAllHotelsWithShortInfo(Pageable pageable) {
        return hotelRepository.findAllHotelsWithContactPageable(pageable)
                .map(hotelMapper::toShortHotelInfoResponseDto);
    }

    @Override
    @Transactional
    public ShortHotelInfoResponseDto createHotel(CreateHotelRequestDto request) {
        return Optional.of(request)
                .map(hotelMapper::toEntity)
                .map(hotel -> {
                    contactRepository.save(hotel.getContact());
                    return hotelRepository.save(hotel);
                })
                .map(hotelMapper::toShortHotelInfoResponseDto)
                .orElseThrow(
                        () -> new EntityHotelSaveException("Failed to save hotel. Please check the request data."));
    }

    @Override
    @Transactional(readOnly = true)
    public FullHotelInfoResponseDto getHotelById(UUID id) {
        return hotelRepository.findById(id)
                .map(hotelMapper::toFullHotelInfoResponseDto)
                .orElseThrow(() -> new EntityHotelNotFoundException("Hotel with ID " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShortHotelInfoResponseDto> searchHotels(HotelSearchRequestDto request, Pageable pageable) {
        var filter = buildHotelFilterRequestDto(request.getName(), request.getBrand(), request.getCity(),
                request.getCountry());
        var hotels = getHotels(filter, request.getAmenities(), pageable);

        return hotels.map(hotelMapper::toShortHotelInfoResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> countHotelsGroupedByParameter(HotelGroupingParameter param) {
        return switch (param) {
            case BRAND -> getGroupedHistogram(hotelRepository::findAllBrands);
            case CITY -> getGroupedHistogram(hotelRepository::findAllCities);
            case COUNTRY -> getGroupedHistogram(hotelRepository::findAllCountries);
            case AMENITIES -> getAmenitiesHistogram();
        };
    }

    private Map<String, Long> getGroupedHistogram(Supplier<List<String>> supplier) {
        return supplier.get()
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private Map<String, Long> getAmenitiesHistogram() {
        return hotelAmenityRepository.findAllHotelAmenities()
                .stream()
                .map(hotelAmenity -> hotelAmenity.getAmenity().getName())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private HotelFilterRequestDto buildHotelFilterRequestDto(String name, String brand, String city, String country) {
        return HotelFilterRequestDto.builder()
                .name(name)
                .brand(brand)
                .city(city)
                .country(country)
                .build();
    }

    private Page<Hotel> getHotels(HotelFilterRequestDto filter, List<String> amenities, Pageable pageable) {
        var specification = HotelSpecification.filterBy(filter);

        if (amenities == null || amenities.isEmpty()) {
            return hotelRepository.findAll(specification, pageable);
        }

        List<Hotel> hotelsByAmenities = getHotelsByAmenities(amenities);
        List<Hotel> filteredHotels = filterHotelsBySpecification(hotelsByAmenities, specification);

        return toPage(filteredHotels, pageable);
    }

    private List<Hotel> getHotelsByAmenities(List<String> amenities) {
        return amenities.stream()
                .flatMap(amenity -> Optional.ofNullable(
                                hotelAmenityRepository.findHotelsByAmenityFilter(amenity, Pageable.unpaged()))
                        .orElse(Page.empty())
                        .stream())
                .distinct()
                .toList();
    }

    private List<Hotel> filterHotelsBySpecification(List<Hotel> hotels, Specification<Hotel> specification) {
        return hotelRepository.findAll(specification).stream()
                .filter(hotels::contains)
                .toList();
    }

    private Page<Hotel> toPage(List<Hotel> hotels, Pageable pageable) {
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), hotels.size());

        List<Hotel> pagedHotels = hotels.subList(start, end);

        return new PageImpl<>(pagedHotels, pageable, hotels.size());
    }
}
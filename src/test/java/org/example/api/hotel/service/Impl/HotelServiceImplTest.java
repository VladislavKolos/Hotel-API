package org.example.api.hotel.service.Impl;

import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.dto.response.ShortHotelInfoResponseDto;
import org.example.api.hotel.mapper.HotelMapper;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.enums.HotelGroupingParameter;
import org.example.api.hotel.repository.ContactRepository;
import org.example.api.hotel.repository.HotelRepository;
import org.example.api.hotel.util.TestDataBuilderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelServiceImplTest {

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Mock
    private HotelMapper hotelMapper;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ContactRepository contactRepository;

    private Hotel hotel;
    private Pageable pageable;
    private CreateHotelRequestDto createHotelRequest;
    private FullHotelInfoResponseDto fullHotelInfoResponse;
    private ShortHotelInfoResponseDto shortHotelInfoResponse;

    @BeforeEach
    public void setUp() {
        hotel = TestDataBuilderUtil.generateHotel();
        pageable = PageRequest.of(0, 10);
        createHotelRequest = TestDataBuilderUtil.generateCreateHotelRequest();
        fullHotelInfoResponse = TestDataBuilderUtil.generateFullHotelInfoResponse();
        shortHotelInfoResponse = TestDataBuilderUtil.generateShortHotelInfoResponse();
    }

    @Test
    public void testCreateHotelSuccess() {
        when(hotelMapper.toEntity(createHotelRequest)).thenReturn(hotel);
        when(contactRepository.save(hotel.getContact())).thenReturn(hotel.getContact());
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toShortHotelInfoResponseDto(hotel)).thenReturn(shortHotelInfoResponse);

        var result = hotelService.createHotel(createHotelRequest);

        assertThat(result).isEqualTo(shortHotelInfoResponse);
        verify(contactRepository).save(hotel.getContact());
        verify(hotelRepository).save(hotel);
    }

    @Test
    public void testGetAllHotelsWithShortInfoSuccess() {
        Page<Hotel> hotelPage = new PageImpl<>(List.of(hotel), pageable, 1);

        when(hotelRepository.findAllHotelsWithContactPageable(pageable)).thenReturn(hotelPage);
        when(hotelMapper.toShortHotelInfoResponseDto(hotel)).thenReturn(shortHotelInfoResponse);

        Page<ShortHotelInfoResponseDto> result = hotelService.getAllHotelsWithShortInfo(pageable);

        assertThat(result.getContent()).isNotEmpty().hasSize(1).containsExactly(shortHotelInfoResponse);
        verify(hotelRepository).findAllHotelsWithContactPageable(pageable);
    }

    @Test
    public void testGetHotelById() {
        var hotelId = hotel.getId();

        when(hotelRepository.findById(hotelId)).thenReturn(java.util.Optional.of(hotel));
        when(hotelMapper.toFullHotelInfoResponseDto(hotel)).thenReturn(fullHotelInfoResponse);

        var result = hotelService.getHotelById(hotelId);

        assertThat(result).isEqualTo(fullHotelInfoResponse);
        verify(hotelRepository).findById(hotelId);
    }

    @Test
    public void testCountHotelsGroupedByParameterSuccess() {
        List<String> brands = TestDataBuilderUtil.generateBrands();

        when(hotelRepository.findAllBrands()).thenReturn(brands);

        Map<String, Long> result = hotelService.countHotelsGroupedByParameter(HotelGroupingParameter.BRAND);

        assertThat(result).isNotEmpty().hasSize(brands.size());
        verify(hotelRepository).findAllBrands();
    }
}
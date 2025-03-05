package org.example.api.hotel.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.experimental.UtilityClass;
import org.example.api.hotel.dto.request.HotelFilterRequestDto;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.util.HotelApiConstantUtil;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class HotelSpecification {
    public static Specification<Hotel> filterBy(HotelFilterRequestDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addLikePredicateIfNotNull(predicates, root, criteriaBuilder, filter.getName(),
                    hotel -> hotel.get(HotelApiConstantUtil.NAME));
            addLikePredicateIfNotNull(predicates, root, criteriaBuilder, filter.getBrand(),
                    hotel -> hotel.get(HotelApiConstantUtil.BRAND));
            addLikePredicateIfNotNull(predicates, root, criteriaBuilder, filter.getCity(),
                    hotel -> hotel.get(HotelApiConstantUtil.ADDRESS).get(HotelApiConstantUtil.CITY));
            addLikePredicateIfNotNull(predicates, root, criteriaBuilder, filter.getCountry(),
                    hotel -> hotel.get(HotelApiConstantUtil.ADDRESS).get(HotelApiConstantUtil.COUNTRY));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addLikePredicateIfNotNull(List<Predicate> predicates, Root<Hotel> root,
                                                  CriteriaBuilder criteriaBuilder, String value,
                                                  Function<Root<Hotel>, Path<String>> pathFunction
    ) {
        if (value != null) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(pathFunction.apply(root)),
                    "%" + value.toLowerCase() + "%"
            ));
        }
    }
}
package org.example.api.hotel.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@Data
@ConfigurationProperties(prefix = "spring.jpa")
public class HibernateProperties {

    @NotBlank
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @NotNull
    @Value("${spring.jpa.show-sql}")
    private Boolean showSql;

    @NotBlank
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    public Properties toProperties() {
        var properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", Boolean.toString(showSql));
        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        return properties;
    }
}

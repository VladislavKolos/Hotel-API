package org.example.api.hotel.repository;

import org.example.api.hotel.model.Contact;
import org.example.api.hotel.util.TestDataBuilderUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    private Contact contact;

    @BeforeEach
    void setUp() {
        contact = TestDataBuilderUtil.generateValidContact();
    }

    @AfterEach
    public void cleanUp() {
        contactRepository.deleteAll();
    }

    @Test
    public void testExistsByEmailSuccess() {
        contactRepository.save(contact);

        boolean exists = contactRepository.existsByEmail(contact.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByEmailFail() {
        boolean exists = contactRepository.existsByEmail(contact.getEmail());

        assertThat(exists).isFalse();
    }

    @Test
    public void testExistsByPhoneSuccess() {
        contactRepository.save(contact);

        boolean exists = contactRepository.existsByPhone(contact.getPhone());

        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByPhoneFail() {
        boolean exists = contactRepository.existsByPhone(contact.getPhone());

        assertThat(exists).isFalse();
    }
}
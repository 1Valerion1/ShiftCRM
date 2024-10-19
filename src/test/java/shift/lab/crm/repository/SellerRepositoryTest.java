package shift.lab.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.repository.SellerRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
public class SellerRepositoryTest {
    @Autowired
    private SellerRepository sellerRepository;

    @BeforeEach
    public void setUp() {
        sellerRepository.deleteAll();
    }

    @Test
    public void testExistsById() {
        Seller seller = new Seller();
        seller.setName("SellerTest");
        seller.setContactInfo("contact@test.com");
        sellerRepository.save(seller);

        assertTrue(sellerRepository.existsById(seller.getId()));
    }

    @Test
    public void testExistsByName() {
        Seller seller = new Seller();
        seller.setName("UniqueName");
        seller.setContactInfo("contact@test.com");
        sellerRepository.save(seller);

        assertTrue(sellerRepository.existsByName("UniqueName"));
        assertFalse(sellerRepository.existsByName("NonExistentName"));
    }

    @Test
    public void testExistsByContactInfo() {
        Seller seller = new Seller();
        seller.setName("SampleName");
        seller.setContactInfo("unique@contact.com");
        sellerRepository.save(seller);

        assertTrue(sellerRepository.existsByContactInfo("unique@contact.com"));
        assertFalse(sellerRepository.existsByContactInfo("nonexistent@contact.com"));
    }

    @Test
    void testUpdateSellerName() {
        Seller seller = new Seller();
        seller.setName("OldName");
        seller.setContactInfo("old@contact.com");
        Seller sellerSaved = sellerRepository.save(seller);

        int updatedCount = sellerRepository.updateSellerName(sellerSaved.getId(), "NewName");

        assertEquals(1, updatedCount);

        Seller updatedSeller = sellerRepository.findById(sellerSaved.getId()).orElseThrow();
        assertEquals("NewName", updatedSeller.getName());
    }

    @Test
    void testUpdateSellerContactInfo() {
        Seller seller = new Seller();
        seller.setName("SampleName");
        seller.setContactInfo("old@contactinfo.com");
        Seller sellerSaved = sellerRepository.save(seller);


        int updatedCount = sellerRepository.updateSellerContactInfo(sellerSaved.getId(), "new@contactinfo.com");
        assertEquals(1, updatedCount);


        Seller updatedSeller = sellerRepository.findById(sellerSaved.getId()).orElseThrow();
        assertEquals("new@contactinfo.com", updatedSeller.getContactInfo());
    }

    @Test
    void testUpdateSellerNameAndContactInfo() {
        Seller seller = new Seller();
        seller.setName("OriginalName");
        seller.setContactInfo("original@contactinfo.com");
        Seller sellerSaved = sellerRepository.save(seller);


        int updatedCount = sellerRepository.updateSellerNameAndContactInfo(
                sellerSaved.getId(),
                "UpdatedName",
                "updated@contactinfo.com");

        assertEquals(1, updatedCount);


        Seller updatedSeller = sellerRepository.findById(sellerSaved.getId()).orElseThrow();
        assertEquals("UpdatedName", updatedSeller.getName());
        assertEquals("updated@contactinfo.com", updatedSeller.getContactInfo());
    }
}

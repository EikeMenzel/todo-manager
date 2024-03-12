package org.authenticationservice.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PasswordServiceTest {

    @Autowired
    private PasswordService passwordSecurityService;

    @Test
    void testCheckPasswordSecurity_ValidPassword1() {
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123!"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123~"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123`"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123!"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123@"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123#"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123$"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123%"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123^"));
    }

    @Test
    void testCheckPasswordSecurity_ValidPassword2() {
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123&"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123*"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123()"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123_-"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123+"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123={[}]"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123|"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123:"));
        assertTrue(passwordSecurityService.checkPasswordSecurity("ValidPass123;<,>.?/"));
    }

    @Test
    void testCheckPasswordSecurity_NoDigit() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("NoDigitSymbol!"));
    }

    @Test
    void testCheckPasswordSecurity_NoSymbol() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("NoSymbol123"));
    }

    @Test
    void testCheckPasswordSecurity_UnknownChar() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("UnknownChar×‼123"));
    }

    @Test
    void testCheckPasswordSecurity_UpperCaseMissing() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("mismatchedcase1!".toLowerCase()));
    }

    @Test
    void testCheckPasswordSecurity_LowerCaseMissing() {
        assertFalse(passwordSecurityService.checkPasswordSecurity("mismatchedcase1!".toUpperCase()));
    }
}

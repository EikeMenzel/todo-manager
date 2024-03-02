package org.authenticationservice.services;

import org.springframework.stereotype.Service;

@Service
public class PasswordService implements IPasswordService{
    @Override
    public boolean checkPasswordSecurity(String password) {
        var hasDigit = false;
        var hasSymbol = false;
        var hasUnknownChar = false;
        var hasUppercase = !password.equals(password.toLowerCase());
        var hasLowercase = !password.equals(password.toUpperCase());

        for (char c: password.toCharArray()) {
            if (Character.isLetter(c)) continue;

            if (Character.isDigit(c)) {
                hasDigit = true;
                continue;
            }

            var allowedSymbols = "~`! @#$%^&*()_-+={[}]|:;<,>.?/";
            if(allowedSymbols.indexOf(c) != -1)
                hasSymbol = true;
            else
                hasUnknownChar = true;
        }

        return hasDigit && hasUppercase && hasLowercase && hasSymbol && !hasUnknownChar;
    }
}

package pl.swieconek.anti_fraud_system.validator;

import org.springframework.stereotype.Component;
import pl.swieconek.anti_fraud_system.model.Region;

@Component
public class DataValidator {

    public boolean validateIp(String ip) {
        String[] splitIp = ip.split("\\.");

        if (splitIp.length != 4) {
            return false;
        }

        for (String c : splitIp) {
            try {
                int parsedC = Integer.parseInt(c);

                if (parsedC < 0 || parsedC > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public boolean validateCard(String cardNumber) {
        if (cardNumber.length() != 16) {
            return false;
        }

        int sum = 0;

        for (int i = 0; i < 15; i++) {
            char ch = cardNumber.charAt(i);
            int digitValue = Character.getNumericValue(ch);

            if (i % 2 == 0) {
                digitValue *= 2;

                if (digitValue > 9) {
                    digitValue -= 9;
                }
            }
            sum += digitValue;
        }
        int checkDigit = Character.getNumericValue(cardNumber.charAt(15));
        sum += checkDigit;

        return sum % 10 == 0;
    }

    public boolean checkRegion(String region) {
        try {
            Region.valueOf(region);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

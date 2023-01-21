package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;

public class Main {

    private static final int OUTPUT_SCALE = 5;

    private static final String[] MESSAGES_STAGE3 = {
            "Enter two numbers in format: {source base} {target base} (To quit type /exit) ",
            "Enter number in base ",
            " to convert to base ",
            " (To go back type /back) ",
            "Conversion result: ",
    };

    private static final String[] COMMAND = {
            "/back",
            "/exit"
    };

    public static void main(String[] args) {
        // write your code here

        Scanner scanner = new Scanner(System.in);
        BigDecimal decimalNumber;
        int sourceBase = 0;
        int targetBase = 0;
        String sourceNumber;
        StringBuilder userCommand = new StringBuilder();
        String userInput;
        StringBuilder secondLine = new StringBuilder();

        do {

            System.out.print(MESSAGES_STAGE3[0]);
            if (scanner.hasNextInt()) {
                sourceBase = scanner.nextInt();
                targetBase = scanner.nextInt();
            } else {
                userCommand.setLength(0);
                userCommand.append(scanner.nextLine());
            }
            secondLine.setLength(0);
            secondLine.append(MESSAGES_STAGE3[1]).append(sourceBase).append(MESSAGES_STAGE3[2]).append(targetBase).append(MESSAGES_STAGE3[3]);

            if (sourceBase != 0 && targetBase != 0 && !userCommand.toString().equals(COMMAND[1])) {
                do {
                    System.out.print(secondLine);
                    userCommand.setLength(0);

                    userInput = scanner.nextLine();
                    while (userInput.isEmpty()) {
                        userInput = scanner.nextLine();
                    }

                    userCommand.append(userInput);

                    if (userCommand.toString().equals(COMMAND[0])) {
                        break;
                    } else {
                        sourceNumber = userCommand.toString();
                    }

                    if (sourceBase == 10) {
                        decimalNumber = new BigDecimal(sourceNumber);
                        System.out.println(MESSAGES_STAGE3[4] + convertNumberToBase(decimalNumber, targetBase) + "\n");
                    } else if (sourceBase > 1 && sourceBase < 37 && targetBase > 1 && targetBase < 37) {
                        decimalNumber = convertNumberToDecimal(sourceNumber, sourceBase);
                        //System.out.println(decimalNumber);
                        if (sourceNumber.contains(".")) {
                            System.out.println(MESSAGES_STAGE3[4] + convertNumberToBase(decimalNumber, targetBase) + "\n");
                        } else {
                            System.out.println(MESSAGES_STAGE3[4] + convertNumberToBase(decimalNumber.setScale(0, RoundingMode.CEILING), targetBase) + "\n");
                        }
                    }

                } while (!userCommand.toString().equals(COMMAND[0]));
            }
        } while (!userCommand.toString().equals(COMMAND[1]));
    }

    private static StringBuilder convertNumberToBase(BigDecimal decimalNumber, int newBase) {
        StringBuilder resultIntegerPart = new StringBuilder();
        StringBuilder resultFractionPart = new StringBuilder();
        int currentFractionDigit;
        int scaleCounter = 0;

        BigInteger decimalIntegerPart = decimalNumber.toBigInteger();
        BigDecimal decimalIntegerPartAsBigDecimal = new BigDecimal(decimalIntegerPart);
        BigDecimal decimalFractionPart = decimalNumber.subtract(decimalIntegerPartAsBigDecimal);
        BigDecimal currentProduct;
        BigDecimal currentFractionRemainder;

        BigInteger[] quotientAndRemainder = {decimalIntegerPart, BigInteger.ZERO};
        char remainderAsChar;
        char currentFractionDigitAsChar;

        if (newBase < 11) {
            do {
                quotientAndRemainder = quotientAndRemainder[0].divideAndRemainder(BigInteger.valueOf(newBase));
                resultIntegerPart.insert(0, quotientAndRemainder[1]);
            } while (!quotientAndRemainder[0].equals(BigInteger.ZERO));
        } else if (newBase < 37) {
            do {
                quotientAndRemainder = quotientAndRemainder[0].divideAndRemainder(BigInteger.valueOf(newBase));
                remainderAsChar = convertRemainder(quotientAndRemainder[1].intValue());
                resultIntegerPart.insert(0, remainderAsChar);
            } while (!quotientAndRemainder[0].equals(BigInteger.ZERO));
        }

        currentFractionRemainder = decimalFractionPart;
        if (newBase < 11) {
            do {
                currentProduct = currentFractionRemainder.multiply(BigDecimal.valueOf(newBase));
                currentFractionDigit = currentProduct.intValue();
                currentFractionRemainder = currentProduct.subtract(BigDecimal.valueOf(currentFractionDigit));
                resultFractionPart.append(currentFractionDigit);
                scaleCounter++;
            } while (scaleCounter < OUTPUT_SCALE);
        } else if (newBase < 37) {
            do {
                currentProduct = currentFractionRemainder.multiply(BigDecimal.valueOf(newBase));
                currentFractionDigit = currentProduct.intValue();
                currentFractionRemainder = currentProduct.subtract(BigDecimal.valueOf(currentFractionDigit));
                currentFractionDigitAsChar = convertRemainder(currentFractionDigit);
                resultFractionPart.append(currentFractionDigitAsChar);
                scaleCounter++;
            } while (scaleCounter < OUTPUT_SCALE);
        }

        StringBuilder result = resultIntegerPart;
        if (!decimalFractionPart.equals(BigDecimal.ZERO)) {
            result.append('.').append(resultFractionPart);
        }

        return result;
    }


    private static BigDecimal convertNumberToDecimal(String sourceNumberAsString, int sourceBase) {

        char[] sourceIntegerPartDigits;
        char[] sourceFractionPart = null;

        if (sourceNumberAsString.contains(".")) {
            String[] numberParts = sourceNumberAsString.split("\\.");
            sourceIntegerPartDigits = numberParts[0].toCharArray();
            sourceFractionPart = numberParts[1].toCharArray();
        } else {
            sourceIntegerPartDigits = sourceNumberAsString.toCharArray();
        }


        BigDecimal multiplier = BigDecimal.ONE;
        BigDecimal resultIntegerPart = multiplier.multiply(BigDecimal.valueOf(convertCharDigitToDecimal(sourceIntegerPartDigits[sourceIntegerPartDigits.length - 1])));
        for (int i = sourceIntegerPartDigits.length - 2; i >= 0; i--) {
            multiplier = multiplier.multiply(BigDecimal.valueOf(sourceBase));
            resultIntegerPart = resultIntegerPart.add(multiplier.multiply(BigDecimal.valueOf(convertCharDigitToDecimal(sourceIntegerPartDigits[i]))));
        }

        BigDecimal resultFractionPart = new BigDecimal(0);
        resultFractionPart.setScale(10, RoundingMode.CEILING);
        int fractionDigitsCounter = 0;
        if (sourceFractionPart != null) {
            int currentDigit;
            BigDecimal divisor = new BigDecimal(sourceBase);
            while (fractionDigitsCounter < sourceFractionPart.length) {
                currentDigit = convertCharDigitToDecimal(sourceFractionPart[fractionDigitsCounter]);
                resultFractionPart = resultFractionPart.add(BigDecimal.valueOf(currentDigit).divide(divisor, 10, RoundingMode.CEILING));
                divisor = divisor.multiply(BigDecimal.valueOf(sourceBase));
                fractionDigitsCounter++;
            }
        }

        return resultIntegerPart.add(resultFractionPart).setScale(10, RoundingMode.CEILING);
    }

    private static char convertRemainder(int remainder) {
        char result = '@';
        if (remainder < 10) {
            result = (char) ('0' + remainder);
        } else if (remainder < 37) {
            result = (char) (remainder - 10 + 'A');
        }

        return result;
    }

    private static int convertCharDigitToDecimal(char charDigit) {
        int result = 0;
        if (charDigit >= '0' && charDigit <= '9') {
            result = charDigit - '0';
        } else if (charDigit >= 'A' && charDigit <= 'Z') {
            result = charDigit - 'A' + 10;
        } else if (charDigit >= 'a' && charDigit <= 'z') {
            result = charDigit - 'a' + 10;
        }
        return result;
    }

}

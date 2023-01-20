package converter;

import java.math.BigInteger;
import java.util.Scanner;

public class Main {

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
        BigInteger decimalNumber;
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
                        decimalNumber = new BigInteger(sourceNumber);
                        System.out.println(MESSAGES_STAGE3[4] + convertNumberToBase(decimalNumber, targetBase) + "\n");
                    } else if (sourceBase > 1 && sourceBase < 37 && targetBase > 1 && targetBase < 37) {
                        decimalNumber = convertNumberToDecimal(sourceNumber, sourceBase);
                        //System.out.println(decimalNumber);
                        System.out.println(MESSAGES_STAGE3[4] + convertNumberToBase(decimalNumber, targetBase) + "\n");
                    }

                } while (!userCommand.toString().equals(COMMAND[0]));
            }
        } while (!userCommand.toString().equals(COMMAND[1]));
    }

    private static StringBuilder convertNumberToBase(BigInteger decimalNumber, int newBase) {
        StringBuilder result = new StringBuilder();
        BigInteger[] quotientAndRemainder = {decimalNumber, BigInteger.ZERO};
        char remainderAsChar;
        if (newBase < 11) {
            do {
                quotientAndRemainder = quotientAndRemainder[0].divideAndRemainder(BigInteger.valueOf(newBase));
                result.insert(0, quotientAndRemainder[1]);
            } while (!quotientAndRemainder[0].equals(BigInteger.ZERO));
        } else if (newBase < 37) {
            do {
                quotientAndRemainder = quotientAndRemainder[0].divideAndRemainder(BigInteger.valueOf(newBase));
                remainderAsChar = convertRemainder(quotientAndRemainder[1].intValue());
                result.insert(0, remainderAsChar);
            } while (!quotientAndRemainder[0].equals(BigInteger.ZERO));
        }
        return result;
    }

    private static BigInteger convertNumberToDecimal(String sourceNumber, int sourceBase) {
        BigInteger multiplier = BigInteger.ONE;
        char[] sourceDigits = sourceNumber.toCharArray();
        BigInteger result = multiplier.multiply(BigInteger.valueOf(convertCharDigitToDecimal(sourceDigits[sourceDigits.length - 1])));
        for (int i = sourceDigits.length - 2; i >= 0; i--) {
            multiplier = multiplier.multiply(BigInteger.valueOf(sourceBase));
            result = result.add(multiplier.multiply(BigInteger.valueOf(convertCharDigitToDecimal(sourceDigits[i]))));
        }
        return result;
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

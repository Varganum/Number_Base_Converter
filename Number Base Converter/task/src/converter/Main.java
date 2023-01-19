package converter;

import java.util.Scanner;

public class Main {

    private static final String[] MESSAGES = {
            "Do you want to convert ",
            " decimal or ",
            " decimal? (To quit type ",
            "Enter number in decimal system: ",
            "Enter target base: ",
            "Conversion result: ",
            "Enter source number: ",
            "Enter source base: ",
            "Conversion to decimal result: "
    };

    private static final String[] COMMAND = {
            "/from",
            "/to",
            "/exit"
    };

    public static void main(String[] args) {
        // write your code here
        String startLine = MESSAGES[0] + COMMAND[0] + MESSAGES[1] + COMMAND[1] + MESSAGES[2] + COMMAND[2] + ") ";
        Scanner scanner = new Scanner(System.in);
        int decimalNumber;
        int newBase;
        String sourceNumber;
        int sourceBase;
        StringBuilder userCommand = new StringBuilder();
        System.out.print(startLine);

        do {
            userCommand.delete(0, userCommand.length());
            userCommand.append(scanner.nextLine());

            if (userCommand.toString().equals(COMMAND[0])) {
                System.out.print(MESSAGES[3]);
                decimalNumber = scanner.nextInt();
                System.out.print(MESSAGES[4]);
                newBase = scanner.nextInt();
                System.out.println(MESSAGES[5] + convertNumberToBase(decimalNumber, newBase) + "\n");
                System.out.print(startLine);
            } else if (userCommand.toString().equals(COMMAND[1])) {
                System.out.print(MESSAGES[6]);
                sourceNumber = scanner.nextLine();
                System.out.print(MESSAGES[7]);
                sourceBase = scanner.nextInt();
                System.out.println(MESSAGES[8] + convertNumberToDecimal(sourceNumber, sourceBase) + "\n");
                System.out.print(startLine);
            }
        } while (!userCommand.toString().equals(COMMAND[2]));

    }

    private static StringBuilder convertNumberToBase(int decimalNumber, int newBase) {
        StringBuilder result = new StringBuilder();
        int currentQuotient = decimalNumber;
        int remainder;
        char remainderAsChar;
        if (newBase < 11) {
            do {
                remainder = currentQuotient % newBase;
                currentQuotient /= newBase;
                result.insert(0, remainder);
            } while (currentQuotient != 0);
        } else if (newBase < 17) {
            do {
                remainder = currentQuotient % newBase;
                remainderAsChar = convertRemainder(remainder);
                currentQuotient /= newBase;
                result.insert(0, remainderAsChar);
            } while (currentQuotient != 0);
        }
        return result;
    }

    private static int convertNumberToDecimal(String sourceNumber, int sourceBase) {
        int multiplier = 1;
        char[] sourceDigits = sourceNumber.toCharArray();
        int result = multiplier * convertCharDigitToDecimal(sourceDigits[sourceDigits.length - 1]);
        for (int i = sourceDigits.length - 2; i >= 0; i--) {
            multiplier *= sourceBase;
            result += convertCharDigitToDecimal(sourceDigits[i]) * multiplier;
        }
        return result;
    }

    private static char convertRemainder(int remainder) {
        switch (remainder) {
            case 0 -> {return '0';}
            case 1 -> {return '1';}
            case 2 -> {return '2';}
            case 3 -> {return '3';}
            case 4 -> {return '4';}
            case 5 -> {return '5';}
            case 6 -> {return '6';}
            case 7 -> {return '7';}
            case 8 -> {return '8';}
            case 9 -> {return '9';}
            case 10 -> {return 'A';}
            case 11 -> {return 'B';}
            case 12 -> {return 'C';}
            case 13 -> {return 'D';}
            case 14 -> {return 'E';}
            case 15 -> {return 'F';}
            default -> {return 'X';}
        }
    }

    private static int convertCharDigitToDecimal(char charDigit) {
        int result = 0;
        if (charDigit >= '0' && charDigit <= '9') {
            result = charDigit - '0';
        } else if (charDigit >= 'A' && charDigit <= 'F') {
            result = charDigit - 'A' + 10;
        } else if (charDigit >= 'a' && charDigit <= 'f') {
            result = charDigit - 'a' + 10;
        }
        return result;
    }

}

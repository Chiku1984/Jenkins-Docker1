package dataUtil;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class RandomDataGen {
	static Random random = new Random();

	@Step("Generating a single Random Number between {0} and {1}")
	@Attachment
	public static int SingleNumber_BetBounds(int boundStart, int boundEnd) {
		return (((int) (Math.random() * (boundEnd - boundStart))) + boundStart);
	}

	@Step("Generating a Random number sequence of length - {0}")
	@Attachment
	public static String NumberSequence(int numberSeqLength) {
		String leftTemp = "1";
		String RightTemp = "9";
		for (int i = 1; i < numberSeqLength; i++) {
			leftTemp = leftTemp + "0";
			RightTemp = RightTemp + "9";
		}
		long generatedInteger = Long.parseLong(leftTemp)
				+ (long) (new Random().nextFloat() * (Long.parseLong(RightTemp) - (Long.parseLong(leftTemp))));
		String strReturnVal = String.valueOf(generatedInteger);
		return strReturnVal;

	}

	@Step("Generating a Number Sequence, the lenght of number Sequence is a Random length between {0} and {1}")
	@Attachment
	public static String NumberSequence_BetBounds(int boundStart, int boundEnd) {
		return NumberSequence(SingleNumber_BetBounds(boundStart, boundEnd));
	}

	@Step("Generating a Date Value, if Offset value is >0 then the System Date will be offset by {0} days")
	@Attachment
	public static String generateDate(int offsetDays) {
		LocalDateTime systemDate = null;
		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyyMMdd");
		if (offsetDays == 0) {
			systemDate = LocalDateTime.now();

		} else {
			systemDate = LocalDateTime.now().plusDays(offsetDays);
		}
		return systemDate.format(date);
	}

	@Step("Generating a Alphanumeric string Sequence of lenght {0}")
	@Attachment
	public static String AlphaNumericString_Sequence(int randomStrLen) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "1234567890" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(randomStrLen);
		for (int i = 0; i < randomStrLen; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	@Step("Generating a Alphanumeric string Sequence, the lenght of String is a Random length between {0} and {1}")
	@Attachment
	public static String AlphaNumericString_Sequence_BetBounds(int boundStart, int boundEnd) {
		return AlphaNumericString_Sequence(SingleNumber_BetBounds(boundStart, boundEnd));
	}

	@Step("Generating a Random Boolean Value")
	@Attachment
	public static boolean generate_Boolean_Flag() {
		random = new Random();
		return random.nextBoolean();
	}
	@Step("Generating a Alphabetic string Sequence of lenght {0}")
	@Attachment
	public static String String_Sequence(int randomStrLen)
	{
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(randomStrLen);
		for (int i = 0; i < randomStrLen; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
		
		
	}
	
	@Step("Generating a Alpha string Sequence, the lenght of String is a Random length between {0} and {1}")
	@Attachment
	public static String AlphaString_Sequence_BetBounds(int boundStart, int boundEnd) {
		return String_Sequence(SingleNumber_BetBounds(boundStart, boundEnd));
	}
}

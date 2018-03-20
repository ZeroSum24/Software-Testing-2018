package st;

public class SimpleTemplateEngine {

    private static final Character TEMPLATE_SUFFIX = '#';

    public static final Integer CASE_SENSITIVE = 1;
    public static final Integer WHOLE_WORLD_SEARCH = 2;
    public static final Integer DEFAULT_MATCH = 0;


    public String evaluate(String originalText, String formatedPattern, String value, Integer matchingMode) {
        if (!isTextValid(originalText) | !isTextValid(value) |! isTextValid(formatedPattern)) {
            return originalText;
        }

        if (!isMatchingModeValid(matchingMode)) {
            matchingMode = DEFAULT_MATCH;
        }

        String pattern = "";
        Integer number = 0;

        Integer charIndex = 0;
        Character currentChar;
        Boolean suffixFlag = Boolean.FALSE;
        Boolean numberFound = Boolean.FALSE;
        Integer numberStart = 0;
        Integer numberEnd = 0;
        while (charIndex < formatedPattern.length()) {
            currentChar = formatedPattern.charAt(charIndex);
            if (currentChar.equals(TEMPLATE_SUFFIX) && suffixFlag != Boolean.TRUE) {
                suffixFlag = Boolean.TRUE;
                charIndex++;
                continue;
            }
            if (suffixFlag == Boolean.TRUE) {
                if (currentChar.equals(TEMPLATE_SUFFIX)) {
                    suffixFlag = Boolean.FALSE;
                    charIndex++;
                    pattern = pattern + currentChar;
                    continue;
                } else if (Character.isDigit(currentChar)) {
                    numberStart = charIndex;
                    numberEnd = charIndex + 1;
                    numberFound = Boolean.TRUE;
                    suffixFlag = Boolean.FALSE;
                    while (numberEnd < formatedPattern.length()) {
                        currentChar = formatedPattern.charAt(numberEnd);
                        if (Character.isDigit(currentChar)) {
                            numberEnd++;
                        } else {
                            break;
                        }
                    }
                    break;
                } else {
                    return originalText;
                }
            }
            charIndex++;
            pattern = pattern + currentChar;
        }
        if (!isTextValid(pattern)) {
            return originalText;
        }

        if (suffixFlag == Boolean.TRUE) {
            return originalText;
        }

        if (numberFound) {
            number = Integer.valueOf(formatedPattern.substring(numberStart, numberEnd));
        }

        if (!caseSensative(matchingMode)) {
            pattern = pattern.toLowerCase();
        }

        charIndex = 0;
        Integer patternLength = pattern.length();
        Integer numMatched = 0;
        while (charIndex <= originalText.length() - patternLength) {
            String subString = originalText.substring(charIndex, charIndex + patternLength);
            if (!caseSensative(matchingMode)) {
                subString = subString.toLowerCase();
            }

            if (subString.equals(pattern)) {
                if (wholeWordSearch(matchingMode)) {
                    if (charIndex + patternLength < originalText.length() && Character.isLetterOrDigit(originalText.charAt(charIndex + patternLength))) {
                        charIndex++;
                        continue;
                    }
                    if (charIndex!=0 && Character.isLetterOrDigit(originalText.charAt(charIndex-1))) {
                        charIndex++;
                        continue;
                    }
                }

                numMatched++;
                if (numberFound && numMatched == number || !numberFound) {
                    String leftPart = originalText.substring(0, charIndex);
                    String rightPart = "";
                    if (charIndex + patternLength < originalText.length()) {
                        rightPart = originalText.substring(charIndex + patternLength, originalText.length());
                    }
                    originalText = leftPart + value + rightPart;
                    charIndex = charIndex + value.length();
                    continue;
                }
            }
            charIndex++;
        }

        return originalText;
    }

    private Boolean isTextValid(String text) {
        if (text == null) {
            return Boolean.FALSE;
        }
        if (text.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private Boolean isMatchingModeValid(Integer matchingMode){
        if (matchingMode == null) {
            return Boolean.FALSE;
        }
        if (matchingMode < 0){
            return Boolean.FALSE;
        }
        if (matchingMode > 3){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private Boolean caseSensative(Integer matchingMode) {
        if ((matchingMode & CASE_SENSITIVE) == CASE_SENSITIVE) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    private Boolean wholeWordSearch(Integer matchingMode) {
        if ((matchingMode & WHOLE_WORLD_SEARCH) == WHOLE_WORLD_SEARCH) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}

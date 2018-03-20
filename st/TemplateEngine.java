package st;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Stack;

public class TemplateEngine
{

    public static final Integer DELETE_UNMATCHED = 1;
    public static final Integer KEEP_UNMATCHED = 0;
    public static final Integer CASE_SENSITIVE = 2;
    public static final Integer CASE_INSENSITIVE = 0;
    public static final Integer BLUR_SEARCH = 4;
    public static final Integer ACCURATE_SEARCH = 0;
    public static final Integer DEFAULT = 0;

    private static final Character TEMPLATE_START_PREFIX = '$';
    private static final Character TEMPLATE_START = '{';
    private static final Character TEMPLATE_END = '}';

    private static final String YEAR_TEMPLATE = "year";
    private static final String BASE_YEAR_TEMPLATE = "base_year";


    public TemplateEngine()
    {

    }

    public String evaluate(String templateString, EntryMap entryMap, Integer matchingMode)
    {
        if (!isEvaluationPossible(templateString, entryMap))
        {
            return templateString;
        }

        if (!isMatchingModeValid(matchingMode))
        {
            matchingMode = Integer.valueOf(0);
        }

        HashSet<Template> templates = identifyTemplates(templateString);

        ArrayList<Template> sortedTemplates = sortTemplates(templates);

        Result result = instantiate(templateString, sortedTemplates, entryMap.getEntries(), matchingMode);

        return result.getInstancedString();
    }

    private Boolean isEvaluationPossible(String templateString, EntryMap entryMap)
    {
        if (templateString == null)
        {
            return Boolean.FALSE;
        }
        if (templateString.isEmpty())
        {
            return Boolean.FALSE;
        }
        if (entryMap == null)
        {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private Boolean isMatchingModeValid(Integer matchingMode)
    {
        if (matchingMode == null)
        {
            return Boolean.FALSE;
        }

        if (matchingMode < 0)
        {
            return Boolean.FALSE;
        }
        if (matchingMode > 7)
        {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private Boolean keepUnmatched(Integer matchingMode)
    {
        if ((matchingMode & DELETE_UNMATCHED) == DELETE_UNMATCHED)
        {
            return Boolean.FALSE;
        } else
        {
            return Boolean.TRUE;
        }
    }

    private Boolean caseInsensative(Integer matchingMode)
    {
        if ((matchingMode & CASE_SENSITIVE) == CASE_SENSITIVE)
        {
            return Boolean.FALSE;
        } else
        {
            return Boolean.TRUE;
        }
    }

    private Boolean accurateSearch(Integer matchingMode)
    {
        if ((matchingMode & BLUR_SEARCH) == BLUR_SEARCH)
        {
            return Boolean.FALSE;
        } else
        {
            return Boolean.TRUE;
        }
    }

    private HashSet<Template> identifyTemplates(String templateString)
    {
        HashSet<Template> templates = new HashSet<>();
        Stack<Integer> templateCandidates = new Stack<>();
        Integer charIndex = 0;
        Boolean underSequence = Boolean.FALSE;
        while (charIndex < templateString.length())
        {
            if (Character.compare(templateString.charAt(charIndex), TEMPLATE_START_PREFIX) == 0)
            {
                underSequence = Boolean.TRUE;
                charIndex++;
                continue;
            }
            if (Character.compare(templateString.charAt(charIndex), TEMPLATE_START) == 0)
            {
                if (underSequence)
                {
                    templateCandidates.add(charIndex);
                }
                underSequence = Boolean.FALSE;
                charIndex++;
                continue;
            }
            if (Character.compare(templateString.charAt(charIndex), TEMPLATE_END) == 0)
            {
                if (!templateCandidates.isEmpty())
                {
                    Template template;
                    Integer startIndex = templateCandidates.pop();
                    if ((startIndex + 1) == charIndex)
                    {
                        template = new Template(startIndex, charIndex, "");
                    } else
                    {
                        template = new Template(startIndex, charIndex, templateString.substring(startIndex + 1, charIndex));
                    }
                    templates.add(template);
                }
                underSequence = Boolean.FALSE;
                charIndex++;
                continue;
            }
            underSequence = Boolean.FALSE;
            charIndex++;
        }
        return templates;
    }

    private ArrayList<Template> sortTemplates(HashSet<Template> templates)
    {
        ArrayList<Template> sortedTemplates = new ArrayList<>();
        Template currentTemplate;
        Integer minLength;
        Integer startIndex;
        while (!templates.isEmpty())
        {
            currentTemplate = null;
            minLength = Integer.MAX_VALUE;
            startIndex = Integer.MAX_VALUE;
            for (Template current : templates)
            {
                if (current.getContent().length() < minLength)
                {
                    currentTemplate = current;
                    minLength = current.getContent().length();
                    startIndex = current.getStartIndex();
                } else
                {
                    if (current.getContent().length() == minLength)
                    {
                        if (current.getStartIndex() < startIndex)
                        {
                            currentTemplate = current;
                            minLength = current.getContent().length();
                            startIndex = current.getStartIndex();
                        }
                    }
                }
            }
            if (currentTemplate != null)
            {
                templates.remove(currentTemplate);
                sortedTemplates.add(currentTemplate);
            } else
            {
                throw new RuntimeException();
            }
        }
        return sortedTemplates;
    }

    private Result instantiate(String instancedString, ArrayList<Template> sortedTemplates, ArrayList<EntryMap.Entry> sortedEntries, Integer matchingMode)
    {
        Integer templatesReplaced = 0;
        Boolean replaceHappened;
        Template currentTemplate;
        EntryMap.Entry currentEntry;
        for (Integer i = 0; i < sortedTemplates.size(); i++)
        {
            currentTemplate = sortedTemplates.get(i);
            replaceHappened = Boolean.FALSE;
            for (Integer j = 0; j < sortedEntries.size(); j++)
            {
                currentEntry = sortedEntries.get(j);
                if (isAMatch(currentTemplate, currentEntry, matchingMode))
                {

                    String entryValue = currentEntry.getValue();

                    if (isAMatchString(currentTemplate.getContent(), YEAR_TEMPLATE, matchingMode))
                    {
                        entryValue = getYearEntryValue(entryValue, sortedEntries, matchingMode);
                    }

                    instancedString = doReplace(instancedString, currentTemplate, i, entryValue, sortedTemplates);
                    replaceHappened = Boolean.TRUE;
                    break;
                }
            }
            if (replaceHappened)
            {
                templatesReplaced++;
            } else
            {
                if (!keepUnmatched(matchingMode))
                {
                    instancedString = doReplace(instancedString, currentTemplate, i, "", sortedTemplates);
                }
            }
        }
        return new Result(instancedString, templatesReplaced);
    }

    private int getYearsAgoInt(String entryValue)
    {
        String yearsAgo = " years ago";

        if (entryValue.endsWith(yearsAgo))
        {
            String num = entryValue.substring(0, entryValue.indexOf(yearsAgo));
            return isValidNonNegativeInt(num) ? Integer.valueOf(num) : -1;
        } else
        {
            return -1;
        }
    }


    private int getInXYearsInt(String entryValue)
    {
        String in = "in ";
        String years = " years";

        if (entryValue.startsWith(in) && entryValue.endsWith(years))
        {
            String num = entryValue.substring(entryValue.indexOf(in) + in.length(), entryValue.indexOf(years));
            return  isValidNonNegativeInt(num) ? Integer.valueOf(num) : -1;
        } else
        {
            return -1;
        }
    }

    private int getBaseYear(ArrayList<EntryMap.Entry> entries, Integer matchingMode)
    {
        EntryMap.Entry baseYearEntry = null;

        for(EntryMap.Entry e : entries)
        {
            if(isAMatchString(e.pattern, BASE_YEAR_TEMPLATE, matchingMode))
            {
                baseYearEntry = e;
            }
        }

        if(baseYearEntry != null && isValidNonNegativeInt(baseYearEntry.value))
        {
            return Integer.valueOf(baseYearEntry.value);
        }
        else
        {
            return Calendar.getInstance().get(Calendar.YEAR);
        }
    }

    private String getYearEntryValue(String entryValue, ArrayList<EntryMap.Entry> entries, Integer matchingMode)
    {
        int inXYearsInt = getInXYearsInt(entryValue);
        int yearsAgoInt = getYearsAgoInt(entryValue);
        int baseYear = getBaseYear(entries, matchingMode);

        if (yearsAgoInt != -1)
        {
            int targetYear = baseYear - yearsAgoInt;
            return String.valueOf(targetYear);
        }
        else if(inXYearsInt != -1)
        {
            int targetYear = baseYear + inXYearsInt;
            return String.valueOf(targetYear);
        }
        else
        {
            return entryValue;
        }
    }

    private boolean isValidNonNegativeInt(String s)
    {
        try
        {
            int i = Integer.valueOf(s);
            return i >= 0;
        } catch (NumberFormatException | NullPointerException e)
        {
            return false;
        }
    }

    private Boolean isAMatchString(String leftHandSide, String rightHandSide, Integer matchingMode)
    {
        if (!accurateSearch(matchingMode))
        {
            leftHandSide = leftHandSide.replaceAll("\\s", "");
            rightHandSide = rightHandSide.replaceAll("\\s", "");
        }

        if (caseInsensative(matchingMode))
        {
            return leftHandSide.toLowerCase().equals(rightHandSide.toLowerCase());
        } else
        {
            return leftHandSide.equals(rightHandSide);
        }
    }

    private Boolean isAMatch(Template template, EntryMap.Entry entry, Integer matchingMode)
    {
        String leftHandSide;
        String rightHandSide;
        if (!accurateSearch(matchingMode))
        {
            leftHandSide = template.getContent().replaceAll("\\s", "");
            rightHandSide = entry.getPattern().replaceAll("\\s", "");
        } else
        {
            leftHandSide = template.getContent();
            rightHandSide = entry.getPattern();
        }
        if (caseInsensative(matchingMode))
        {
            return leftHandSide.toLowerCase().equals(rightHandSide.toLowerCase());
        } else
        {
            return leftHandSide.equals(rightHandSide);
        }
    }

    private String doReplace(String instancedString, Template currentTemplate, Integer currentTemplateIndex, String replaceValue, ArrayList<Template> sortedTemplates)
    {
        Integer diff = 3 + currentTemplate.getContent().length() - replaceValue.length();
        String firstHalf;
        String secondHalf;
        if (currentTemplate.getStartIndex() == 1)
        {
            firstHalf = "";
        } else
        {
            firstHalf = instancedString.substring(0, currentTemplate.getStartIndex() - 1);
        }
        if (currentTemplate.getEndIndex() == instancedString.length())
        {
            secondHalf = "";
        } else
        {
            secondHalf = instancedString.substring(currentTemplate.getEndIndex() + 1);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(firstHalf);
        builder.append(replaceValue);
        builder.append(secondHalf);
        String updatedInstancedString = builder.toString();

        Template temp = null;
        for (int i = currentTemplateIndex + 1; i < sortedTemplates.size(); i++)
        {
            temp = sortedTemplates.get(i);
            if ((temp.getStartIndex() < currentTemplate.getStartIndex()) && (temp.getEndIndex() > currentTemplate.getEndIndex()))
            {
                sortedTemplates.get(i).setEndIndex(temp.getEndIndex() - diff);
                sortedTemplates.get(i).setContent(updatedInstancedString.substring(sortedTemplates.get(i).getStartIndex() + 1, sortedTemplates.get(i).getEndIndex()));
            } else
            {
                if (temp.getStartIndex() > currentTemplate.getEndIndex())
                {
                    sortedTemplates.get(i).setStartIndex(temp.getStartIndex() - diff);
                    sortedTemplates.get(i).setEndIndex(temp.getEndIndex() - diff);
                }
            }
        }
        return updatedInstancedString;
    }

    class Template
    {
        Integer startIndex;
        Integer endIndex;
        String content;

        Template(Integer startIndex, Integer endIndex, String content)
        {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.content = content;
        }

        public Integer getStartIndex()
        {
            return startIndex;
        }

        public Integer getEndIndex()
        {
            return endIndex;
        }

        public String getContent()
        {
            return content;
        }

        public void setStartIndex(Integer startIndex)
        {
            this.startIndex = startIndex;
        }

        public void setEndIndex(Integer endIndex)
        {
            this.endIndex = endIndex;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Template template = (Template) o;

            if (getStartIndex() != null ? !getStartIndex().equals(template.getStartIndex()) : template.getStartIndex() != null)
                return false;
            if (getEndIndex() != null ? !getEndIndex().equals(template.getEndIndex()) : template.getEndIndex() != null)
                return false;
            return getContent() != null ? getContent().equals(template.getContent()) : template.getContent() == null;
        }

        @Override
        public int hashCode()
        {
            int result = getStartIndex() != null ? getStartIndex().hashCode() : 0;
            result = 31 * result + (getEndIndex() != null ? getEndIndex().hashCode() : 0);
            result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
            return result;
        }
    }

    class Result
    {
        String instancedString;
        Integer templatesReplaced;

        Result(String instancedString, Integer templatesReplaced)
        {
            this.instancedString = instancedString;
            this.templatesReplaced = templatesReplaced;
        }

        String getInstancedString()
        {
            return instancedString;
        }

        Integer getTemplatesReplaced()
        {
            return templatesReplaced;
        }
    }
}

package com.theopus.parser.obj;

import com.theopus.parser.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by Oleksandr_Tkachov on 17.12.2017.
 */
public class Patterns {


    public static final class Sheet {
        public static final String DAY_OF_WEEK_SPLITTER = ("(понедiлок)|(Вiвторок)|(Середа)|(Четвер)|(П'ятниця)|(Субота)|(Неділя)");
        public static final String DAY_OF_WEEK_Pattern = (DAY_OF_WEEK_SPLITTER + "(.{50,200})" + DAY_OF_WEEK_SPLITTER);
        public static final String TABLE_BOUND = ("===");
    }

    public static final class LessonLine {
        public static final String LAB = "\\(лаб.*\\)";
        public static final String PRACT = "\\(пра.*\\)";
        public static final String LECTURE = "\\(лек.*\\)";
        public static final String EXAM = "";
        public static final String CONSULTATION = "";
        public static final String TEACHER = "\\b((([^.,\\s\\d\\p{Punct}]{2,5}.)?" +
                "[^.,\\s\\d\\p{Punct}]{2,4}\\.)|[^.,\\d\\s]{3,}\\.)\\s[^.,\\s\\d]+(\\s[^.,\\d\\s]\\.)?" +
                "([^.,\\d\\s]\\.?)?";
        public static final String GROUP = "";
        public static final String BEGIN_LESSON_SPLITTER = ("((\\d?\\d:\\d\\d)|(--\"--))");
        public static final String END_LESSON_SPLITTER = ("((\\d?\\d:\\d\\d)|(--\"--)|(-{4,5}))");
    }

    public static final class RoomDates {
        public static final String BRACKETS = ("\\[([^]]+)\\]");
        public static final String AUDITORY = ("ауд\\.([\\wА-я<>]+)");
        public static final String SINGLE_DATE = ("(^|([^доз]\\s))(\\d?\\d\\.\\d\\d)");
        public static final String FROM_DATE = ("з\\s(\\d?\\d\\.\\d\\d)");
        public static final String TO_DATE = ("до\\s(\\d?\\d\\.\\d\\d)");


    }
}
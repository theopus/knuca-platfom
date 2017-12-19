package com.theopus.parser.obj.sheets;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.parser.StringUtils;
import com.theopus.parser.exceptions.IllegalPDFFormatException;
import com.theopus.parser.obj.Patterns;
import com.theopus.parser.obj.table.Table;
import javafx.util.Pair;

import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents view of full sheet with T anchor type.
 *
 * @param <T> anchor type.
 */

public class Sheet<T> {

    private Table table;
    private String content;
    private T anchor;

    private FileSheet<T> parent;
    private DaySheet<T> child;

    private Pattern dayOfWeekSplitter;
    private String tableBound;

    public Table getTable() {
        return table;
    }

    public List<Curriculum> parse() {
        return splitToDays().entrySet().stream().map(dc -> {
            child.prepare(dc.getKey(), dc.getValue());
            return child.parse();
        }).reduce((c1, c2) -> {
            c1.addAll(c2);
            return c1;
        }).orElseThrow(() -> new IllegalPDFFormatException("Daysheet"));

    }

    Map<DayOfWeek, String> splitToDays() {
        Matcher matcher = dayOfWeekSplitter.matcher(content);
        matcher.region(content.lastIndexOf(tableBound), content.length() - 1);
        Map<DayOfWeek, Pair<Integer, Integer>> map = new LinkedHashMap<>();
        while (matcher.find()) {
            map.put(StringUtils.converUkrDayOfWeek(matcher.group()), new Pair<>(matcher.start(), matcher.end()));
        }
        map.put(null, new Pair<>(content.length(), content.length()));
        Map<DayOfWeek, String> result = new LinkedHashMap<>();
        map.entrySet().stream().reduce((d1, d2) -> {
            result.put(d1.getKey(), StringUtils
                    .trimWhitespaces(content.substring(d1.getValue().getValue(), d2.getValue().getKey())
                    .replaceAll("---", "")));
            return d2;
        });
        return result;
    }

    public T getAnchor() {
        return anchor;
    }

    public Sheet<T> child(DaySheet<T> daySheet) {
        this.child = daySheet;
        return this;
    }

    public Sheet<T> anchor(T anchor) {
        this.anchor = anchor;
        return this;
    }

    public Sheet<T> parent(FileSheet<T> fileSheet) {
        this.parent = fileSheet;
        return this;
    }

    public static <T> Sheet<T>.Builder create() {
        return new Sheet<T>().new Builder();
    }

    public Sheet<T> prepare(String content) {
        this.content = content;
        return this;
    }

    public class Builder {

        public Sheet<T>.Builder defaultPatterns() {
            Sheet.this.dayOfWeekSplitter = Pattern.compile(Patterns.Sheet.DAY_OF_WEEK_SPLITTER,
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.MULTILINE);
            return this;
        }

        public Sheet<T>.Builder deafultTableBound() {
            Sheet.this.tableBound = Patterns.Sheet.TABLE_BOUND;
            return this;
        }

        public Sheet<T> build() {
            return Sheet.this;
        }
    }


}

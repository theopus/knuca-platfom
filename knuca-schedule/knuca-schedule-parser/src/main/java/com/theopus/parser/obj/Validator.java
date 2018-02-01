package com.theopus.parser.obj;

import com.google.common.collect.Sets;
import com.theopus.entity.schedule.Curriculum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Validator {

    private static final Logger LOG = LoggerFactory.getLogger(Validator.class);
    private int errorCount;
    private Sheet parent;

    public Validator() {
    }

    public List<Curriculum> validate(List<Curriculum> curriculums) {
        Table table = parent.getTable();
        table.getScheduleMap().forEach((localDate, tableEntries) -> {
            Set<Table.TableEntry> result = curriculumAtDate(curriculums, localDate);
            if (!Sets.symmetricDifference(result, tableEntries).isEmpty()) {
                LOG.error(
                        "{} \n " +
                                "{} \n " +
                                "{} \n " +
                                "{} \n " +
                                "{} \n " +
                                "{} \n",
                        localDate,
                        tableEntries,
                        result,
                        curriculums,
                        table.getDaysMap(),
                        table.getParent().getContent()
                );
                errorCount++;
            }
        });
        return curriculums;
    }

    public Set<Table.TableEntry> curriculumAtDate(List<Curriculum> curriculumList, LocalDate localDate) {
        return curriculumList.stream().flatMap(curic ->
                curic.getCircumstances().stream()
                        .filter(circ -> circ.getDates().contains(localDate))
                        .map(circumstance1 -> new Table.TableEntry(circumstance1.getLessonOrder(), curic.getCourse().getType()))
        ).collect(Collectors.toSet());
    }

    public int getErrorCount() {
        return errorCount;
    }

    public <T> Validator parent(Sheet sheet) {
        this.parent = sheet;
        this.errorCount = 0;
        return this;
    }

    public float hitCount() {
        return 100 - getErrorCount() / ((parent.getParent().getPosition() + 1) / 100f);
    }
}

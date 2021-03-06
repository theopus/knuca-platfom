package com.theopus.repository.specification;

import com.theopus.entity.schedule.Subject;
import org.springframework.data.jpa.domain.Specification;

public class SubjectSpecification {

    public static Specification<Subject> getByName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }
}

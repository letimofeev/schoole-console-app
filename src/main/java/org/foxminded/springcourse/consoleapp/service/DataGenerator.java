package org.foxminded.springcourse.consoleapp.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataGenerator {

    public List<Group> generateRandomGroups(int count) {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String letters = RandomStringUtils.randomAlphabetic(2);
            String digits = RandomStringUtils.randomNumeric(2);
            String groupName = String.format("%s_%s", letters, digits);
            Group group = new Group();
            group.setGroupName(groupName);
            groups.add(group);
        }
        return groups;
    }

    public List<Course> generateCourses() {
        return List.of(new Course("math", "math_desc"),
                new Course("meth", "meth_desc"),
                new Course("biology", "biology_desc"),
                new Course("informatics", "informatics_desc"),
                new Course("computer_science", "computer_science_desc"),
                new Course("memelogy", "memelogy_desc"),
                new Course("religion", "religion_desc"),
                new Course("geometry", "geometry_desc"),
                new Course("functional_analysis", "functional_analysis_desc"),
                new Course("machine_learning", "machine_learning_desc"));
    }

    public List<Student> generateRandomStudents(int count, List<Group> groups) {
        List<Student> students = new ArrayList<>();
        List<String> firstNames = getFirstNames();
        List<String> lastNames = getLastNames();
        for (int i = 0; i < count; i++) {
            Group group = getRandomElement(groups);
            String firstName = getRandomElement(firstNames);
            String lastName = getRandomElement(lastNames);
            Student student = new Student();
            student.setGroupId(group.getGroupId());
            student.setFirstName(firstName);
            student.setLastName(lastName);
            students.add(student);
        }
        return students;
    }

    public <T> T getRandomElement(List<T> elements) {
        int index = RandomUtils.nextInt(0, elements.size());
        return elements.get(index);
    }

    private List<String> getLastNames() {
        return List.of("Cavill", "Solace", "Levine", "Cromwell",
                "Martin", "West", "Madison", "Hope",
                "Gatlin", "Ford", "Adler", "Stoll",
                "Carter", "Gray", "Wilson", "Gosling",
                "Fleet", "Sharpe", "Gasper", "Lennon");
    }

    private List<String> getFirstNames() {
        return List.of("James", "George", "Eric", "Ryan",
                "Robert", "John", "Michael", "David",
                "William", "Richard", "Joseph", "Thomas",
                "Charles", "Christopher", "Daniel", "Matthew",
                "Anthony", "Henry", "Donald", "Steven");
    }
}

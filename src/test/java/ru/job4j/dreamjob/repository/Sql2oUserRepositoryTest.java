package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    public static final List<Map<String, String>> EMAIL_PASS = List.of(Map.of("olga@mail.ru", "olgaPass"),
            Map.of("olga@mail.ru", "olgaPass2"),
            Map.of("2olga@mail.ru", "olgaPass"),
            Map.of("3olga@mail.ru", "olgaPass")
    );

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @BeforeEach
    public void beforeClearUsers() {
        clearUsers();
    }

    @AfterEach
    public void afterClearUsers() {
        clearUsers();
    }

    private void clearUsers() {
        for (var user : EMAIL_PASS) {
            sql2oUserRepository.deleteByEmail(user.keySet().toArray()[0].toString());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var email = EMAIL_PASS.get(0).keySet().toArray()[0];
        var pass = EMAIL_PASS.get(0).get(email);
        var user = sql2oUserRepository.save(new User(3, email.toString(), "Ильина Ольга", pass));
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword()).get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user.get());
    }

    @Test
    public void whenSaveSeveralDiffEmailThenGetNeed() {
        var email = EMAIL_PASS.get(0).keySet().toArray()[0];
        var pass = EMAIL_PASS.get(0).get(email);
        var user = sql2oUserRepository.save(new User(1, email.toString(), "Ильина Ольга", pass));
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword()).get();
        var email2 = EMAIL_PASS.get(2).keySet().toArray()[0];
        var pass2 = EMAIL_PASS.get(2).get(email2);
        var user2 = sql2oUserRepository.save(new User(2, email2.toString(), "Ильина Ольга", pass2));
        var savedUser2 = sql2oUserRepository.findByEmailAndPassword(user2.get().getEmail(), user2.get().getPassword()).get();
        var email3 = EMAIL_PASS.get(3).keySet().toArray()[0];
        var pass3 = EMAIL_PASS.get(3).get(email3);
        var user3 = sql2oUserRepository.save(new User(3, email3.toString(), "Ильина Ольга", pass3));
        var savedUser3 = sql2oUserRepository.findByEmailAndPassword(user3.get().getEmail(), user3.get().getPassword()).get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user.get());
        assertThat(savedUser2).usingRecursiveComparison().isEqualTo(user2.get());
        assertThat(savedUser3).usingRecursiveComparison().isEqualTo(user3.get());
    }

    @Test
    public void whenSaveSameThenError() {
        var email1 = EMAIL_PASS.get(0).keySet().toArray()[0];
        var pass1 = EMAIL_PASS.get(0).get(email1);
        var user1 = sql2oUserRepository.save(new User(0, email1.toString(), "Ильина Ольга", pass1));
        var savedUser1 = sql2oUserRepository.findByEmailAndPassword(user1.get().getEmail(), user1.get().getPassword()).get();
        assertThat(savedUser1).usingRecursiveComparison().isEqualTo(user1.get());
        var email2 = EMAIL_PASS.get(1).keySet().toArray()[0];
        var pass2 = EMAIL_PASS.get(1).get(email2);
        var user2 = sql2oUserRepository.save(new User(0, email2.toString(), "Ильина Ольга", pass2));
        assertThat(savedUser1).usingRecursiveComparison().isEqualTo(user1.get());
        assertThat(user2).isEmpty();
    }
}
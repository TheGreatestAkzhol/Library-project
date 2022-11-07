package org.tomik.com.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import org.tomik.com.model.Person;
import org.tomik.com.model.Book;
@Component
public class PersonDAO {
    public final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Person> index(){
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }
    public Person show(int id){
        return jdbcTemplate.query("SELECT * from Person where id =?",new Object[]{id},new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }
    public void save(Person person){
        jdbcTemplate.update("INSERT INTO Person(full_name,year_of_birth) values (?,?)",person.getFullName(),person.getYearOfBirth());
    }
    public void update(int id,Person updatedPerson){
        jdbcTemplate.update("UPDATE Person Set full_name=?,year_of_birth=? where id = ?",updatedPerson.getFullName(),
                updatedPerson.getYearOfBirth(),id);
    }
    public void delete(int id){
        jdbcTemplate.update("DELETE from person where id =?",id);
    }
    //Для валидации уникальных ФИО
    public Optional<Person> getPersonByFullName(String fullName){
        return jdbcTemplate.query("Select * from PErson where full_name=?",new Object[]{fullName},
               new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
    public List<Book> getBooksByPersonId(int id){
        return jdbcTemplate.query("Select * from book where person_id=?",new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}

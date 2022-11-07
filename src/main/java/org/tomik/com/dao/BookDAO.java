package org.tomik.com.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.tomik.com.model.Person;
import org.tomik.com.model.Book;

import java.util.List;
import java.util.Optional;
@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT *from book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int id) {
        return jdbcTemplate.query("Select * From book where id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT into book(title, author, year) values (?,?,?)",
                book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE  book set title =?, author=?,year=? where id =?",
                updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE from book where id = ?", id);
    }
    //Join'им таблицы Book и Person и получаем человека, которому принадлежит книга с указанным id
    public Optional<Person> getBookOwner(int id){
       //выбираем все колонки таблицы Person из обьединенной таблицы
       return jdbcTemplate.query("Select Person. * From book join person on book.person_id = Person.id "+
               "WHERE Book.id = ?",new Object[]{id},new BeanPropertyRowMapper<>(Person.class))
               .stream().findAny();
    }
    //Освобождает книгу(этот метод вызывается, когда человек возвращает книгу в библиотеку)
    public void release(int id){
        jdbcTemplate.update("UPDATE book set person_id=null where id=?",id);
    }
    //назначает книгу человеку(этот метод вызывается когда человек забирает книгу из библиотеки)
    public void assign(int id, Person selectedPerson){
        jdbcTemplate.update("UPDATE book set person_id=? where id = ?", selectedPerson.getId(),id);
    }
}

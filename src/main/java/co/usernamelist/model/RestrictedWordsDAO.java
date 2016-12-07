package co.usernamelist.model;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface RestrictedWordsDAO extends CrudRepository<RestrictedWords, String> {

}
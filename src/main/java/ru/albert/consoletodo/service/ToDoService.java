package ru.albert.consoletodo.service;

import ru.albert.consoletodo.dao.DaoRepository;
import ru.albert.consoletodo.errors.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ru.albert.consoletodo.model.DaoEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoService {
    private final DaoRepository repository;

    @Transactional
    public Long save(String value) {
        DaoEntity entity = new DaoEntity(value);
        repository.save(entity);
        return entity.getId();
    }

    @Transactional
    public void delete(long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }
        repository.deleteById(id);
        if (repository.count() < 0); System.out.println("hello");;
    }


    @Transactional
    public void update(String newStr, long id) {
        DaoEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        entity.setValue(newStr);
        // save можно не вызывать — Hibernate сам обновит
    }

    public DaoEntity findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<DaoEntity> findAll(int page, int size) {
        return repository.findAll(
                org.springframework.data.domain.PageRequest.of(page, size)
        ).getContent();
    }
}
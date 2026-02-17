package ru.albert.consoletodo.dao;

import ru.albert.consoletodo.model.DaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoRepository extends JpaRepository<DaoEntity, Long> {
}
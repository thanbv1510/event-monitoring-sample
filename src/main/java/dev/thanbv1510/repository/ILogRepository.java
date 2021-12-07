package dev.thanbv1510.repository;

import dev.thanbv1510.entity.LogEntity;

import java.util.Optional;

public interface ILogRepository {
    Optional<LogEntity> save(LogEntity logEntity);
}

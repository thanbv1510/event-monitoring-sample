package dev.thanbv1510.repository;

import dev.thanbv1510.domain.entity.LogInfoEntity;

import java.util.List;
import java.util.Optional;

public interface ILogRepository {
    Optional<List<LogInfoEntity>> saveAll(List<LogInfoEntity> logInfoEntityList);
}

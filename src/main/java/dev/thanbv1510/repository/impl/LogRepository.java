package dev.thanbv1510.repository.impl;

import dev.thanbv1510.entity.LogEntity;
import dev.thanbv1510.repository.ILogRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class LogRepository implements ILogRepository {

    @Override
    public Optional<LogEntity> save(LogEntity logEntity) {
//        String query = "INSERT INTO MSG_LOGS(FLOW, MSG, TIME_RECEIVE, NODE_LABEL, NODE_TYPE) VALUES (?, ?, ?, ?, ?)";
        String query = "INSERT INTO MSG_LOGS(MSG) VALUES (?)";

        try (Connection connection = EntityManagerFactory.getConnection();
             PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(query)) {
            preparedStatement.setString(1, logEntity.getMsg());

//            preparedStatement.setString(1, logEntity.getFlow());
//            preparedStatement.setString(2, logEntity.getMsg());
//            preparedStatement.setString(3, logEntity.getTimeReceive());
//            preparedStatement.setString(4, logEntity.getNodeLabel());
//            preparedStatement.setString(5, logEntity.getNodeType());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(logEntity);
    }
}

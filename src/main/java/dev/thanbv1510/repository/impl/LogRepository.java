package dev.thanbv1510.repository.impl;

import dev.thanbv1510.domain.entity.LogInfoEntity;
import dev.thanbv1510.repository.ILogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LogRepository implements ILogRepository {
    private static final Logger logger = LoggerFactory.getLogger(LogRepository.class);
    private static long id = 1; // fake id

    @Override
    public Optional<List<LogInfoEntity>> saveAll(List<LogInfoEntity> logInfoEntityList) {
        String query = "INSERT INTO LOG_INFO_TEST(MSG_ID, SYSTEM_ID, CLIENT_IP, USER_NAME, LOG_TIME, MFL_ID, MSISDN, COMMAND, RESULT_FLOW,\n" +
                "DESCRIPTION, INFO_LEVEL, STEP_NAME, CONTENT_TYPE, SERVER_NAME,KEY_TIME, MSG_CONTENT, ID, MSG_TIME)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = EntityManagerFactory.getConnection();
             PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(query)) {
            connection.setAutoCommit(false);

            for (LogInfoEntity logInfoEntity : logInfoEntityList) {
                preparedStatement.setString(1, logInfoEntity.getMsgId());
                preparedStatement.setInt(2, logInfoEntity.getSystemId());
                preparedStatement.setString(3, logInfoEntity.getClientIp());
                preparedStatement.setString(4, logInfoEntity.getUsername());
                preparedStatement.setObject(5, logInfoEntity.getLogTime());
                preparedStatement.setInt(6, logInfoEntity.getMflId());
                preparedStatement.setString(7, logInfoEntity.getMsisdn());
                preparedStatement.setString(8, logInfoEntity.getCommand());
                preparedStatement.setInt(9, logInfoEntity.getResultFlow());
                preparedStatement.setString(10, logInfoEntity.getDescription());
                preparedStatement.setString(11, logInfoEntity.getInfoLevel());
                preparedStatement.setString(12, logInfoEntity.getStepName());
                preparedStatement.setString(13, logInfoEntity.getContentType());
                preparedStatement.setString(14, logInfoEntity.getServerName());
                preparedStatement.setLong(15, logInfoEntity.getKeyTime());
                preparedStatement.setString(16, logInfoEntity.getMsgContent());
                preparedStatement.setLong(17, id++);
                preparedStatement.setObject(18, logInfoEntity.getMsgTime());

                preparedStatement.addBatch();
            }

            int[] result = preparedStatement.executeBatch();
            logger.info("The number of rows inserted: {}", result.length);
            connection.commit();

        } catch (SQLException ex) {
            logger.error("Ex: ", ex);
            return Optional.empty();
        }
        return Optional.of(logInfoEntityList);

    }
}

package com.vpactually.dao;

import com.vpactually.entities.TaskStatus;
import com.vpactually.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskStatusDAO implements DAO<Integer, TaskStatus> {

    private static final TaskStatusDAO INSTANCE = new TaskStatusDAO();

    private static final String FIND_ALL_SQL = "SELECT * FROM task_statuses";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM task_statuses WHERE id = ?";
    private static final String FIND_BY_SLUG_SQL = "SELECT * FROM task_statuses WHERE slug = ?";
    private static final String SAVE_SQL = "INSERT INTO task_statuses (name, slug, created_at) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE task_statuses SET name = ?, slug = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM task_statuses WHERE id = ?";

    private TaskStatusDAO() {
    }

    @Override
    public List<TaskStatus> findAll() {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<TaskStatus> taskStatuses = new ArrayList<>();
            while (resultSet.next()) {
                var taskStatus = buildTaskStatus(resultSet);
                taskStatuses.add(taskStatus);
            }
            return taskStatuses;
        } catch (SQLException e) {
            e.fillInStackTrace();
            return List.of();
        }
    }

    @Override
    public Optional<TaskStatus> findById(Integer id) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildTaskStatus(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.fillInStackTrace();
            return Optional.empty();
        }
    }

    public Optional<TaskStatus> findBySlug(String slug) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_BY_SLUG_SQL)) {
            preparedStatement.setObject(1, slug);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildTaskStatus(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.fillInStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public TaskStatus save(TaskStatus entity) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, entity.getName());
            preparedStatement.setObject(2, entity.getSlug());
            preparedStatement.setObject(3, entity.getCreatedAt());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getInt(1));
            }
            return entity;
        } catch (SQLException e) {
            e.fillInStackTrace();
            return null;
        }
    }

    @Override
    public TaskStatus update(TaskStatus entity) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1, entity.getName());
            preparedStatement.setObject(2, entity.getSlug());
            preparedStatement.setObject(3, entity.getId());
            preparedStatement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            e.fillInStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_SQL)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.fillInStackTrace();
            return false;
        }
        return true;
    }

    private TaskStatus buildTaskStatus(ResultSet resultSet) throws SQLException {
        var taskStatus = new TaskStatus();
        taskStatus.setId(resultSet.getInt("id"));
        taskStatus.setName(resultSet.getString("name"));
        taskStatus.setSlug(resultSet.getString("slug"));
        taskStatus.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate());
        return taskStatus;
    }

    public static TaskStatusDAO getInstance() {
        return INSTANCE;
    }
}

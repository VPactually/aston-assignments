package com.vpactually.dao;

import com.vpactually.entities.User;
import com.vpactually.util.ConnectionManager;
import com.vpactually.util.FetchType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<Integer, User> {

    private static final UserDAO INSTANCE = new UserDAO();
    private static final TaskDAO TASK_DAO = TaskDAO.getInstance();

    private static final String FIND_ALL_SQL = "SELECT * FROM users";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String SAVE_SQL = "INSERT INTO users (name, email, password, created_at) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM users WHERE id = ?";

    private UserDAO() {
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var user = buildUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> findById(Integer id) {
        User user = null;
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = buildUser(resultSet);
                user.setFetchType(FetchType.EAGER);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User save(User user) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, user.getName());
            preparedStatement.setObject(2, user.getEmail());
            preparedStatement.setObject(3, user.getPassword());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.executeUpdate();
            if (user.getTasks() != null) {
                user.setFetchType(FetchType.EAGER);
                TASK_DAO.saveUserTasks(user.getTasks(), user.getId());
            }
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return user;
    }

    @Override
    public User update(User user) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1, user.getName());
            preparedStatement.setObject(2, user.getEmail());
            preparedStatement.setObject(3, user.getPassword());
            preparedStatement.setObject(4, user.getId());
            preparedStatement.executeUpdate();
            if (user.getTasks() != null) {
                TASK_DAO.saveUserTasks(user.getTasks(), user.getId());
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return user;
    }

    @Override
    public boolean deleteById(Integer id) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_SQL)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.fillInStackTrace();
            return false;
        }
    }

    public static UserDAO getInstance() {
        return INSTANCE;
    }

    private static User buildUser(ResultSet resultSet) throws SQLException {
        var user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
        return user;
    }

}

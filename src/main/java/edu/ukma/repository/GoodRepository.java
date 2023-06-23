package edu.ukma.repository;

import edu.ukma.connection.ConnectionFactory;
import edu.ukma.domain.Good;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoodRepository {

    private final Connection connection;
    private final GoodMapper goodMapper;

    public GoodRepository() {
        this.connection = ConnectionFactory.getConnection();
        this.goodMapper = new GoodMapper();
    }

    public Good create(Good good) throws SQLException {
        String sql = "INSERT INTO good (name, quantity, price) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, good.getName());
            statement.setInt(2, good.getQuantity());
            statement.setBigDecimal(3, good.getPrice());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Good failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);
                    good.setId(generatedId);
                } else {
                    throw new SQLException("Creating Good failed, no ID obtained.");
                }
            }
        }
        return good;
    }

    public Good read(Long goodId) throws SQLException {
        String sql = """
                SELECT g.id, g.name, g.quantity, g.price
                FROM good g
                WHERE g.id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, goodId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return goodMapper.map(resultSet);
                }
            }
        }
        return null;
    }

    public int update(Long id, Good good) throws SQLException {
        String sql = "UPDATE good SET name = ?, quantity = ?, price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, good.getName());
            statement.setInt(2, good.getQuantity());
            statement.setBigDecimal(3, good.getPrice());
            statement.setLong(4, id);
            return statement.executeUpdate();
        }
    }

    public int delete(Long goodId) throws SQLException {
        String sql = "DELETE FROM good WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, goodId);
            return statement.executeUpdate();
        }
    }

    public List<Good> listByCriteria(String criteria) throws SQLException {
        List<Good> goods = new ArrayList<>();
        String sql = "SELECT g.id, g.name, g.quantity, g.price  as group_name " +
                "FROM good g " +
                "WHERE " + criteria;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Good good = goodMapper.map(resultSet);
                goods.add(good);
            }
        }
        return goods;
    }
}

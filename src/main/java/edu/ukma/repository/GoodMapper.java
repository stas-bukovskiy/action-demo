package edu.ukma.repository;

import edu.ukma.domain.Good;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GoodMapper {
    public Good map(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong(1);
        String name = resultSet.getString(2);
        Integer quantity = resultSet.getInt(3);
        BigDecimal price = resultSet.getBigDecimal(4);
        return new Good(id, name, quantity, price);
    }
}

package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DbConnection;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDAOJDBC implements DepartmentDao {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private String sql = "";

	public DepartmentDAOJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department department) {
		try {
			sql = "insert into department (name) values (?)";
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, department.getName());

			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.next()) {
					int id = resultSet.getInt(1);
					department.setId(id);
				}
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbConnection.closeResultSet(resultSet);
			DbConnection.closeStatement(preparedStatement);
		}
	}

	@Override
	public void update(Department department) {
		try {
			sql = "update department set name = ? where id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, department.getName());
			preparedStatement.setInt(2, department.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbConnection.closeStatement(preparedStatement);
		}
	}

	@Override
	public void deleteById(Integer id) {
		try {
			sql = "delete from departmet where id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbConnection.closeStatement(preparedStatement);
		}
	}

	@Override
	public Department findById(Integer id) {
		try {
			sql = "select * from department where id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Department department = new Department(resultSet.getInt("id"), resultSet.getString("name"));
				return department;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbConnection.closeResultSet(resultSet);
			DbConnection.closeStatement(preparedStatement);
		}
	}

	@Override
	public List<Department> findAll() {
		try {
			sql = "SELECT * FROM department ORDER BY name";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			List<Department> departments = new ArrayList<>();
			while (resultSet.next()) {
				Department department = new Department(resultSet.getInt("Id"), resultSet.getString("Name"));
				departments.add(department);
			}
			return departments;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			DbConnection.closeResultSet(resultSet);
			DbConnection.closeStatement(preparedStatement);
		}
	}

}
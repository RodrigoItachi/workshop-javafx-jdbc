package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DbConnection;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDAOJDBC implements SellerDao {
	private Connection connection;
	private PreparedStatement preparedStatement;// quando testar verificar se retorna null
	private ResultSet resultSet;
	private String sql = "";

	public SellerDAOJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller seller) {
		try {
			sql = "INSERT INTO seller (name, email, birthDate, baseSalary, departmentId) VALUES (?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, seller.getName());
			preparedStatement.setString(2, seller.getEmail());
			preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
			preparedStatement.setDouble(4, seller.getBaseSalary());
			preparedStatement.setInt(5, seller.getDepartment().getId());

			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.next()) {
					int id = resultSet.getInt(1);
					seller.setId(id);
				}
				DbConnection.closeResultSet(resultSet);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbConnection.closeStatement(preparedStatement);
		}
	}

	@Override
	public void update(Seller seller) {
		try {
			sql = "UPDATE seller SET name = ?, email = ?, birthDate = ?, baseSalary = ?, departmentId = ? WHERE id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, seller.getName());
			preparedStatement.setString(2, seller.getEmail());
			preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
			preparedStatement.setDouble(4, seller.getBaseSalary());
			preparedStatement.setInt(5, seller.getDepartment().getId());
			preparedStatement.setInt(6, seller.getId());
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
			sql = "DELETE FROM seller WHERE id = ?";
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
	public Seller findById(Integer id) {
		try {
			sql = "SELECT seller.*, department.name as departmentName FROM seller INNER JOIN department ON seller.departmentId = department.id WHERE seller.id = ?";

			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Department department = instantiateDepartment(resultSet);
				Seller seller = instantiateSeller(resultSet, department);
				return seller;
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
	public List<Seller> findByDepartment(Department department) {
		try {
			sql = "SELECT seller.*, department.name as departmentName FROM seller INNER JOIN department ON seller.departmentId = department.id ORDER BY name ";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, department.getId());
			resultSet = preparedStatement.executeQuery();
			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (resultSet.next()) {
				Department dep = map.get(resultSet.getInt("departmentId"));
				if (dep == null) {
					dep = instantiateDepartment(resultSet);
					map.put(resultSet.getInt("departmentId"), dep);
				}
				Seller seller = instantiateSeller(resultSet, dep);
				sellers.add(seller);
			}
			return sellers;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbConnection.closeResultSet(resultSet);
			DbConnection.closeStatement(preparedStatement);
		}
	}

	@Override
	public List<Seller> findAll() {
		try {
			sql = "SELECT seller.*, department.name AS departmentName FROM seller INNER JOIN department ON seller.departmentId = department.id ORDER BY name";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (resultSet.next()) {
				Department department = map.get(resultSet.getInt("departmentId"));
				if (department == null) {
					department = instantiateDepartment(resultSet);
					map.put(resultSet.getInt("departmentId"), department);
				}
				Seller seller = instantiateSeller(resultSet, department);
				sellers.add(seller);
			}
			return sellers;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbConnection.closeResultSet(resultSet);
			DbConnection.closeStatement(preparedStatement);
		}
	}

	private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		department.setId(resultSet.getInt("departmentId"));
		department.setName(resultSet.getString("departmentName"));
		return department;
	}

	private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(resultSet.getInt("id"));
		seller.setName(resultSet.getString("name"));
		seller.setEmail(resultSet.getString("email"));
		seller.setBirthDate(resultSet.getDate("birthDate"));
		seller.setBaseSalary(resultSet.getDouble("baseSalary"));
		seller.setDepartment(department);
		return seller;
	}

}
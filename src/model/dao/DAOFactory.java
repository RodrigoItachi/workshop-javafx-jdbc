package model.dao;

import db.DbConnection;
import model.dao.impl.DepartmentDAOJDBC;
import model.dao.impl.SellerDAOJDBC;

public class DAOFactory {
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDAOJDBC(DbConnection.getConnection());
	}

	public static SellerDao createSellerDao() {
		return new SellerDAOJDBC(DbConnection.getConnection());
	}
}
package model.services;

import java.util.List;

import model.dao.DAOFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao departmentDao = DAOFactory.createDepartmentDao();
	
	public List<Department> findAll() {
		return departmentDao.findAll();
	}
}

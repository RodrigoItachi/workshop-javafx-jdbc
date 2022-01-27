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
	
	public void saveOrUpdate(Department department) {
		if (department.getId() == null) {
			departmentDao.insert(department);
		}else {
			departmentDao.update(department);			
		}
	}
}

package model.services;

import java.util.List;

import model.dao.DAOFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	private SellerDao sellerDao = DAOFactory.createSellerDao();
	
	public List<Seller> findAll() {
		return sellerDao.findAll();
	}
	
	public void saveOrUpdate(Seller seller) {
		if (seller.getId() == null) {
			sellerDao.insert(seller);
		}else {
			sellerDao.update(seller);
		}
	}
	
	public void remove(Seller seller) {
		sellerDao.deleteById(seller.getId());
	}
} 
package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerService {

	private SellerDao dao = DaoFactory.createSellerDao();

	public List<Seller> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Seller obj) {
		//Significa que estamos inserindo um Seller
		if(obj.getId() == null) {
			dao.insert(obj);
		} else {
			//Significa que estamos editando um Seller
			dao.update(obj);
		}
	}
	
	public void remove(Seller obj) {
		dao.deleteById(obj.getId());
	}
	

}

package com.example.yiyao2.bean;
/**
 * 
 * @author GRR
 *
 */
public class DrugBean {
/**
 *  "name": "吉松",
            "image": "img/drug/20150313102024_173.jpg",
            "category": 182,
            "count": 4034,
            "fcount": 0,
            "rcount": 0,
            "PType": "解热镇痛",
            "factory": "鲁南贝特制药有限公司",
            "price": 0,
            "id": 24837
 */
	String name;
	String image;
	String PType;
	String factory;
	String price;
	String count;
	String id;
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPType() {
		return PType;
	}
	public void setPType(String pType) {
		PType = pType;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}

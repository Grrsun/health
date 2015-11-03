package com.example.yiyao2.bean;

public class HospitalBean {

	/**
	 *  "name": "河南现代医学研究院中医院(消化内科)",
            "domain": "hnxdyxyjyzyyxhnk",
            "logo": "img/hospital/00002153.jpg",
            "area": 131,
            "address": "郑州市桐柏南路28号",
            "x": 113.613,
            "y": 34.7278,
            "tel": "0371-63888503",
            "fax": "400-6246-120",
            "zipcode": "450006",
            "url": "http://www.hnweichang.com",
            "mail": "2371103362@qq.com",
            "gobus": "<p> 1.火车站乘坐20路到农业局下车即到；2.市内乘坐B1到航海西路站下车向北100米		路西即到；3.市内乘坐63路车到农业局下车即到</p>",
            "level": "三级甲等",
            "nature": "",
            "mtype": "居民医保",
            "comment": 6,
            "count": 48,
            "id": 2153
	 */
	
	String id;
	String name;
	String logo;
	String level;
	String address;
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}

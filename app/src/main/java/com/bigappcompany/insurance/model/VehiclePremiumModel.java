package com.bigappcompany.insurance.model;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 21 Oct 2017 at 4:05 PM
 */

public class VehiclePremiumModel {
	
	private String insurance_package_id,insurance_package_name,ipc_id,policy_actual_premium_price,
	    policy_final_premium_price,ipc_ncb_percentage;
	
	public String getInsurance_package_id() {
		return insurance_package_id;
	}
	
	public void setInsurance_package_id(String insurance_package_id) {
		this.insurance_package_id = insurance_package_id;
	}
	
	public String getInsurance_package_name() {
		return insurance_package_name;
	}
	
	public void setInsurance_package_name(String insurance_package_name) {
		this.insurance_package_name = insurance_package_name;
	}
	
	public String getIpc_id() {
		return ipc_id;
	}
	
	public void setIpc_id(String ipc_id) {
		this.ipc_id = ipc_id;
	}
	
	public String getPolicy_actual_premium_price() {
		return policy_actual_premium_price;
	}
	
	public void setPolicy_actual_premium_price(String policy_actual_premium_price) {
		this.policy_actual_premium_price = policy_actual_premium_price;
	}
	
	public String getPolicy_final_premium_price() {
		return policy_final_premium_price;
	}
	
	public void setPolicy_final_premium_price(String policy_final_premium_price) {
		this.policy_final_premium_price = policy_final_premium_price;
	}
	
	public String getIpc_ncb_percentage() {
		return ipc_ncb_percentage;
	}
	
	public void setIpc_ncb_percentage(String ipc_ncb_percentage) {
		this.ipc_ncb_percentage = ipc_ncb_percentage;
	}
	
	public VehiclePremiumModel(String insurance_package_id, String insurance_package_name, String ipc_id,
	                           String policy_actual_premium_price, String policy_final_premium_price,
	                           String ipc_ncb_percentage)
	{
		this.insurance_package_id = insurance_package_id;
		this.insurance_package_name = insurance_package_name;
		this.ipc_id = ipc_id;
		this.policy_actual_premium_price = policy_actual_premium_price;
		this.policy_final_premium_price = policy_final_premium_price;
		this.ipc_ncb_percentage = ipc_ncb_percentage;
	}
}

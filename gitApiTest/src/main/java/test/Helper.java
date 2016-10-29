package test;

import api.CreateRequest;

public class Helper {

	public static CreateRequest setParams(String repoName, String desc) {
		CreateRequest obj= new CreateRequest();
		obj.setDescription(desc);
		obj.setName(repoName);
		return obj;
	}
}

package test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import api.CreateRequest;
import api.CreateResponse;
import util.AESCrypt;
import util.Config;
import util.JsonUtil;

public class CreateAPITests {

	Config config;
	CreateRequest obj;
	ObjectMapper objectMapper;
	String accessToken,repoUrl,repoName;
	private static String desc="My First Repo";  
	
	@BeforeTest
	public void setup() throws Exception
	{
		config=Config.getInstance();
		accessToken=AESCrypt.decrypt(config.getConfig("access.token"));
		repoUrl=config.getConfig("api.Repo.Url");
	}


	@Test
	public void testCreateRepo() throws JsonGenerationException, JsonMappingException, IOException
	{
		repoName ="Arpit_"+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String createRepoUrl=repoUrl+"?access_token="+accessToken;
		obj=Helper.setParams(repoName, desc);
		objectMapper = new ObjectMapper();
		String body=objectMapper.writeValueAsString(obj);
		String res=JsonUtil.jsonPostWithoutAuth(createRepoUrl, body);
		CreateResponse resObj=objectMapper.readValue(res, CreateResponse.class);
		Assert.assertEquals(resObj.getName(), repoName);
		Assert.assertEquals(resObj.getDescription(), desc);
	}

	@Test (dataProvider="accessTokens")
	public void testCreateRepoWithInvalidAccessToken(String accessToken) throws JsonGenerationException, JsonMappingException, IOException
	{
		repoName ="Arpit_"+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String createRepoUrl=repoUrl+"?access_token="+accessToken;;
		obj=Helper.setParams(repoName, desc);
		objectMapper = new ObjectMapper();
		String body=objectMapper.writeValueAsString(obj);
		String res=JsonUtil.jsonPostWithoutAuth(createRepoUrl, body);
		CreateResponse resObj=objectMapper.readValue(res, CreateResponse.class);
		Assert.assertEquals(resObj.getMessage(), "Bad credentials");
	}

	@Test
	public void testPrivateRepoCreation() throws JsonGenerationException, JsonMappingException, IOException
	{
		repoName ="Arpit_"+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String createRepoUrl=repoUrl+"?access_token="+accessToken;
		obj=Helper.setParams(repoName, desc);
		obj.setPrivate(true);
		objectMapper = new ObjectMapper();
		String body=objectMapper.writeValueAsString(obj);
		String res=JsonUtil.jsonPostWithoutAuth(createRepoUrl, body);
		CreateResponse resObj=objectMapper.readValue(res, CreateResponse.class);
		Assert.assertTrue(resObj.getMessage().contains("You'll need a different OAuth scope"), "Private Repo is getting created wihout upgrading plan");
	}


	@Test (dataProvider="nameDesc")
	public void testCreateRepoNameInputCombinations(String name,String description) throws JsonGenerationException, JsonMappingException, IOException
	{
		String createRepoUrl=repoUrl+"?access_token="+accessToken;
		obj=Helper.setParams(name, description);
		objectMapper = new ObjectMapper();
		String body=objectMapper.writeValueAsString(obj);
		String res=JsonUtil.jsonPostWithoutAuth(createRepoUrl, body);
		CreateResponse resObj=objectMapper.readValue(res, CreateResponse.class);
		if(resObj.getMessage()!=null)
		{
			if(resObj.getErrors().size()>0)
				Assert.assertTrue(resObj.getErrors().get(resObj.getErrors().size()-1).getMessage().contains("name"), "Name Validations are failing");
			else
				Assert.assertEquals(resObj.getMessage(), "Validation Failed");
		}
		else
		{
			Assert.fail("Repo is getting created with errors");
		}
	}

	@Test 
	public void testDeleteRepo() throws JsonParseException, JsonMappingException, IOException
	{
		String delUrl=config.getConfig("delete.repo.Url");
		String owner=config.getConfig("repo.Owner");
		repoName=config.getConfig("perm.Repo");
		String createRepoUrl=repoUrl+"?access_token="+accessToken;
		obj=Helper.setParams(repoName, "Creating repo to delete");
		objectMapper = new ObjectMapper();
		String body=objectMapper.writeValueAsString(obj);
		JsonUtil.jsonPostWithoutAuth(createRepoUrl, body);
		String deleteRepo=delUrl+owner+repoName+"?access_token="+accessToken;
		String statusCode=JsonUtil.jsonDelete(deleteRepo);
		Assert.assertEquals(statusCode, "204");
	}

	@Test 
	public void testDeleteRepoWithLimitedAccessToken() throws Exception
	{
		String delUrl=config.getConfig("delete.repo.Url");
		String owner=config.getConfig("repo.Owner");
		repoName=config.getConfig("perm.Repo");
		String limitedAccess=AESCrypt.decrypt(config.getConfig("limited.access.token"));
		String createRepoUrl=repoUrl+"?access_token="+accessToken;
		obj=Helper.setParams(repoName, "Creating repo to delete");
		objectMapper = new ObjectMapper();
		String body=objectMapper.writeValueAsString(obj);
		JsonUtil.jsonPostWithoutAuth(createRepoUrl, body);
		String deleteRepo=delUrl+owner+repoName+"?access_token="+limitedAccess;
		String status=JsonUtil.jsonDeleteResponse(deleteRepo);
		CreateResponse resObj=objectMapper.readValue(status, CreateResponse.class);
		Assert.assertEquals(resObj.getMessage(), "Must have admin rights to Repository.");
	}

	@Test (dataProvider="deleteParams")
	public void testDeleteRepoInvalidInputs(String owner,String repoName)
	{
		String delUrl=config.getConfig("delete.repo.Url");
		String deleteRepo=delUrl+owner+repoName+"?access_token="+accessToken;
		String statusCode=JsonUtil.jsonDelete(deleteRepo);
		Assert.assertEquals(statusCode, "404");
	}


	@DataProvider(name = "nameDesc")
	public static Object[][] dataProviderMethod() 
	{
		return new Object[][] { 
			{ "Arpit","Existing Repo Name" }, 
			{ "","No Name" },
			{"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa","Text More than 100 characters"}, 
			{"><script >alert(document.cookie)</script >","Cross site Scripting"},
			{null,"null value for name"}
		};
	}

	@DataProvider(name = "deleteParams")
	public static Object[][] dataProviderMethod2() 
	{
		return new Object[][] { 
			{ "arpitaggarwal1248","" }, 
			{ "arpitaggarwal1248","invalidName" },
			{"",""}, 
			{"","NoOwner"},
			{"invalidOwner","Arpit"},
			{null,null},
		};
	}
	@DataProvider(name = "accessTokens")
	public static Object[][] dataProviderMethod3() 
	{
		return new Object[][] { 
			{ "a7cf410eb420646df8c365c09205b029e57577395"}, 
			{""}, 
			{null},
		};
	}
}

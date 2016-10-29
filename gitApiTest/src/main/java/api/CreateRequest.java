package api;

public class CreateRequest
{
    private boolean has_issues;

    private String description;

    private String name;

    private boolean has_wiki;

    private boolean has_downloads;

    private String homepage;

    private boolean _private;
    
    public CreateRequest() {
		// TODO Auto-generated constructor stub
    	this.has_issues=true;
    	this.has_wiki=true;
    	this.has_downloads=true;
    	this._private=false;
    	this.homepage="https://github.com";
	}


    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }



    public boolean isHas_issues() {
		return has_issues;
	}


	public void setHas_issues(boolean has_issues) {
		this.has_issues = has_issues;
	}


	public boolean isHas_wiki() {
		return has_wiki;
	}


	public void setHas_wiki(boolean has_wiki) {
		this.has_wiki = has_wiki;
	}


	public boolean isHas_downloads() {
		return has_downloads;
	}


	public void setHas_downloads(boolean has_downloads) {
		this.has_downloads = has_downloads;
	}

	public boolean isPrivate() {
		return _private;
	}


	public void setPrivate(boolean _private) {
		this._private = _private;
	}


	public String getHomepage ()
    {
        return homepage;
    }

    public void setHomepage (String homepage)
    {
        this.homepage = homepage;
    }


}

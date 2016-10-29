package api;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Permissions
{
	@JsonProperty("pull")
    private String pull;

	@JsonProperty("admin")
    private String admin;

	@JsonProperty("push")
    private String push;

    public String getPull ()
    {
        return pull;
    }

    public void setPull (String pull)
    {
        this.pull = pull;
    }

    public String getAdmin ()
    {
        return admin;
    }

    public void setAdmin (String admin)
    {
        this.admin = admin;
    }

    public String getPush ()
    {
        return push;
    }

    public void setPush (String push)
    {
        this.push = push;
    }

}
			
			

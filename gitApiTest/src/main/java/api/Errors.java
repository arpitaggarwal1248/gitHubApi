package api;

import org.codehaus.jackson.annotate.JsonProperty;

public class Errors
{
	@JsonProperty("message")
    private String message;

	@JsonProperty("field")
    private String field;

	@JsonProperty("resource")
    private String resource;

	@JsonProperty("code")
    private String code;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getField ()
    {
        return field;
    }

    public void setField (String field)
    {
        this.field = field;
    }

    public String getResource ()
    {
        return resource;
    }

    public void setResource (String resource)
    {
        this.resource = resource;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

}

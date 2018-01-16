import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ncd.spring.common.model.EAPUserSession;
import ncd.spring.common.util.NcdConstants;



public class ErpSessionTag extends BodyTagSupport{

	private static final long serialVersionUID = -3828835257037157085L;
	static final Logger LOGGER = LoggerFactory.getLogger(ErpSessionTag.class);

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private String attr = null;
	private String value = null;

	public void release() {
		super.release();
        this.attr = "";
	}

	public int doStartTag() throws JspTagException {
		try {
			 this.setBodyContent(bodyContent);

	         value = getSessionAttr();
	         this.printTagString(value); //?
	     } catch(Exception e) {
			LOGGER.error(e.getMessage());
//	         e.printStackTrace();
	     }
			return EVAL_BODY_BUFFERED;
		}

	public int doEndTag() throws JspException {
		try {
            if ( bodyContent != null ) {
                bodyContent.writeOut( bodyContent.getEnclosingWriter() );
            }
        } catch ( IOException e ) {
            throw new JspException(e);
        } finally {
            this.release();
        }
        return EVAL_PAGE;
	}

	protected String getSessionAttr() throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		HttpSession session = pageContext.getSession();
		EAPUserSession userSession = (EAPUserSession)session.getAttribute(NcdConstants.HTTP_SESSION_KEY);
		String rtnVal = "";
		Class klass = Class.forName("ncd.spring.common.model.EAPUserSession");
		
		Method[] methods = klass.getMethods();
		for (int i = 0; i <= methods.length - 1; i++) {
			String methodString = methods[i].getName();
			String keyAttribute = null;
			if (methodString.indexOf("get") == 0){
				keyAttribute = methodString.substring(3);
			}
			if(this.attr.equals(keyAttribute)){
				rtnVal = (String) methods[i].invoke(userSession);
			}
//
		}
		return rtnVal;
	}

	protected void printTagString(String printStr) {
		JspWriter out = this.pageContext.getOut();
		try {
			out.print(printStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

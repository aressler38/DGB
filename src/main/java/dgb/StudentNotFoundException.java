package dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Student not found")
public class StudentNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public StudentNotFoundException(String exception)
	{
		super(exception);
	}

}

package dgb;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dgb.GradebookService.GradebookNotFoundException;

@RestController
public class GradebookController {

	Integer id;
	ArrayList<Student> students;
	Student student;
	//the above variables will not be used in the final code, they are just so return types can be satisfied in stubs


	@Autowired
	GradebookService gradebookService;



	@RequestMapping(path = "/gradebook/{name}", method = RequestMethod.POST)
	public Gradebook createGradebook(@PathVariable String name)
	{
		return createGradebookOp(name);
	}

	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.POST)
	public void createSecondary(@PathVariable Integer id)
	{
		//create secondary copy of gradebook, cannot be done on primary server
		//most of this will be done via logic and communication between apps
		try {
			gradebookService.createSecondaryGradebook(id, Application.secondary_host);
		} catch (GradebookNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.POST)
	public void createStudent(@PathVariable Integer id, @PathVariable String name,
			@PathVariable String grade)
	{
		//Von - add error checking here, throw exception if gradebook doesnt exist
		try {
			gradebookService.createStudent(id, name, grade);
		} catch (GradebookNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@RequestMapping(path = "/gradebook/{name}", method = RequestMethod.PUT)
	public Gradebook updateGradebook(@PathVariable String name)
	{
		return updateGradebookOp(name);
	}


	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.PUT)
	public void updateSecondary(@PathVariable Integer id, @RequestBody Gradebook gradebook)
	{
		//create secondary copy of gradebook, cannot be done on primary server
		gradebookService.updateSecondaryGradebook(gradebook);
	}


	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.PUT)
	public void updateStudent(@PathVariable Integer id, @PathVariable String name,
			@PathVariable String grade)
	{
		//add student and grade, can not be done on secondary, changes must flow to secondary
	}

	@RequestMapping(path = "/gradebook", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public List<Gradebook> getGradebooks()
	{
		//get all gradebooks on this server, including primary and secondary copies
		return gradebookService.getGradebooks();
	}

	@RequestMapping(path = "/gradebook/{id}/student", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public List<Student> getStudents(@PathVariable Integer id)
	{
		//Von - add error checking here, throw exception if gradebook doesnt exist
		try {
			return gradebookService.getStudents(id);
		} catch (GradebookNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(path = "/gradebook/{id}/student/{name}", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public Student getStudent(@PathVariable Integer id, @PathVariable String name)
	{
		//get student information, can be done on primary or secondary copy
		return student;
	}

	@RequestMapping(path = "/gradebook/{id}", method = RequestMethod.DELETE)
	public void deleteGradebook(@PathVariable Integer id)
	{
		//delete gradebook, must be done on primary server, deletion also deletes secondary copies

		try {
			gradebookService.deleteGradebook(id);
		} catch (GradebookNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.DELETE)
	public void deleteSecondary(@PathVariable Integer id)
	{
		//delete secondary, must be done on secondary server, does not affect primary copy
		// gradebook := gradebookService.getGradebook(id);
		// if gradebook.isPrimary():
		//     throw error
		// gradebookService.deleteGradebook(id);
	}

	@RequestMapping(path = "/gradebook/{id}/student/{name}", method = RequestMethod.DELETE)
	public void deleteStudent(@PathVariable Integer id, @PathVariable String name)
	{
		//delete gradebook, must be done on primary server, deletion auto updates secondary copies
		try {
			gradebookService.deleteStudent(id, name);
		} catch (GradebookNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Gradebook createGradebookOp(String name)
	{
		try {
			return gradebookService.createGradebook(name, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IT FAILED");
		}
		return null;
	}

	public Gradebook updateGradebookOp(String name) {

		try {
			return gradebookService.updateGradebook(name);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("IT FAILED");
		}
		return null;

	}

}

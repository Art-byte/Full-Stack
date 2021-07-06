package com.example.FullStack.controller;

import com.example.FullStack.model.Employee;
import com.example.FullStack.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //display list of employees
    @RequestMapping("/")
    public String viewHomePage(Model model){
        //model.addAttribute("listEmployees", employeeService.getAllEmployees());
        //return "index";
        return findPaginated(1, "firstName", "asc", model);
    }

    /*
    * Esta funcion se encarga de capturar en el formulario, todos
    * los datos que pasamos por medio de model del objeto empleado
    * */
    @GetMapping("/showNewEmployeeForm")
    public String showNewEmployeeForm(Model model){
        //Create model attribute to bind form data
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "new_employee";
    }

    /*
    * Esta funcion, se encarga de cachar los datos que fueron capturados
    * en el formulario anterior y los envia por medio de una peticion
    * post al servidor para guardar al usuario. Una vez guardado
    * redirecciona la vista a la tabla de inicio.
    * */
    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee){
        //Save employee to database
        employeeService.saveEmployee(employee);
        return "redirect:/";
    }


    /*
     * Esta funcion primero obtiene al usuario de acuerdo por su Id
     * posterior a ello envia el objeto como parametro con los nuevos datos
     * para poder llevar a cavo el update
     * */
    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") Long id, Model model){
        //Get employee from the service
        Employee employee = employeeService.getEmployeeById(id);
        //set employee as a model to pre-populate the form
        model.addAttribute("employee", employee);
        return "update_employee";

    }

    /*
    * Recibimos el id del usuario que hay que eliminar y posterior a ello
    * la funcion llegada desde la implementacion del servicio, la elimina
    * */
    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable("id") Long id){
        this.employeeService.deleteEmployee(id);
        return "redirect:/";
    }


    /*
    *  /page.1?sortField=name&sortDir=asc
    * */
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model){
        int pageSize = 5;

        Page<Employee> page = employeeService.findPaginated(pageNo,pageSize, sortField, sortDir);
        List<Employee> listEmployees = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listEmployees", listEmployees);
        return "index";
    }
}

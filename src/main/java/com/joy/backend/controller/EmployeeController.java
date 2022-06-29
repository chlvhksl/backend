package com.joy.backend.controller;

import com.joy.backend.exceotion.ResourceNotFoundException;
import com.joy.backend.model.Employee;
import com.joy.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
 * RESTful API로 작성
 * @RestController 모든 protocol처리를 REST(비동기식)로 처리
 * @RequestMapping을 통해서 Controller 요청되는 "주소"
 * 기능을 담고 있는 Repository 클래스인 EmployeeRepository 인터페이스를 DI
 */
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("api/v1") //http:/localhost:8080/api/v1/ mapping명
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository; //@Repository로 선언이  되어있음

    //회원 전체 호출
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    //회원 입력하기
    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    //회원 ID로 조회하기
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () ->  new ResourceNotFoundException(id + "사원은 존재하지 않습니다."));
        System.out.println("getEmployeeById findById 동작 했습니다.");

        Optional<Employee> test = employeeRepository.findById(id);
        Employee testEmp = test.get();
        System.out.println("Optional<Employee>의 값 : "+testEmp.getFirstName());

//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//		return new ResponseEntity<Employee>(employee, headers, HttpStatus.OK);
        return ResponseEntity.ok(employee); // 200인 상태일때 ok실행 employee를 전달 하겠다 => builder방법
        // new를 통한 ResponseEntity 객체를 생성하여 필요한 (값, header, 응답)을 입력하여 처리
        // builder를 통해서 값 전달 ok() 응답(status)에 대한 형태가 200일 경우에 반환
    }

    //회원 수정하기
    //JPA에서는 update. modify와 같은 메소드를 제공하고 있지않음 따라서 객체를 받아온 후 set()메소드를 사용하여 자동으로 변경
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,
                                                   @RequestBody Employee employeeDatail){//화면에서 전달받은 값
        Employee employee = //주소에서 전달받은  {id}값으로 DataBase에서 검색된 객체
                employeeRepository.findById(id).orElseThrow(
                        ()-> new ResourceNotFoundException(id+"사원이 존재하지 않습니다."));
        //화면에서 입력받는 값을 검색된 새로운 값으로 변경하여 객체 생성
        employee.setFirstName(employeeDatail.getFirstName());
        employee.setLastName(employeeDatail.getLastName());
        employee.setEmailId(employeeDatail.getEmailId());

        //JPA의 save()를 통해서 새로 저장 후 결과 값 변환
        Employee updateEmployee = employeeRepository.save(employee);

        //ok는 즉 HttpStatus가 200인 경우 새로 변경된 객체를 반화
        return ResponseEntity.ok(updateEmployee);
    }

}

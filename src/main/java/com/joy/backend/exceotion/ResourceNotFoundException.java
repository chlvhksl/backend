package com.joy.backend.exceotion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND) //404
public class ResourceNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 8005357596276819294L;

   public ResourceNotFoundException(String msg){
       super(msg);
   }

}

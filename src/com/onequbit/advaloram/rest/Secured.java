package com.onequbit.advaloram.rest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.ws.rs.NameBinding;

import com.onequbit.advaloram.hibernate.entity.Role;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured { 
	Role[] value() default {
		
	};	
}

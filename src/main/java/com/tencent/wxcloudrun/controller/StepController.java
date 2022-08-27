package com.tencent.wxcloudrun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class StepController {


	@RequestMapping("/step2")
	public String step(){
		return "step2";
	}
	@RequestMapping("/step3")
	public String step3(){
		return "step3";
	}
	@RequestMapping("/step6")
	public String step6(){
		return "step6";
	}
	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	@RequestMapping("/step")
	public String step2(){
		return "step";
	}


  }

package org.eduqi.eduqiservice.rest.control;

import org.apache.log4j.Logger;
import org.eduqi.eduqiservice.core.domain.AnswerResults;
import org.eduqi.eduqiservice.core.domain.Formanswers;
import org.eduqi.eduqiservice.core.service.AnswerStatsService;
import org.eduqi.eduqiservice.core.service.EduqiSchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/")
public class EduqiSchoolRequestImpl implements EduqiSchoolRequest{
	
	private static final Logger LOG = Logger.getLogger(EduqiSchoolRequestImpl.class);
	@Autowired
	private EduqiSchoolService eduqiSchoolRequest;
	@Autowired
	private AnswerStatsService answerStats;
	
	@RequestMapping(value ="/generalstats", method = RequestMethod.GET,
			headers="Accept=application/json, application/xml")
	public @ResponseBody AnswerResults getSGeneralStats() { 
		LOG.info("Getting statistics");
		AnswerResults result = new AnswerResults();
		result.setResults(answerStats.getAverageAnswers());
		
		LOG.info("Returning statistics");
		return result;
	}
	
	@RequestMapping(value ="/schoolanswers/{schoolID}", method = RequestMethod.GET,
			headers="Accept=application/json, application/xml")
	public @ResponseBody Formanswers getSchoolAnswers(@PathVariable String schoolID){
		int id = 0;
		Formanswers result = new Formanswers();
		if(schoolID != null){
			try{
				id = Integer.parseInt(schoolID);
			}catch(NumberFormatException e){
				LOG.warn("Invalid Number, Please check your ID and send an integer ID.");
				return result;
			}
			result = eduqiSchoolRequest.getSchoolAnswers(id);
		}
		return result;
	}
}

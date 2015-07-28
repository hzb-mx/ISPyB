package ispyb.ws.rest;

import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.measurementToDataCollection.MeasurementToDataCollection3Service;
import ispyb.server.biosaxs.services.core.plateType.PlateType3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.services.core.robot.Robot3Service;
import ispyb.server.biosaxs.services.core.samplePlate.Sampleplate3Service;
import ispyb.server.biosaxs.services.webUserInterface.WebUserInterfaceService;
import ispyb.server.common.services.config.MenuGroup3Service;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.collections.Session3Service;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RestWebService {
	private long now;
	
	private final static Logger log = Logger.getLogger(RestWebService.class);
	protected Gson getGson() {
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE)
				.serializeSpecialFloatingPointValues()
				.create();
	}
	
	protected Gson getWithoutExposeAnnotationGson() {
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE)
				.serializeSpecialFloatingPointValues()
				.excludeFieldsWithoutExposeAnnotation()
				.create();
	}
	

	protected Response sendResponse(String response) {
		return Response.ok(response).header("Access-Control-Allow-Origin", "*")
				.build();
	}

	protected Response sendResponse(Object response) {
		return Response.ok(getGson().toJson(response))
				.header("Access-Control-Allow-Origin", "*").build();
	}

	protected Response unauthorizedResponse() {
		return Response.status(401).build();
	}

	protected PlateType3Service getPlateType3Service() throws NamingException{
		return (PlateType3Service) Ejb3ServiceLocator.getInstance().getLocalService(PlateType3Service.class);
	}
	
	protected MenuGroup3Service getMenuGroup3Service() throws NamingException{
		return (MenuGroup3Service) Ejb3ServiceLocator.getInstance().getLocalService(MenuGroup3Service.class);
	}
	
	protected Analysis3Service getAnalysis3Service() throws NamingException{
		return (Analysis3Service) Ejb3ServiceLocator.getInstance().getLocalService(Analysis3Service.class);
	}
	
	protected Experiment3Service getExperiment3Service() throws NamingException{
		return (Experiment3Service) Ejb3ServiceLocator.getInstance().getLocalService(Experiment3Service.class);
	}
	
	protected Proposal3Service getProposal3Service() throws NamingException {
	    return (Proposal3Service) Ejb3ServiceLocator.getInstance().getLocalService(Proposal3Service.class);
	}
	
	protected SaxsProposal3Service getSaxsProposal3Service() throws NamingException{
		return (SaxsProposal3Service) Ejb3ServiceLocator.getInstance().getLocalService(SaxsProposal3Service.class);
	}
	
	protected MeasurementToDataCollection3Service getMeasurementToDataCollectionService() throws NamingException {
	    return (MeasurementToDataCollection3Service) Ejb3ServiceLocator.getInstance().getLocalService(MeasurementToDataCollection3Service.class);
	}

	protected Session3Service getSession3Service() throws NamingException{
		return (Session3Service) Ejb3ServiceLocator.getInstance().getLocalService(Session3Service.class);
	}

	protected Sampleplate3Service getSamplePlate3Service() throws NamingException{
		return (Sampleplate3Service) Ejb3ServiceLocator.getInstance().getLocalService(Sampleplate3Service.class);
	}
	
	protected PrimaryDataProcessing3Service getPrimaryDataProcessing3Service() throws NamingException{
		return (PrimaryDataProcessing3Service) Ejb3ServiceLocator.getInstance().getLocalService(PrimaryDataProcessing3Service.class);
	}
	
	protected LabContact3Service getLabContact3Service() throws NamingException{
		return (LabContact3Service) Ejb3ServiceLocator.getInstance().getLocalService(LabContact3Service.class);
	}
	
	protected Shipping3Service getShipping3Service() throws NamingException{
		return (Shipping3Service) Ejb3ServiceLocator.getInstance().getLocalService(Shipping3Service.class);
	}
	
	protected Robot3Service getRobot3Service() throws NamingException{
		return (Robot3Service) Ejb3ServiceLocator.getInstance().getLocalService(Robot3Service.class);
	}
	
	protected Dewar3Service getDewar3Service() throws NamingException{
		return (Dewar3Service) Ejb3ServiceLocator.getInstance().getLocalService(Dewar3Service.class);
	}
	
	protected WebUserInterfaceService getWebUserInterfaceService() throws NamingException{
		return (WebUserInterfaceService) Ejb3ServiceLocator.getInstance().getLocalService(WebUserInterfaceService.class);
	}
	
	
	/**
	 * Gets proposal Id by login name
	 * @param proposal
	 * @return
	 * @throws Exception
	 */
	protected int getProposalId(String proposal) throws Exception {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator
				.getInstance();
		SaxsProposal3Service saxsProposalService = (SaxsProposal3Service) ejb3ServiceLocator
				.getLocalService(SaxsProposal3Service.class);
		List<Proposal3VO> proposals = saxsProposalService
				.findProposalByLoginName(proposal);
		if (proposals != null) {
			if (proposals.size() > 0) {
				return proposals.get(0).getProposalId();
			}
		}
		throw new Exception("No proposal found.");
	}

	protected List<Map<String, Object>> filter(
			List<Map<String, Object>> elements, String key, String value)
			throws Exception {
		List<Map<String, Object>> filtered = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> element : elements) {
			if (element.containsKey(key)) {
				if (element.get(key) != null) {
					if (element.get(key).toString().trim().equals(value.trim())) {
						filtered.add(element);
					}
				}
			}
		}
		return filtered;
	}

	protected List<Map<String, Object>> filter(
			List<Map<String, Object>> elements, String key, List<String> values)
			throws Exception {
		log.info("values: " + values.toString());
		
		Set<String> hash = new HashSet<String>();
		for (String value : values) {
			log.info("value: " + value);
			hash.add(value.trim());
		}
		List<Map<String, Object>> filtered = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> element : elements) {
			if (element.containsKey(key)) {
				if (element.get(key) != null) {
					
					if (hash.contains(element.get(key).toString().trim())) {
						filtered.add(element);
					}
				}
			}
		}
		return filtered;
	}

	
	/**
	 * Form a comma separated string returns a list of integers
	 * 
	 * @param commaSeparated
	 * @return
	 */
	protected List<Integer> parseToInteger(String commaSeparated) {
		Set<String> keys = new HashSet<String>();
		if (commaSeparated != null) {
			List<String> myList = Arrays.asList(commaSeparated.split(","));
			ArrayList<Integer> intList = new ArrayList<Integer>();
			if (!myList.equals("")) {
				for (String string : myList) {
					try {
						/** We remove repeated ids **/
						if (!keys.contains(string)){
							if (string.matches("\\d+")){
								intList.add(Integer.valueOf(string));
								keys.add(string);
							}
						}
					} catch (Exception e) {
						/** No parseable value ***/
						log.info(e.getMessage());
					}
				}
			}
			return intList;
		}
		return null;
	}

	protected List<String> parseToString(String commaSeparated) {
		if (commaSeparated != null) {
			return Arrays.asList(commaSeparated.split(","));
		}
		return null;
	}

	protected long logInit(String methodName, String params, Logger logger) {
		logger.info("-----------------------");
		this.now = System.currentTimeMillis();
		logger.info(methodName.toUpperCase());
		LoggerFormatter.log(logger, LoggerFormatter.Package.BIOSAXS_WS, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), params);
		return this.now;
	}
	
	protected void logFinish(String methodName, long id, Logger logger) {
		logger.debug("### [" + methodName.toUpperCase() + "] Execution time was " + (System.currentTimeMillis() - this.now) + " ms.");
		LoggerFormatter.log(logger, LoggerFormatter.Package.BIOSAXS_WS, methodName, id, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);

	}
	
}

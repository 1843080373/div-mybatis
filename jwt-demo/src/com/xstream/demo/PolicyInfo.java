package com.xstream.demo;

import java.util.List;

public class PolicyInfo {
	private String SerialNo;
	private String RiskCode;
	private String OperateTimes;
	private String StartDate;
	private String EndDate;
	private String StartHour;
	private String EndHour;
	private String SumAmount;
	private String SumPremium;
	private String ArguSolution;
	private CargoInfo CargoInfo;
	private InsuredPlan InsuredPlan;
	private Applicant Applicant;
    private List<Insured> Insureds;
    
	public String getSerialNo() {
		return SerialNo;
	}
	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}
	public String getRiskCode() {
		return RiskCode;
	}
	public void setRiskCode(String riskCode) {
		RiskCode = riskCode;
	}
	public String getOperateTimes() {
		return OperateTimes;
	}
	public void setOperateTimes(String operateTimes) {
		OperateTimes = operateTimes;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public String getStartHour() {
		return StartHour;
	}
	public void setStartHour(String startHour) {
		StartHour = startHour;
	}
	public String getEndHour() {
		return EndHour;
	}
	public void setEndHour(String endHour) {
		EndHour = endHour;
	}
	public String getSumAmount() {
		return SumAmount;
	}
	public void setSumAmount(String sumAmount) {
		SumAmount = sumAmount;
	}
	public String getSumPremium() {
		return SumPremium;
	}
	public void setSumPremium(String sumPremium) {
		SumPremium = sumPremium;
	}
	public String getArguSolution() {
		return ArguSolution;
	}
	public void setArguSolution(String arguSolution) {
		ArguSolution = arguSolution;
	}
	public CargoInfo getCargoInfo() {
		return CargoInfo;
	}
	public void setCargoInfo(CargoInfo cargoInfo) {
		CargoInfo = cargoInfo;
	}
	public InsuredPlan getInsuredPlan() {
		return InsuredPlan;
	}
	public void setInsuredPlan(InsuredPlan insuredPlan) {
		InsuredPlan = insuredPlan;
	}
	public Applicant getApplicant() {
		return Applicant;
	}
	public void setApplicant(Applicant applicant) {
		Applicant = applicant;
	}
	public List<Insured> getInsureds() {
		return Insureds;
	}
	public void setInsureds(List<Insured> insureds) {
		Insureds = insureds;
	}
}

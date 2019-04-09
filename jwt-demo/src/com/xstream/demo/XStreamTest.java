package com.xstream.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamTest {
	private static YDL ydl;
	private static SimpleDateFormat sdf_hhmmss=new SimpleDateFormat("YY-MM-DD HH:MM:SS");
	private static SimpleDateFormat sdf_yymmrr=new SimpleDateFormat("YY-MM-DD");
	private static XStream xstream = new XStream();//�?要xpp3的jar文件
	private static  XStream xstream1 = new XStream(new DomDriver());//�?要xpp3的jar文件
	static {
		xstream.alias("YDL", YDL.class); 
        xstream.alias("PolicyInfo", PolicyInfo.class);  
        xstream.alias("Insured", Insured.class);  
        
        xstream1.alias("YDL", YDL.class); 
        xstream1.alias("PolicyInfo", PolicyInfo.class);  
        xstream1.alias("Insured", Insured.class); 
	}
	public static void main(String[] args) {
		testObject2XML();
		testXML2Object();
		testJSON2XML();
	}

	public static void testObject2XML() {
		ydl=new YDL();
		GeneralInfo GeneralInfo=new GeneralInfo();
		ydl.setGeneralInfo(GeneralInfo);
		GeneralInfo.setUUID(UUID.randomUUID().toString().replaceAll("-", ""));
		GeneralInfo.setPlateformCode("CPI000147");
		GeneralInfo.setMd5Value(MD5Util.md5Hex(GeneralInfo.getPlateformCode()));
		List<PolicyInfo> PolicyInfos=new ArrayList<PolicyInfo>();
		ydl.setPolicyInfos(PolicyInfos);
		PolicyInfo PolicyInfo=new PolicyInfo();
		PolicyInfo.setSerialNo(String.valueOf(System.currentTimeMillis()));
		PolicyInfo.setRiskCode("YDL");
		Date now=new Date();
		PolicyInfo.setOperateTimes(sdf_hhmmss.format(now));
		PolicyInfo.setStartDate(sdf_yymmrr.format(now));
		Calendar calendar=new GregorianCalendar();
		calendar.setTime(now);
		calendar.add(Calendar.MONTH, 1);
		PolicyInfo.setEndDate(sdf_yymmrr.format(calendar.getTime()));
		PolicyInfo.setStartHour("16");
		PolicyInfo.setEndHour("21");
		PolicyInfo.setSumAmount("398");
		PolicyInfo.setSumPremium("200");
		PolicyInfo.setArguSolution("1");
		PolicyInfos.add(PolicyInfo);
		
		CargoInfo CargoInfo=new CargoInfo();
		CargoInfo.setItemName("");
		CargoInfo.setEndSiteName("");
		CargoInfo.setCarryBillNo("");
		CargoInfo.setNumber("1");
		CargoInfo.setStartSiteName("");
		PolicyInfo.setCargoInfo(CargoInfo);
		InsuredPlan InsuredPlan=new InsuredPlan();
		InsuredPlan.setRationType("YDL3300001");
		PolicyInfo.setInsuredPlan(InsuredPlan);
		Applicant Applicant=new Applicant();
		Applicant.setAppliIdentity("1");
		Applicant.setAppliIdNo("");
		Applicant.setAppliIdType("");
		Applicant.setAppliName("皇后印象");
		PolicyInfo.setApplicant(Applicant);
        List<Insured> Insureds=new ArrayList<Insured>();
        Insured insured=new Insured(); 
        insured.setInsuredIdNo("");
        insured.setInsuredIdType("");
        insured.setInsuredName("皇后印象");
        insured.setInsuredSeqNo("");
        Insureds.add(insured);
        PolicyInfo.setInsureds(Insureds);
		String xml = xstream.toXML(ydl);
		System.out.println(xml);
	}

	
	public static void testXML2Object() {
		 String xml="<YDL>" + 
		 		"  <GeneralInfo>" + 
		 		"    <UUID>179610210010184117</UUID>" + 
		 		"    <PlateformCode>CPI000147</PlateformCode>" + 
		 		"    <Md5Value>a7c3a7d69ceff7f716976e2baa5ce439</Md5Value>" + 
		 		"  </GeneralInfo>" + 
		 		"  <PolicyInfos>" + 
		 		"    <PolicyInfo>" + 
		 		"      <SerialNo>1</SerialNo>" + 
		 		"      <RiskCode>YDL</RiskCode>" + 
		 		"      <OperateTimes>2019-03-06 16:36:33</OperateTimes>" + 
		 		"      <StartDate>2019-03-06</StartDate>" + 
		 		"      <EndDate>2019-03-06</EndDate>" + 
		 		"      <StartHour>16</StartHour>" + 
		 		"      <EndHour>21</EndHour>" + 
		 		"      <SumAmount></SumAmount>" + 
		 		"      <SumPremium></SumPremium>" + 
		 		"      <ArguSolution>1</ArguSolution>" + 
		 		"      <CargoInfo>" + 
		 		"        <ItemName></ItemName>" + 
		 		"        <StartSiteName></StartSiteName>" + 
		 		"        <EndSiteName></EndSiteName>" + 
		 		"        <Number>1</Number>" + 
		 		"        <CarryBillNo></CarryBillNo>" + 
		 		"      </CargoInfo>" + 
		 		"      <InsuredPlan>" + 
		 		"        <RationType>YDL3300001</RationType>" + 
		 		"      </InsuredPlan>" + 
		 		"      <Applicant>" + 
		 		"        <AppliName>皇后印象</AppliName>" + 
		 		"        <AppliIdType></AppliIdType>" + 
		 		"        <AppliIdNo></AppliIdNo>" + 
		 		"        <AppliIdentity>1</AppliIdentity>" + 
		 		"      </Applicant>" + 
		 		"      <Insureds>" + 
		 		"        <Insured>" + 
		 		"          <InsuredSeqNo></InsuredSeqNo>" + 
		 		"          <InsuredName>皇后印象</InsuredName>" + 
		 		"          <InsuredIdType></InsuredIdType>" + 
		 		"          <InsuredIdNo></InsuredIdNo>" + 
		 		"        </Insured>" + 
		 		"      </Insureds>" + 
		 		"    </PolicyInfo>" + 
		 		"  </PolicyInfos>" + 
		 		"</YDL>";
		 YDL ydl = (YDL) xstream1.fromXML(xml);
		 System.out.println(JSONObject.toJSONString(ydl));
	}
	
	public static void testJSON2XML() {
		 String json="{\"generalInfo\":{\"md5Value\":\"a7c3a7d69ceff7f716976e2baa5ce439\",\"plateformCode\":\"CPI000147\",\"uUID\":\"179610210010184117\"},\"policyInfos\":[{\"applicant\":{\"appliIdNo\":\"\",\"appliIdType\":\"\",\"appliIdentity\":\"1\",\"appliName\":\"皇后印象\"},\"arguSolution\":\"1\",\"cargoInfo\":{\"carryBillNo\":\"\",\"endSiteName\":\"\",\"itemName\":\"\",\"number\":\"1\",\"startSiteName\":\"\"},\"endDate\":\"2019-03-06\",\"endHour\":\"21\",\"insuredPlan\":{\"rationType\":\"YDL3300001\"},\"insureds\":[{\"insuredIdNo\":\"\",\"insuredIdType\":\"\",\"insuredName\":\"皇后印象\",\"insuredSeqNo\":\"\"}],\"operateTimes\":\"2019-03-06 16:36:33\",\"riskCode\":\"YDL\",\"serialNo\":\"1\",\"startDate\":\"2019-03-06\",\"startHour\":\"16\",\"sumAmount\":\"\",\"sumPremium\":\"\"}]}";
		 ydl=JSONObject.parseObject(json, YDL.class);
		 String xml = xstream.toXML(ydl);
		 System.out.println(xml);
	}
}


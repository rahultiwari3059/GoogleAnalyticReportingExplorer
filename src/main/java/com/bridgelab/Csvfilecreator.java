package com.bridgelab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.util.SystemPropertyUtils;

import com.bridgelab.model.GAreportModel;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;

public class Csvfilecreator {
	static int i = 0, j = 0, k = 0;
	static boolean flag = false;
	String dimension8var = "dimension8";
	IdandDiscReader id = new IdandDiscReader();
	GAreportModel ga = new GAreportModel();

	public void csvfilecreate(String response) {
		int temp1 = 0, temp2 = 0, temp3 = 0;
		// making object of Csvfilecreator
		Csvfilecreator js = new Csvfilecreator();
		// creating values ArrayList
		ArrayList<String> values = new ArrayList<String>();
		// creating values1 ArrayList
		ArrayList<String> values1 = new ArrayList<String>();
		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(response);
			// converting object into JSONObject
			JSONObject jsonObject = (JSONObject) obj;
			// covering report array into JSONArray
			JSONArray reportarray = (JSONArray) jsonObject.get("reports");
			// reading report JSONArray
			for (int j = 0; j < reportarray.size(); j++) {
				// getting first object and converting into JSONObject
				JSONObject obj3 = (JSONObject) reportarray.get(j);
				// making JSONObject of data
				JSONObject dataobject = (JSONObject) obj3.get("data");
				// making JSONArray of rows
				JSONArray rowarray = (JSONArray) dataobject.get("rows");
				// storing row JSONArray size into temp1
				temp1 = rowarray.size();
				// System.out.println(temp1);
				// reading rows JSONArray
				for (int i = 0; i < rowarray.size(); i++) {
					// getting first object and converting into JSONObject
					JSONObject rowobject = (JSONObject) rowarray.get(i);
					// making metrics JSONArray
					JSONArray metricarray = (JSONArray) rowobject.get("metrics");
					// storing metric JSONArray size into temp2
					temp2 = metricarray.size();
					// System.out.println(temp2);
					// iterating metric JSONArray
					for (int k = 0; k < metricarray.size(); k++) {
						// getting first object and converting into JSONObject
						JSONObject metricobject = (JSONObject) metricarray.get(k);
						// making values JSONArray
						JSONArray valuesarray = (JSONArray) metricobject.get("values");
						temp2 = valuesarray.size();
						if (temp2 == 1) {
							// converting JSONArray into JSONString
							String valuestring = JSONArray.toJSONString(valuesarray);
							// making subString
							valuestring = valuestring.substring(valuestring.indexOf("[") + 2,
									valuestring.indexOf("]") - 1);
							// adding into value1 ArrayList
							values1.add(valuestring);
						}

						else {
							for (int l1 = 0; l1 < valuesarray.size(); l1++) {
								// System.out.println( valuesarray.get(l1));
								values1.add((String) valuesarray.get(l1));
							}
						}
					}
					JSONArray dimensionsarray = (JSONArray) rowobject.get("dimensions");

					temp3 = dimensionsarray.size();
					for (int l = 0; l < dimensionsarray.size(); l++) {
						// adding into value ArrayList
						if (dimension8var == (String) dimensionsarray.get(l)) {
							flag = true;

						}
						// adding into ArrayList
						values.add((String) dimensionsarray.get(l));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// calling createcsv method
		js.createCsv(values, values1, temp1, temp2, temp3);

	}

	public void createCsv(ArrayList<String> list, ArrayList<String> list1, int temp1, int temp2, int temp3) {
		int k = 0;
		int p = 0;
		int c = 0;
		String date = null;
		String uniqueuser = null;
		int total1 = 0;
		Integer total = 0;
		// creating object of hashMap for date 
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		// creating object of hashMap androidId
		HashMap<String, Integer> hm1 = new HashMap<String, Integer>();
		// creating ArrayList object  for  storing dates 
		ArrayList<String> datearray= new ArrayList<String>();
		// creating ArrayList object for storing uniqueAndroidId
		ArrayList<String> uniqueandroidId = new ArrayList<String>();
		// creating ArrayList object for storing totalarray
		ArrayList<Integer> totalarray= new ArrayList<Integer>();
		int r = 1;
		int l = 1;
		try {
			// calling method of IdandDiscReader and storing into ArrayList
			ArrayList<String> s1 = id.gaid();
			// taking first element and setting in GAreportmodel
			ga.setGAID(s1.get(0));
			// taking second element and storing in GAreportmodel
			ga.setGaDiscription(s1.get(1));
			// initializing the boolean value
			boolean b1 = false;
			boolean b = false;
			// creating the new csv file
			File file1 = new File("/home/bridgeit/Music/allcsv/" + s1.get(1) + ".csv");
			// if file is not created yet then set b as true
			if (!file1.exists()) {
				b1 = true;
			}

			FileWriter fw1 = new FileWriter(file1.getAbsoluteFile(), true);
			BufferedWriter bw1 = new BufferedWriter(fw1);

			if (b1) {
				file1.createNewFile();
				// appending column names
				bw1.append("gaid");
				bw1.append("^");
				bw1.append("gadiscription");
				bw1.append("^");
				bw1.append("Date");
				bw1.append("^");
				bw1.append("AndroisId");
				bw1.append("^");
				bw1.append("Eventcategory");
				bw1.append("^");
				bw1.append("connectiontype");
				bw1.append("^");
				bw1.append("Totalevents");
				bw1.append("^");
				bw1.append("Sessions");
				bw1.append("^");
				bw1.append("Screenviews");
				bw1.append("^");
				bw1.append("Exit");
				bw1.append("^");
				bw1.append("ExitRate");
				bw1.append("^");
				bw1.newLine();
			}
			// id dimension have 3 element and to give choice between
			// eventCategory and connection type
			if (temp3 == 3) {
				for (i = 0; i < temp1; i++) {
					// appending id and gadiscription
					bw1.append(ga.getGAID());
					bw1.append("^");
					bw1.append(ga.getGaDiscription());
					bw1.append("^");
					// appending dimension output
					for (j = 0; j < temp3; j++) {

						// condition for taking date and setting
						if (k % 3 == 0) {
							// setting date into model class
							ga.setDate(list.get(k));
							// adding into HashMap according to date
							hm.put(ga.getDate(), r++);
						}
						if (k % 3 == 1) {
							// setting android id in model class
							ga.setAndroidId(list.get(k));
							// adding into HashMap according to android id to
							// fetch number of unique user
							hm1.put(ga.getAndroidId(), l++);
							
						}

						// System.out.println(list.get(k));
						bw1.append(list.get(k));
						bw1.append("^");
						// if dimension8 is there in DimensionList then put space 
						if (s1.get(2) == "true") {
							if (k % 3 == 1) {
								bw1.append(" ");
								bw1.append("^");
							}
						}
						k++;
					}
					// if dimension8 is not there then put space after 
					if (!s1.get(2).equals("true")) {
						bw1.append(" ");
						bw1.append("^");
					}
					// appending metric value
					for (int m = 0; m < temp2; m++) {
						
						bw1.append(list1.get(p));
						p++;
						bw1.append("^");
					}
					bw1.append(" ");
					bw1.append("^");
					bw1.append(" ");
					bw1.append("^");
					bw1.append(" ");
					bw1.append("^");
					bw1.append(" ");
					bw1.append("^");
					bw1.newLine();

				}
				bw1.close();
				 System.out.println(hm1.size());
				// taking number of unique android id
				total1 = hm1.size();
				uniqueuser = String.valueOf(total1);
				for (Entry<String, Integer> m1 : hm1.entrySet()) {
					// taking value
					total = m1.getValue();
					uniqueandroidId.add(m1.getKey());
				}
				for (Entry<String, Integer> m : hm.entrySet()) {
					//putting key and value into date and total 
					date = m.getKey();
					total = m.getValue();
					//adding into date ArrayList
					datearray.add(m.getKey());
					totalarray.add(m.getValue());
					// printing key value pair
					System.out.println(m.getKey() + " " + m.getValue());
				}
				for (int k1 = 0; k1 < uniqueandroidId.size(); k1++) {
					//System.out.println(uniqueandroidId.get(k1));
				}
			}
			// if metric having 4 element and 2 dimension
			else {
				if (temp2 == 4 && temp3 == 2) {
					r = 0;
					for (i = 0; i < temp1; i++) {
						// appending id and gadiscription
						bw1.append(ga.getGAID());
						bw1.append("^");
						bw1.append(ga.getGaDiscription());
						bw1.append("^");
						// appending dimension output
						for (j = 0; j < temp3; j++) {
							if (k % 2 == 0) {
								// setting date into model class
								ga.setDate(list.get(k));
								// adding into HashMap according to date
								hm.put(ga.getDate(), r++);
							}
							if (k % 2 == 1) {
								// setting android id in model class
								ga.setAndroidId(list.get(k));
								// adding into HashMap according to android id
								// to fetch number of unique user
								hm1.put(ga.getAndroidId(), l++);

							}
							//appending value
							bw1.append(list.get(k));
							bw1.append("^");
							k++;
						}
						bw1.append(" ");
						bw1.append("^");
						bw1.append(" ");
						bw1.append("^");
						bw1.append(" ");
						bw1.append("^");
						// appending metric value
						for (int m = 0; m < temp2; m++) {

							// System.out.println(list1.get(p));
							bw1.append(list1.get(p));
							p++;
							bw1.append("^");
						}
						bw1.newLine();
					}
					bw1.close();
					 System.out.println(hm1.size());
						// taking number of unique android id
						total1 = hm1.size();
						uniqueuser = String.valueOf(total1);
						for (Entry<String, Integer> m1 : hm1.entrySet()) {
							// taking value
							total = m1.getValue();
							uniqueandroidId.add(m1.getKey());
						}
						for (Entry<String, Integer> m : hm.entrySet()) {
							date = m.getKey();
							total = m.getValue();
							datearray.add(m.getKey());
							totalarray.add(m.getValue());
							// printing key value pair
							System.out.println(m.getKey() + " " + m.getValue());
						}
					
						for (int k1 = 0; k1 < uniqueandroidId.size(); k1++) {
							//System.out.println(uniqueandroidId.get(k1));
				
						}
				}

				else {

					if (temp3 == 2) {
						for (i = 0; i < temp1; i++) {
							// appending id and gadiscription
							bw1.append(ga.getGAID());
							bw1.append("^");
							bw1.append(ga.getGaDiscription());
							bw1.append("^");
							// appending dimension output
							for (j = 0; j < temp3; j++) {
								if (k % 2 == 0) {
									ga.setDate(list.get(k));
									hm.put(ga.getDate(), r++);
								}
								if (k % 2 == 1) {
									ga.setAndroidId(list.get(k));
									hm1.put(ga.getAndroidId(), l++);

								}
								// appending value 
								bw1.append(list.get(k));
								k++;
								bw1.append("^");
							}
							bw1.append(" ");
							bw1.append("^");
							bw1.append(" ");
							bw1.append("^");
							// appending metric value
							for (int m = 0; m < temp2; m++) {

								// System.out.println(list1.get(p));
								bw1.append(list1.get(p));
								p++;
								bw1.append("^");
							}
							bw1.append(" ");
							bw1.append("^");
							bw1.append(" ");
							bw1.append("^");
							bw1.append(" ");
							bw1.append("^");
							bw1.append(" ");
							bw1.append("^");
							bw1.newLine();
						}
						bw1.close();
						 System.out.println(hm1.size());
							// taking number of unique android id
							total1 = hm1.size();
							uniqueuser = String.valueOf(total1);
							for (Entry<String, Integer> m1 : hm1.entrySet()) {
								// taking value
								total = m1.getValue();
								uniqueandroidId.add(m1.getKey());
							}
							for (Entry<String, Integer> m : hm.entrySet()) {
								date = m.getKey();
								total = m.getValue();
								datearray.add(m.getKey());
								totalarray.add(m.getValue());
								// printing key value pair
								System.out.println(m.getKey() + " " + m.getValue());
							}
						
							for (int k1 = 0; k1 < uniqueandroidId.size(); k1++) {
								//System.out.println(uniqueandroidId.get(k1));
					
							}
					}
				}

			}
			File file = new File("/home/bridgeit/Music/summaryreport.csv");
			if (!file.exists()) {
				b = true;
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			if (b) {
				file.createNewFile();
				// appending id and gadiscription
				bw.append("gaid");
				bw.append("^");
				bw.append("gadiscription");
				bw.append("^");
				bw.append("totaluniqueandroidid");
				bw.append("^");
				//appending date in summary response
				for (int j1 = 0; j1 < datearray.size(); j1++) {
					bw.append(datearray.get(j1));
					bw.append("^");
				}
				bw.newLine();
			}
			if (true) {
				bw.append(ga.getGAID());
				bw.append("^");
				bw.append(ga.getGaDiscription());
				bw.append("^");
				// appending unique user total value 
				bw.append(uniqueuser);
				bw.append("^");
				// appending total values 
				for (int j2 = 0; j2 < datearray.size(); j2++) {
					bw.append(totalarray.get(j2).toString());
					bw.append("^");
				}
				/*bw.append(totalarray.get(1).toString());
				bw.append("^");
				bw.append(totalarray.get(2).toString());
				bw.append("^");*/
				bw.newLine();

			}
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

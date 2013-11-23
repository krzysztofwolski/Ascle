
public class AnnDataContainer {
	private Integer age;
	private Integer sex;
	private Float cp;
	private Float trestbps;
	private Float chol;
	private Float fbs;
	private Float restecg;
	private Float exang;
	private Float oldpeak;
	private Float slope;
	private Float ca;
	private Float thal;
	
	private final int numAttr = 12;
	
	AnnDataContainer(Integer vAge,Boolean vSex,Float cp2,Float vTrestbps,Float vChol,Float vFbs,Float vRestecg,
					Float vExang,Float vOldpeak,Float vSlope,Float vCa,Float vThal){
		this.age = vAge;
		if(vSex)
			this.sex = 1;
		else
			this.sex = 0;
		this.cp = cp2;
		this.trestbps = vTrestbps;
		this.chol = vChol;
		this.fbs = vFbs;
		this.restecg = vRestecg;
		this.exang = vExang;
		this.oldpeak = vOldpeak;
		this.slope = vSlope;
		this.ca = vCa;
		this.thal = vThal;
		
	}
	/*
	0 age
	1 sex
	2 cp
	3 trestbps
	4 chol
	5 fbs
	6 restecg
	7 exang
	8 oldpeak
	9 slope
	10 ca
	11 thal
	*/
	public double[] toArray(){
		double[] returnArray = new double[this.numAttr];
		
		returnArray[0] = this.age;
		returnArray[1] = this.sex;
		returnArray[2] = this.cp;
		returnArray[3] = this.trestbps;
		returnArray[4] = this.chol;
		returnArray[5] = this.fbs;
		returnArray[6] = this.restecg;
		returnArray[7] = this.exang;
		returnArray[8] = this.oldpeak;
		returnArray[9] = this.slope;
		returnArray[10] = this.ca;
		returnArray[11] = this.thal;
		
		return returnArray;
		
	}
}

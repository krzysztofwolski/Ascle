
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
	
	AnnDataContainer(Integer vAge,Boolean vSex,float cp2,Float vTrestbps,Float vChol,Float vFbs,Float vRestecg,
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
	
}

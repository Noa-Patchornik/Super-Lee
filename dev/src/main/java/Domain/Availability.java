package Domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;

public class Availability {
    /**
     * a class that represent availability of an employee for a specific week with the constraint
     */

    private int AvID;
    private String startWeekRange;
    private String endWeekRange;
    private int[][] availabilityArr;

    public Availability(String startWeekRange, String endWeekRange,int[][]availabilityArr) {
        this.startWeekRange = startWeekRange;
        this.endWeekRange = endWeekRange;
        this.availabilityArr = new int[GlobalsVar.getNUMOFSHIFTSPERDAY()][GlobalsVar.getNUMOFWORKDAYSATWEEK()];
        this.availabilityArr=availabilityArr;
        this.AvID= GlobalsVar.getGeneralIDCounter();
    }
    public Availability(String startWeekRange, String endWeekRange){
        this.startWeekRange = startWeekRange;
        this.endWeekRange = endWeekRange;
        this.availabilityArr = new int[GlobalsVar.getNUMOFSHIFTSPERDAY()][GlobalsVar.getNUMOFWORKDAYSATWEEK()];
        this.AvID= GlobalsVar.getGeneralIDCounter();
        for(int i=0; i<availabilityArr.length; i++){
            for(int j=0; j<availabilityArr[0].length; j++){
                availabilityArr[i][j]=1;
            }
        }
    }

    public int[][] getAvailabilityArr() {
        return availabilityArr;
    }

    public int getAvID() {
        return AvID;
    }

    public String getStartWeekRange(){
        return this.startWeekRange;
    }

    public String getEndWeekRange(){
        return this.endWeekRange;
    }


}

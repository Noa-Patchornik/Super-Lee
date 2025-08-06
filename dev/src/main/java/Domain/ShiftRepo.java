package Domain;

import Presentation.AssigningPres;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShiftRepo {

    private List<Shift> shiftHistory = new ArrayList<>();

    private List<AssigningPres> assigningPresList = new ArrayList<>();

    List<Availability> availabilityList = new ArrayList<>();
    private WorkerRepo WR = new WorkerRepo();


    public Shift bringShiftbyDatenType(String date, int type, Branch b) {
        for (int i = 0; i < this.shiftHistory.size(); i++) {
            if (this.shiftHistory.get(i).getShiftdate().compareTo(date) == 0 && this.shiftHistory.get(i).getShiftType() == type &&
                    this.shiftHistory.get(i).getBranch() == b) {
                return this.shiftHistory.get(i);
            }
        }
        return null;
    }

    public boolean deleteShiftfromDB(int type, String date, String b) {
        for (int i = 0; i < this.shiftHistory.size(); i++) {
            if (this.shiftHistory.get(i).getShiftdate().compareTo(date) == 0 && this.shiftHistory.get(i).getShiftType() == type &&
                    String.valueOf(this.shiftHistory.get(i).getBranch()).compareTo(b) == 0) {
                this.shiftHistory.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean savetoHistory(Shift tempshift) throws Exception {
        this.shiftHistory.add(tempshift);
        return true;

    }


    /**
     * the function update the shift manager after the assigning employees to the shift
     *
     * @param type      - the shift type
     * @param date      - the date of the shift
     * @param newmanger - the manager
     * @throws Exception - if the shift is not created
     */
    public boolean updateShiftManager(int type, String date, int newmanger) throws Exception {
        for (int i = 0; i < this.shiftHistory.size(); i++) {
            if (this.shiftHistory.get(i).getShiftdate().compareTo(date) == 0 && this.shiftHistory.get(i).getShiftType() == type) {
                this.shiftHistory.get(i).setShiftManager(WR.readarecfromDB(newmanger));
                return true;
            }
        }
        return false;
    }

    /**
     * the function update and saves to the database the employees whose working in a given shift
     *
     * @param type    - the shift type
     * @param date    - the shift date
     * @param tmplist - the list of the employees who worked
     * @throws Exception - if the shift is not in the database
     */
    public boolean updateShiftWorkeres(int type, String date, Map<Role, List<Worker>> tmplist) {
        for (int i = 0; i < this.shiftHistory.size(); i++) {
            if (this.shiftHistory.get(i).getShiftdate().compareTo(date) == 0 && this.shiftHistory.get(i).getShiftType() == type) {
                this.shiftHistory.get(i).setParticipatesWorkersinShift(tmplist);
                return true;
            }
        }
        return false;
    }

    public boolean updateShiftCounterRoles(int type, String date, Map<Role, Integer> dict, String branch) throws Exception {
        for (int i = 0; i < this.shiftHistory.size(); i++) {
            if (this.shiftHistory.get(i).getShiftdate().compareTo(date) == 0 && this.shiftHistory.get(i).getShiftType() == type) {
                this.shiftHistory.get(i).setCounterNeededRoles(dict);
                return true;
            }
        }
        return false;
    }

    public boolean deleteassingfromDB(AssigningPres assigningPres) throws Exception {
        for (int i = 0; i < this.assigningPresList.size(); i++) {
            if (assigningPres.getStartDateRange().compareTo(assigningPresList.get(i).getStartDateRange()) == 0) {
                assigningPresList.remove(assigningPres);
                System.out.println("Update succeed.");
                return true;
            }
        }
        System.out.println("update wasn't succeed");
        return false;

    }

    public void saveassign(AssigningPres assigningPres) {
        this.assigningPresList.add(assigningPres);
    }

    public AssigningPres bringAssigningPres(String startdate,Branch b) {
        for (int i = 0; i < this.assigningPresList.size(); i++) {
            if(this.assigningPresList.get(i).getStartDateRange().compareTo(startdate) == 0 &&
                    this.assigningPresList.get(i).getBranch().compareTo(b)==0) {
                return this.assigningPresList.get(i);
            }
        }
        return null;
    }


    public List<Shift> getAllShifts() {
        return this.shiftHistory;
    }


}

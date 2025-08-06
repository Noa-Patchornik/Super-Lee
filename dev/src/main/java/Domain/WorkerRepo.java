package Domain;

import java.time.LocalDate;
import java.util.*;

public class WorkerRepo {

    private List<Worker> allCurrentWorkers = new ArrayList<>();
    private List<Worker> allPreviousWorkers = new ArrayList<>();

    public Worker readarecfromDB(int tempID) throws Exception {
        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            if (this.allCurrentWorkers.get(i).getWorkerID() == tempID) {
                return this.allCurrentWorkers.get(i);
            }
        }

        return null;
    }

    public boolean FireaWorkerDB(int tempID) throws Exception {
        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            if (this.allCurrentWorkers.get(i).getWorkerID() == tempID) {
                Worker w = this.allCurrentWorkers.get(i);
                this.allCurrentWorkers.remove(i);
                this.allPreviousWorkers.add(w);

                return true;
            }
        }
        return false;
    }

    public boolean saveworkertoDB(Worker tempWorker) throws Exception {
        if (!IsCurr(tempWorker.getWorkerID())) {
            this.allCurrentWorkers.add(tempWorker);
            return true;
        } else {
            return false;
        }
    }

    public Worker readPrevWorker(int id) throws Exception {
        for (int i = 0; i < this.allPreviousWorkers.size(); i++) {
            if (this.allPreviousWorkers.get(i).getWorkerID() == id)
                return allPreviousWorkers.get(i);
        }
        return null;
    }

    public List<Worker> bringworkersbyenum(Role temprole) throws Exception {
        List<Worker> returnlist = new ArrayList<>();
        List<Role> tmplist = new ArrayList<>();
        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            tmplist = this.allCurrentWorkers.get(i).getRole();
            for (int j = 0; j < tmplist.size(); j++) {
                if (tmplist.get(j) == temprole)
                    returnlist.add(this.allCurrentWorkers.get(i));
            }
        }
        return returnlist;
    }

    public boolean updatePer(Worker tempworker, Role tempRole) throws Exception {
        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            if (this.allCurrentWorkers.get(i).getWorkerID() == tempworker.getWorkerID()) {
                this.allCurrentWorkers.get(i).getRole().add(tempRole);
                System.out.println("adding a new role was successful");
                return true;
            }
        }
        return false;
    }
    public boolean deletePer(Worker tempworker, Role tempRole) throws Exception {
        List<Role> tmplist;

        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            if (this.allCurrentWorkers.get(i).getWorkerID() == tempworker.getWorkerID()) {
                tmplist = this.allCurrentWorkers.get(i).getRole();
                for (int j = 0; j < tmplist.size(); j++) {
                    if (tmplist.get(j) == tempRole) {
                        tmplist.remove(j);
                        System.out.println("removing an existing role was successful");
                        this.allCurrentWorkers.get(i).setRole(tmplist);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Worker> bringworkersbycontractdate() {
        LocalDate today = LocalDate.now();
        Contract tmpcontract;
        List<Worker> tmplist = new ArrayList<>();
        int diff;
        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            tmpcontract = this.allCurrentWorkers.get(i).getWorkerContract();
            diff = (int) calculateDifferenceInDays(tmpcontract.getEndDateRange(), String.valueOf(today));
            if (diff < 30) {
                tmplist.add(this.allCurrentWorkers.get(i));
            }
        }
        return tmplist;
    }

    public static int calculateDifferenceInDays(String shift, String start) {
        String[] parts = shift.split("/");
        int date = 0, date2 = 0;
        try {
            date = Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException e) {
        }
        String[] parts2 = start.split("/");
        try {
            date2 = Integer.parseInt(parts2[0].trim());
        } catch (NumberFormatException e) {
        }
        int ans = date - date2;
        if (ans >= 0)
            return ans;
        else return date+30-date2;


    }

    public boolean updateContract(Contract tmpcon, int tmpID) throws Exception {
        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            if (this.allCurrentWorkers.get(i).getWorkerID() == tmpID) {
                Worker w = this.allCurrentWorkers.get(i);
                this.allCurrentWorkers.remove(i);
                w.setContract(tmpcon);
                this.allCurrentWorkers.add(w);
                return true;
            }
        }
        return false;
    }

    public Map<Integer, Double> collectallsalaries() {
        Map<Integer, Double> tmpdict = new HashMap<>();
        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            tmpdict.put(this.allCurrentWorkers.get(i).getWorkerID(), (double) this.allCurrentWorkers.get(i).getSalary());
        }
        return tmpdict;
    }

    public boolean IsCurr(int workerID) throws Exception {
        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            if (this.allCurrentWorkers.get(i).getWorkerID() == workerID)
                return true;
        }
        return false;
    }

    public boolean savePrevworkertoDB(Worker worker) throws Exception {
        for(int i=0; i<this.allCurrentWorkers.size(); i++){
            if(allCurrentWorkers.get(i).getWorkerID()==worker.getWorkerID()){
               return false;
            }
        }
        this.allPreviousWorkers.add(worker);
        return true;
    }
    public Map<Integer, Double> collectallsalariesbyenum(List<Worker> list) {
        Map<Integer, Double> tmpdict = new HashMap<>();
        for (int j = 0; j < list.size(); j++) {
            for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
                if (this.allCurrentWorkers.get(i).getWorkerID() == list.get(j).getWorkerID()) {
                    double x = (double) this.allCurrentWorkers.get(i).getSalary();
                    tmpdict.put(this.allCurrentWorkers.get(i).getWorkerID(), x);
                }
            }
        }
        return tmpdict;
    }

    public List<Worker> readdworkersbybranch(Branch tmpb) throws Exception {
        List<Worker > tmplist = new ArrayList<>();
        for(int i=0;i<this.allCurrentWorkers.size();i++){
            if(tmpb==this.allCurrentWorkers.get(i).getmyBranch()){
                tmplist.add(allCurrentWorkers.get(i));
            }
        }
        return tmplist;
    }

    public Availability readav(int id, String date){
        for(int i=0;i<allCurrentWorkers.size();i++) {
            if (this.allCurrentWorkers.get(i).getWorkerID() == id) {
                for (String key : this.allCurrentWorkers.get(i).getMyConstraintsperWeek().keySet()) {
                    if (this.allCurrentWorkers.get(i).getMyConstraintsperWeek().get(key).getStartWeekRange().compareTo(date) == 0) {
                        return this.allCurrentWorkers.get(i).getMyConstraintsperWeek().get(key);
                    }
                }
            }
        }
        return null;
        }

    public boolean insertAv(int id, int[] morning, int[] evening, String startdate, String endDate) {
        int [][] ans= new int[2][7];
        ans[0]=morning;
        ans[1]=evening;
        Availability av = new Availability(startdate,endDate,ans);
        for(int i=0;i<allCurrentWorkers.size();i++){
            if(allCurrentWorkers.get(i).getWorkerID()==id){
                allCurrentWorkers.get(i).getMyConstraintsperWeek().put(startdate,av);
                return true;
            }
        }
        return false;

    }

    public boolean deleteAv(int id, String date) {
        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            if (this.allCurrentWorkers.get(i).getWorkerID() == id) {
                this.allCurrentWorkers.get(i).getMyConstraintsperWeek().remove(date);
                return true;
            }
        }

        return false;
    }

    public void deletePrev(int id) {
        for(int i=0;i<this.allPreviousWorkers.size();i++){
            if(this.allPreviousWorkers.get(i).getWorkerID()==id)
                this.allPreviousWorkers.remove(i);
            return;
        }
    }

    public void insertWorker(Worker returnw) {
        this.allCurrentWorkers.add(returnw);
    }

    public List<Worker> getAllCurrWorkers() {
        return this.allCurrentWorkers;
    }

}


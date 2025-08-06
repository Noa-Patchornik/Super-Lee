package Domain;

import java.util.List;
import java.util.Map;

public class ShiftToSend{

        public int id;
        public String date;
        public int type;
        public Branch b;
        public WorkerToSend manager;
        public Map<Role,Integer> counter;
        public Map<Role,List<WorkerToSend>> participent;
        public List<TransportToSend> transports;

        public ShiftToSend(int id, String date, int type, Branch b, WorkerToSend m, Map<Role,Integer> count,
                           Map<Role,List<WorkerToSend>> par, List<TransportToSend> tran){

            this.id =id;
            this.date=date;
            this.transports=tran;
            this.b=b;
            this.type=type;
            this.manager=m;
            this.counter=count;
            this.participent=par;

        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Branch getB() {
        return b;
    }

    public void setB(Branch b) {
        this.b = b;
    }

    public WorkerToSend getManager() {
        return manager;
    }

    public void setManager(WorkerToSend manager) {
        this.manager = manager;
    }

    public Map<Role, Integer> getCounter() {
        return counter;
    }

    public void setCounter(Map<Role, Integer> counter) {
        this.counter = counter;
    }

    public List<TransportToSend> getTransports() {
        return transports;
    }

    public void setTransports(List<TransportToSend> transports) {
        this.transports = transports;
    }

    public Map<Role,List<WorkerToSend>> getParticipent() {
        return participent;
    }

    public void setParticipent(Map<Role,List<WorkerToSend>> participent) {
        this.participent = participent;
    }
}
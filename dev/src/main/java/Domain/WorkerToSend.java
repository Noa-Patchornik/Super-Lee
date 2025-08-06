package Domain;

import java.util.List;
import java.util.Map;

public class WorkerToSend {

    public int id;
    public String firstname;
    public String lastname;
    public String bankAccount;
    public ContractToSend c;
    public List<Role>roles;
    public Branch branch;

    public WorkerToSend(int id, String fn, String ln, String ba, ContractToSend c, List<Role> roles, Branch b){
        this.id=id;
        this.firstname=fn;
        this.lastname=ln;
        this.bankAccount=ba;
        this.c=c;
        this.roles=roles;
        this.branch=b;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public ContractToSend getC() {
        return c;
    }

    public void setC(ContractToSend c) {
        this.c = c;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}

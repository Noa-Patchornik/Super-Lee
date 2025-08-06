package Service;

public class Response<T> {
    public String errormsg;
    public T valuetosend;

    public Response(){}

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public T getValuetosend() {
        return valuetosend;
    }

    public void setValuetosend(T valuetosend) {
        this.valuetosend = valuetosend;
    }


    public String getErrormsg(){
        return this.errormsg;
    }

}

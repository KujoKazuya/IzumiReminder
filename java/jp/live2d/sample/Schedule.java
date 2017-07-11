package jp.live2d.sample;


/**
 * Created by PIK-R-5 on 8/31/2016.
 */
public class Schedule {
    int _id;
    String _date;
    String _time;
    String _todo;

    public Schedule(){

    }
    public Schedule(int id, String _date, String _time, String _todo ){
        this._id = id;
        this._date = _date;
        this._time = _time;
        this._todo= _todo;
    }

    // constructor
    public Schedule(String _date, String _time, String _todo){
        this._date = _date;
        this._time = _time;
        this._todo= _todo;
    }

    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    public String getDate(){
        return this._date;
    }

    // setting id
    public void setDate(String date){
        this._date = date;
    }
    public String getTime(){
        return this._time;
    }

    // setting id
    public void setTime(String time){
        this._time = time;
    }
    public String getTodo(){
        return this._todo;
    }

    // setting id
    public void setTodo(String todo){
        this._todo = todo;
    }
}

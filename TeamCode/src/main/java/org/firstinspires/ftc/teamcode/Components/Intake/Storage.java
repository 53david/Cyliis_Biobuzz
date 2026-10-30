package org.firstinspires.ftc.teamcode.Components.Intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

public class Storage {
    DigitalChannel sensor1,sensor2,sensor3,sensor4;
    boolean slot1,slot2,slot3,slot4;
    ElapsedTime timer;
    public enum State{
        READY_FOR_TRANSFER,
        NO_POLLEN,
        WAITING_FOR_POLLEN,
    };
    State state;
    public Storage (){
        timer = new ElapsedTime();
        state = State.NO_POLLEN;
        sensor1 = Hardware.bb1;
        sensor2 = Hardware.bb2;
        sensor3 = Hardware.bb3;
        sensor4 = Hardware.bb4;

    }
    public void update(){
        slot1 = sensor1.getState();
        slot2 = sensor2.getState();
        slot3 = sensor3.getState();
        slot4 = sensor4.getState();

        if (slot1 && slot2 && slot3 && slot4) {timer.reset();setState(State.READY_FOR_TRANSFER);}
        else if (slot1 || slot2 || slot3 || slot4) {timer.reset();setState(State.WAITING_FOR_POLLEN);}
        else if (timer.seconds()>0.052) setState(State.NO_POLLEN);
    }
    public void setState(State state){
        this.state = state;
    }
    public boolean getStateSensor1(){return slot1;}
    public boolean getStateSensor2(){
        return slot2;
    }
    public boolean getStateSensor3(){
        return slot3;
    }
    public boolean getStateSensor4(){
        return slot4;
    }
    public State getState(){
        return state;
    }
}
